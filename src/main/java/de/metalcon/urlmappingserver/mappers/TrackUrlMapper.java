package de.metalcon.urlmappingserver.mappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
import de.metalcon.urlmappingserver.ExceptionFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;

/**
 * mapper for track entities
 * 
 * @author sebschlicht
 * 
 */
public class TrackUrlMapper extends EntityUrlMapper {

    /**
     * mapper for record entities to ensure parental mapping tree
     */
    protected RecordUrlMapper recordMapper;

    /**
     * all (mappings to tracks) of a record
     */
    protected Map<Muid, Map<String, Muid>> mappingsToTracksOfRecords;

    /**
     * parental records [track -> record]<br>
     * does not include entry if record is empty
     */
    protected Map<Muid, Muid> parentalRecords;

    /**
     * parental bands [track -> band]<br>
     * does not include entry if record not empty<br>
     * or if band is empty
     */
    protected Map<Muid, Muid> parentalBands;

    /**
     * create mapper for track entities
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     * @param recordMapper
     *            mapper for record entities to ensure parental mapping tree
     */
    public TrackUrlMapper(
            EntityUrlMappingManager manager,
            RecordUrlMapper recordMapper) {
        super(manager, MuidType.TRACK, false, "pathTrack");
        this.recordMapper = recordMapper;
        mappingsToTracksOfRecords = new HashMap<Muid, Map<String, Muid>>();
        parentalRecords = new HashMap<Muid, Muid>();
        parentalBands = new HashMap<Muid, Muid>();
    }

    public void setMappingsToTracksOfRecords(
            Map<Muid, Map<String, Muid>> mappingsToTracksOfRecords,
            Map<Muid, Muid> parentalBands) {
        this.mappingsToTracksOfRecords = mappingsToTracksOfRecords;

        Map<String, Muid> mappingsToTrack;
        Set<String> mappingsOfTrack;
        Muid muidTrack;

        // iterate over all records
        for (Muid muidRecord : mappingsToTracksOfRecords.keySet()) {
            mappingsToTrack = mappingsToTracksOfRecords.get(muidRecord);

            // iterate over all track mappings for this record
            for (String trackMapping : mappingsToTrack.keySet()) {
                muidTrack = mappingsToTrack.get(trackMapping);

                mappingsOfTrack = mappingsOfEntities.get(muidTrack);
                if (mappingsOfTrack == null) {
                    mappingsOfTrack = createEmptyMappingSet();
                    mappingsOfEntities.put(muidTrack, mappingsOfTrack);

                    // register parental record
                    if (muidRecord.getID() != 0) {
                        parentalRecords.put(muidTrack, muidRecord);
                    } else {
                        // register parental band
                        Muid muidBand = parentalBands.get(muidTrack);
                        if (muidBand != null) {
                            this.parentalBands.put(muidTrack, muidBand);
                        }
                        // else parental band is empty too
                    }
                }

                mappingsOfTrack.add(trackMapping);
            }
        }
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        checkMuidType(entityUrlData.getMuid().getMuidType());
        TrackUrlData trackUrlData = (TrackUrlData) entityUrlData;

        // register record if not registered yet
        if (!recordMapper.checkForRecord(trackUrlData.getRecord())) {
            recordMapper.registerMuid(trackUrlData.getRecord());
        }

        // switch into track mapping
        Muid record = trackUrlData.getRecord().getMuid();
        mappingToEntity = mappingsToTracksOfRecords.get(record);
        if (mappingToEntity == null) {
            mappingToEntity = new HashMap<String, Muid>();
            mappingsToTracksOfRecords.put(record, mappingToEntity);
        }

        // register track parents for URL resolves
        if (!trackUrlData.getRecord().hasEmptyMuid()) {
            // track has parental record
            parentalRecords.put(trackUrlData.getMuid(), record);
        } else {
            // parental record is empty
            if (!trackUrlData.getRecord().getBand().hasEmptyMuid()) {
                // track has parental band
                parentalBands.put(trackUrlData.getMuid(), trackUrlData
                        .getRecord().getBand().getMuid());
                persistentStorage
                        .saveParent(trackUrlData.getMuid().getValue(),
                                trackUrlData.getRecord().getBand().getMuid()
                                        .getValue());
            }
        }

        Set<String> newMappingsForTrack = createEmptyMappingSet();

        // add mapping: /<track name>
        String nameMapping = convertToUrlText(trackUrlData.getName());
        newMappingsForTrack.add(nameMapping);

        int trackNumber = trackUrlData.getTrackNumber();
        if (trackNumber != 0) {
            String sTrackNumber = formatTrackNumber(trackNumber);

            // add mapping: /<track number>-<track name>
            newMappingsForTrack.add(sTrackNumber + WORD_SEPARATOR
                    + convertToUrlText(trackUrlData.getName()));
        }

        // add mapping: /<track name>-<muid>
        String uniqueMapping =
                nameMapping + WORD_SEPARATOR + trackUrlData.getMuid();
        newMappingsForTrack.add(uniqueMapping);

        return newMappingsForTrack;
    }

    @Override
    protected void storeMapping(EntityUrlData entity, String mapping) {
        // MUID can not be empty here, parental record MUID gets set
        TrackUrlData track = (TrackUrlData) entity;
        persistentStorage.saveMapping(entity.getMuid().getMuidType()
                .getRawIdentifier(), entity.getMuid().getValue(), mapping,
                track.getRecord().getMuid().getValue());
    }

    @Override
    public Muid resolveMuid(Map<String, String> url, MuidType type) {
        checkMuidType(type);

        // resolve record
        Muid record = resolveOtherMuid(url, MuidType.RECORD);
        if (record != null) {
            // resolve track        
            String trackMapping = getPathVar(url, urlPathVarName);
            Map<String, Muid> mappingToEntity =
                    mappingsToTracksOfRecords.get(record);
            if (mappingToEntity != null) {
                // may be null if track of another record than specified
                return mappingToEntity.get(trackMapping);
            }
            // no tracks for record registered -> hierarchy invalid
        }
        return null;
    }

    @Override
    public String resolveUrl(Muid muidTrack) {
        if (mappingsOfEntities.containsKey(muidTrack)) {
            // track was registered
            String urlTrack = super.resolveUrl(muidTrack);

            Muid muidRecord = parentalRecords.get(muidTrack);
            if (muidRecord != null) {
                // track has parental record
                String urlRecord = recordMapper.resolveUrl(muidRecord);
                if (urlRecord != null) {
                    return urlRecord + PATH_SEPARATOR + urlTrack;
                }
                // parental record could not be resolved
                throw ExceptionFactory.internalUrlResolveFailed(muidRecord);
            } else {
                // parental record is empty
                Muid muidBand = parentalBands.get(muidTrack);
                if (muidBand != null) {
                    // track has parental band
                    String urlBand =
                            manager.getMapper(MuidType.BAND).resolveUrl(
                                    muidBand);
                    if (urlBand != null) {
                        return urlBand + PATH_SEPARATOR + EMPTY_ENTITY
                                + PATH_SEPARATOR + urlTrack;
                    }
                    // parental band could not be resolved
                    throw ExceptionFactory.internalUrlResolveFailed(muidBand);
                } else {
                    // parental band is empty too
                    return EMPTY_ENTITY + PATH_SEPARATOR + EMPTY_ENTITY
                            + PATH_SEPARATOR + urlTrack;
                }
            }
        }
        // track is unknown
        checkMuidType(muidTrack.getMuidType());
        return null;
    }

    /**
     * format track number
     * 
     * @param trackNumber
     *            track number to be formatted
     * @return formatted track number
     */
    protected static String formatTrackNumber(int trackNumber) {
        // return String.format("%02d");
        return String.valueOf(trackNumber);
    }

}
