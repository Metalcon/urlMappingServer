package de.metalcon.urlmappingserver.mappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.metalcon.domain.EntityType;
import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;

/**
 * mapper for record entities
 * 
 * @author sebschlicht
 * 
 */
public class RecordUrlMapper extends EntityUrlMapper {

    /**
     * all (mappings to records) of a band
     */
    protected Map<Muid, Map<String, Muid>> mappingsToRecordsOfBands;

    /**
     * create mapper for record entities
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    public RecordUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, EntityType.RECORD, "pathRecord");
    }

    /**
     * access to record mapping for track mapper
     * 
     * @return all (mappings to records) of a band
     */
    public Map<Muid, Map<String, Muid>> getMappingsToRecordsOfBands() {
        return mappingsToRecordsOfBands;
    }

    /**
     * access to record MUIDs for track mapper
     * 
     * @return all mappings of records
     */
    public Map<Muid, Set<String>> getMappingsOfEntities() {
        return mappingsOfEntities;
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        Set<String> newMappingsForRecord = super.createMapping(entityUrlData);
        RecordUrlData recordUrlData = (RecordUrlData) entityUrlData;

        // switch into band mapping
        Muid band =
                (recordUrlData.getBand() != null) ? recordUrlData.getBand()
                        .getMuid() : Muid.EMPTY_MUID;
        mappingToEntity = mappingsToRecordsOfBands.get(band);
        if (mappingToEntity == null) {
            mappingToEntity = new HashMap<String, Muid>();
            mappingsToRecordsOfBands.put(band, mappingToEntity);
        }

        // add mapping: /<band>/<release year>-<record name>
        int releaseYear = recordUrlData.getReleaseYear();
        if (releaseYear != 0) {
            String sReleaseYear = String.valueOf(releaseYear);
            newMappingsForRecord.add(sReleaseYear + WORD_SEPERATOR
                    + convertToUrlText(recordUrlData.getName()));
        }

        return newMappingsForRecord;
    }

    @Override
    public Muid resolveMuid(Map<String, String> url, EntityType type) {
        String recordMapping = getPathVar(url, urlPathVarName);

        // allow empty MUIDs to access tracks of a band
        if (recordMapping.equals(EMPTY_ENTITY)) {
            return Muid.EMPTY_MUID;
        }

        // resolve band
        Muid band = resolveOtherMuid(url, EntityType.BAND);
        if (band != null) {
            // resolve record
            Map<String, Muid> mappingToEntity =
                    mappingsToRecordsOfBands.get(band);
            if (mappingToEntity != null) {
                return mappingToEntity.get(recordMapping);
            }
        }

        return null;
    }

}
