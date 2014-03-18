package de.metalcon.urlmappingserver.mappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.metalcon.domain.EntityType;
import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;

public class TrackUrlMapper extends EntityUrlMapper {

    /**
     * all (mappings to records) of a band
     */
    protected Map<Muid, Map<String, Muid>> mappingsToRecordsOfBands;

    /**
     * all (mappings to tracks) of a record
     */
    protected Map<Muid, Map<String, Muid>> mappingsToTracksOfRecords;

    /**
     * create mapper for track entities
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     * @param mappingsToRecordsOfBands
     *            all (mappings to records) of a band
     */
    public TrackUrlMapper(
            EntityUrlMappingManager manager,
            Map<Muid, Map<String, Muid>> mappingsToRecordsOfBands) {
        super(manager, EntityType.TRACK, "pathTrack");
        this.mappingsToRecordsOfBands = mappingsToRecordsOfBands;
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        Set<String> newMappingsForTrack = super.createMapping(entityUrlData);
        TrackUrlData trackUrlData = (TrackUrlData) entityUrlData;

        // switch into band mapping
        Muid band =
                (trackUrlData.getBand() != null) ? trackUrlData.getBand()
                        .getMuid() : Muid.EMPTY_MUID;
        mappingToEntity = mappingsToRecordsOfBands.get(band);
        if (mappingToEntity == null) {
            mappingToEntity = new HashMap<String, Muid>();
            mappingsToRecordsOfBands.put(band, mappingToEntity);
        }

        // switch into record mapping
        Muid record =
                (trackUrlData.getRecord() != null) ? trackUrlData.getRecord()
                        .getMuid() : Muid.EMPTY_MUID;
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
    public Muid resolveMuid(Map<String, String> url, EntityType type) {
        // resolve record
        Muid record = resolveOtherMuid(url, EntityType.RECORD);

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
