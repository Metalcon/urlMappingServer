package de.metalcon.urlmappingserver.mappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
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
     * mapper for band entities to ensure parental mapping tree
     */
    protected BandUrlMapper bandMapper;

    /**
     * all (mappings to records) of a band
     */
    protected Map<Muid, Map<String, Muid>> mappingsToRecordsOfBands;

    /**
     * create mapper for record entities
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     * @param bandMapper
     *            mapper for band entities to ensure parental mapping tree
     */
    public RecordUrlMapper(
            EntityUrlMappingManager manager,
            BandUrlMapper bandMapper) {
        super(manager, MuidType.RECORD, true, "pathRecord");
        this.bandMapper = bandMapper;
        mappingsToRecordsOfBands = new HashMap<Muid, Map<String, Muid>>();
    }

    /**
     * access to record mapping for track mapper
     * 
     * @return all (mappings to records) of a band
     */
    public Map<Muid, Map<String, Muid>> getMappingsToRecordsOfBands() {
        return mappingsToRecordsOfBands;
    }

    public void setMappingsOfRecordsOfBand(
            Map<Muid, Map<String, Muid>> mappingsOfRecordsOfBand) {
        mappingsToRecordsOfBands = mappingsOfRecordsOfBand;

        Map<String, Muid> mappingsToRecord;
        Set<String> mappingsOfRecord;
        Muid muidRecord;

        // iterate over all bands
        for (Muid muidBand : mappingsOfRecordsOfBand.keySet()) {
            mappingsToRecord = mappingsOfRecordsOfBand.get(muidBand);

            // iterate over all record mappings for this band
            for (String recordMapping : mappingsToRecord.keySet()) {
                muidRecord = mappingsToRecord.get(recordMapping);

                mappingsOfRecord = mappingsOfEntities.get(muidRecord);
                if (mappingsOfRecord == null) {
                    mappingsOfRecord = createEmptyMappingSet();
                    mappingsOfEntities.put(muidRecord, mappingsOfRecord);
                }

                mappingsOfRecord.add(recordMapping);
            }
        }
    }

    /**
     * @return all mappings of a record
     */
    public Map<Muid, Set<String>> getMappingsOfRecord() {
        return mappingsOfEntities;
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        RecordUrlData recordUrlData = (RecordUrlData) entityUrlData;

        // register band if not registered yet
        Muid band = recordUrlData.getBand().getMuid();
        if (!bandMapper.getMappingsOfBand().containsKey(band)) {
            bandMapper.registerMuid(recordUrlData.getBand());
        }

        // switch into record mapping
        mappingToEntity = mappingsToRecordsOfBands.get(band);
        if (mappingToEntity == null) {
            mappingToEntity = new HashMap<String, Muid>();
            mappingsToRecordsOfBands.put(band, mappingToEntity);
        }

        if (!recordUrlData.hasEmptyMuid()) {
            Set<String> newMappingsForRecord =
                    super.createMapping(entityUrlData);

            // add mapping: /<band>/<release year>-<record name>
            int releaseYear = recordUrlData.getReleaseYear();
            if (releaseYear != 0) {
                String sReleaseYear = String.valueOf(releaseYear);
                newMappingsForRecord.add(sReleaseYear + WORD_SEPARATOR
                        + convertToUrlText(recordUrlData.getName()));
            }

            return newMappingsForRecord;
        }
        return null;
    }

    @Override
    protected void storeMapping(EntityUrlData entity, String mapping) {
        // MUID can be empty here, parental band MUID gets set
        RecordUrlData record = (RecordUrlData) entity;
        persistentStorage.saveMapping(MuidType.RECORD.getRawIdentifier(),
                entity.getMuid().getValue(), mapping, record.getBand()
                        .getMuid().getValue());
    }

    @Override
    public Muid resolveMuid(Map<String, String> url, MuidType type) {
        if (type == muidType) {
            String recordMapping = getPathVar(url, urlPathVarName);

            // resolve band
            Muid band = resolveOtherMuid(url, MuidType.BAND);
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
        throw new IllegalArgumentException("mapper handles muid type \""
                + getMuidType() + "\" only (was: \"" + type + "\")");
    }

}
