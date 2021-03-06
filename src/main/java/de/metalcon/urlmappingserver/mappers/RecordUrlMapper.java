package de.metalcon.urlmappingserver.mappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.metalcon.domain.Muid;
import de.metalcon.domain.UidType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
import de.metalcon.urlmappingserver.ExceptionFactory;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
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
     * parental bands [record -> band]<br>
     * does not include entry if band is empty
     */
    protected Map<Muid, Muid> parentalBands;

    /**
     * create mapper for record entities
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     * @param bandMapper
     *            mapper for band entities to ensure parental mapping tree
     */
    public RecordUrlMapper(
            final EntityUrlMappingManager manager,
            final BandUrlMapper bandMapper) {
        super(manager, UidType.RECORD, true, "pathRecord");
        this.bandMapper = bandMapper;
        mappingsToRecordsOfBands = new HashMap<Muid, Map<String, Muid>>();
        parentalBands = new HashMap<Muid, Muid>();
    }

    /**
     * check if a record is registered
     * 
     * @param record
     *            record to be searched for
     * @return true - if record is registered
     */
    public boolean checkForRecord(final RecordUrlData record) {
        if (!record.hasEmptyMuid()) {
            // MUID is unique across all bands -> must exist
            return mappingsOfEntities.containsKey(record.getMuid());
        } else {
            // if MUID empty we have to check if existing for this band in specific
            BandUrlData band = record.getBand();
            if (mappingsToRecordsOfBands.containsKey(band.getMuid())) {
                return mappingsToRecordsOfBands.get(band.getMuid())
                        .containsKey(EMPTY_ENTITY);
            }
            return false;
        }
    }

    public void setMappingsOfRecordsOfBand(
            final Map<Muid, Map<String, Muid>> mappingsOfRecordsOfBand) {
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

                // register parental band
                if (!muidRecord.isEmpty()) {
                    parentalBands.put(muidRecord, muidBand);
                }

                mappingsOfRecord = mappingsOfEntities.get(muidRecord);
                if (mappingsOfRecord == null) {
                    mappingsOfRecord = createEmptyMappingSet();
                    mappingsOfEntities.put(muidRecord, mappingsOfRecord);
                }

                mappingsOfRecord.add(recordMapping);
            }
        }
    }

    @Override
    protected Set<String> createMapping(final EntityUrlData entityUrlData) {
        // TODO check MUID type
        RecordUrlData recordUrlData = (RecordUrlData) entityUrlData;

        // register band if not registered yet
        if (!bandMapper.checkForBand(recordUrlData.getBand())) {
            bandMapper.registerMuid(recordUrlData.getBand());
        }

        // switch into record mapping
        Muid band = recordUrlData.getBand().getMuid();
        mappingToEntity = mappingsToRecordsOfBands.get(band);
        if (mappingToEntity == null) {
            mappingToEntity = new HashMap<String, Muid>();
            mappingsToRecordsOfBands.put(band, mappingToEntity);
        }

        if (!recordUrlData.hasEmptyMuid()) {
            // register record parent for URL resolves
            parentalBands.put(recordUrlData.getMuid(), band);

            Set<String> newMappingsForRecord = createEmptyMappingSet();

            // add mapping: /<record name>
            String nameMapping = convertToUrlText(recordUrlData.getName());
            newMappingsForRecord.add(nameMapping);

            // add mapping: /<band>/<release year>-<record name>
            int releaseYear = recordUrlData.getReleaseYear();
            if (releaseYear != 0) {
                String sReleaseYear = String.valueOf(releaseYear);
                newMappingsForRecord.add(sReleaseYear + WORD_SEPARATOR
                        + convertToUrlText(recordUrlData.getName()));
            }

            // add mapping: /<record name>-<muid>
            String uniqueMapping =
                    nameMapping + WORD_SEPARATOR + recordUrlData.getMuid();
            newMappingsForRecord.add(uniqueMapping);

            return newMappingsForRecord;
        }
        return null;
    }

    @Override
    protected void
        storeMapping(final EntityUrlData entity, final String mapping) {
        // MUID can be empty here, parental band MUID gets set
        RecordUrlData record = (RecordUrlData) entity;
        persistentStorage.saveMapping(UidType.RECORD.getRawIdentifier(), entity
                .getMuid().getValue(), mapping, record.getBand().getMuid()
                .getValue());
    }

    @Override
    public Muid resolveMuid(final Map<String, String> url, final UidType type) {
        checkUidType(type);
        String recordMapping = getPathVar(url, urlPathVarName);

        // resolve band
        Muid band = resolveOtherMuid(url, UidType.BAND);
        if (band != null) {
            // resolve record
            if (mappingsToRecordsOfBands.containsKey(band)) {
                // may be null if record of another band than specified
                return mappingsToRecordsOfBands.get(band).get(recordMapping);
            }
            // no record for band registered -> hierarchy invalid
        }
        return null;
    }

    @Override
    public String resolveUrl(final Muid muidRecord) {
        // TODO: check MUID type
        if (mappingsOfEntities.containsKey(muidRecord)) {
            // record was registered
            String urlRecord = super.resolveUrl(muidRecord);

            Muid muidBand = parentalBands.get(muidRecord);
            if (muidBand != null) {
                // record has parental band
                String urlBand = bandMapper.resolveUrl(muidBand);
                if (urlBand != null) {
                    return urlBand + PATH_SEPARATOR + urlRecord;
                }
                // parental band could not be resolved
                throw ExceptionFactory.internalUrlResolveFailed(muidBand);
            } else {
                // parental band is empty
                return EMPTY_ENTITY + PATH_SEPARATOR + urlRecord;
            }
        }
        // record is unknown
        return null;
    }

}
