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

public class RecordUrlMapper extends EntityUrlMapper {

    /**
     * all (mappings to records) of a band
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    protected Map<Muid, Map<String, Muid>> mappingsToRecordsOfBands;

    public RecordUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, EntityType.RECORD, "pathRecord");
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

        // add mapping: /<band name>/<release year>-<record name>
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
        // resolve band
        Muid band = resolveOtherMuid(url, EntityType.BAND);

        String mapping = getPathVar(url, urlPathVarName);
        Map<String, Muid> mappingToEntity = mappingsToRecordsOfBands.get(band);
        return mappingToEntity.get(mapping);
    }

}
