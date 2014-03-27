package de.metalcon.urlmappingserver.mappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
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
    }

    public void setMappingsToTracksOfRecords(
            Map<Muid, Map<String, Muid>> mappingsToTracksOfRecords) {
        this.mappingsToTracksOfRecords = mappingsToTracksOfRecords;

        Map<String, Muid> mappingsToTrack;
        Set<String> mappingsOfTrack;
        Muid muidTrack;

        // iterate over all records
        for (Muid recordMuid : mappingsToTracksOfRecords.keySet()) {
            mappingsToTrack = mappingsToTracksOfRecords.get(recordMuid);

            // iterate over all track mappings for this record
            for (String trackMapping : mappingsToTrack.keySet()) {
                muidTrack = mappingsToTrack.get(trackMapping);

                mappingsOfTrack = mappingsOfEntities.get(muidTrack);
                if (mappingsOfTrack == null) {
                    mappingsOfTrack = createEmptyMappingSet();
                    mappingsOfEntities.put(muidTrack, mappingsOfTrack);
                }

                mappingsOfTrack.add(trackMapping);
            }
        }
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
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

        Set<String> newMappingsForTrack = super.createMapping(entityUrlData);

        int trackNumber = trackUrlData.getTrackNumber();
        if (trackNumber != 0) {
            String sTrackNumber = formatTrackNumber(trackNumber);

            // add mapping:/<band>/<record>/<track number>
            newMappingsForTrack.add(sTrackNumber);
            // add mapping: /<band>/<record>/<track number>-<track name>
            newMappingsForTrack.add(sTrackNumber + WORD_SEPARATOR
                    + convertToUrlText(trackUrlData.getName()));
        }

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
        if (type == muidType) {
            // resolve record
            Muid record = resolveOtherMuid(url, MuidType.RECORD);
            if (record != null) {
                // resolve track        
                String trackMapping = getPathVar(url, urlPathVarName);
                Map<String, Muid> mappingToEntity =
                        mappingsToTracksOfRecords.get(record);
                if (mappingToEntity != null) {
                    return mappingToEntity.get(trackMapping);
                }
            }
            return null;
        }
        throw new IllegalArgumentException("mapper handles muid type \""
                + getMuidType() + "\" only (was: \"" + type + "\")");
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
