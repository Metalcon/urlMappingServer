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
        super(manager, "pathTrack");
        this.recordMapper = recordMapper;
        mappingsToTracksOfRecords = new HashMap<Muid, Map<String, Muid>>();
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        Set<String> newMappingsForTrack = super.createMapping(entityUrlData);
        TrackUrlData trackUrlData = (TrackUrlData) entityUrlData;

        Muid band =
                (trackUrlData.getBand() != null) ? trackUrlData.getBand()
                        .getMuid() : Muid.EMPTY_MUID;
        Muid record =
                (trackUrlData.getRecord() != null) ? trackUrlData.getRecord()
                        .getMuid() : Muid.EMPTY_MUID;

        // register record for band if not registered yet
        if (recordMapper.getMappingsToRecordsOfBands().get(band) == null
                || !recordMapper.getMappingsOfEntities().containsKey(record)) {
            recordMapper.registerMuid(trackUrlData.getRecord());
        }

        // switch into record mapping
        mappingToEntity = mappingsToTracksOfRecords.get(record);
        if (mappingToEntity == null) {
            mappingToEntity = new HashMap<String, Muid>();
            mappingsToTracksOfRecords.put(record, mappingToEntity);
        }

        int trackNumber = trackUrlData.getTrackNumber();
        if (trackNumber != 0) {
            String sTrackNumber = formatTrackNumber(trackNumber);

            // add mapping:/<band>/<record>/<track number>
            newMappingsForTrack.add(sTrackNumber);
            // add mapping: /<band>/<record>/<track number>-<track name>
            newMappingsForTrack.add(sTrackNumber + WORD_SEPERATOR
                    + convertToUrlText(trackUrlData.getName()));
        }

        return newMappingsForTrack;
    }

    @Override
    public Muid resolveMuid(Map<String, String> url, MuidType type) {
        // resolve record
        Muid record = resolveOtherMuid(url, MuidType.RECORD);

        // resolve track        
        String trackMapping = getPathVar(url, urlPathVarName);
        Map<String, Muid> mappingToEntity =
                mappingsToTracksOfRecords.get(record);
        return mappingToEntity.get(trackMapping);
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
