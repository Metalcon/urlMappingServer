package de.metalcon.urlmappingserver.mappers.resolveUrl;

import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveUrlNamedEntityTest;
import de.metalcon.urlmappingserver.mappers.factories.RecordFactory;

public abstract class ResolveUrlRecordTest extends ResolveUrlNamedEntityTest {

    protected static RecordFactory RECORD_FACTORY = new RecordFactory(
            ResolveUrlBandTest.BAND_FACTORY);

    protected RecordUrlData record;

    /**
     * register record so that main mapping is release year mapping and check
     * this
     */
    @Test
    public void testShortestMappingReleaseYear() {
        record = (RecordUrlData) RECORD_FACTORY.getEntityFull();
        registerEntity(record);

        RecordUrlData recordWithSameName =
                RECORD_FACTORY.getRecordSameName(record);
        registerEntity(recordWithSameName);

        checkMappingName(record);
        checkMappingReleaseYear(recordWithSameName);
    }

    /**
     * register record so that main mapping would be release year mapping but is
     * ID mapping as no release year for record set and check this
     */
    @Test
    public void testShortestMappingReleaseYearWoReleaseYear() {
        record = (RecordUrlData) RECORD_FACTORY.getEntityFull();
        registerEntity(record);

        RecordUrlData recordSameNameWoReleaseYear =
                RECORD_FACTORY.getRecordWoReleaseYear(record);
        registerEntity(recordSameNameWoReleaseYear);

        checkMappingName(record);
        checkMappingId(recordSameNameWoReleaseYear);
    }

    @Test
    public void testEmptyMuid() {
        entity = RECORD_FACTORY.getRecordWoMuid();
        mapper.registerMuid(entity);
        checkMappingId(entity);
    }

    @Override
    protected EntityFactory getFactory() {
        return RECORD_FACTORY;
    }

    /**
     * check if main mapping for record is release year mapping
     */
    protected void checkMappingReleaseYear(RecordUrlData record) {
        checkMainMapping(record, RECORD_FACTORY.getMappingReleaseYear(record));
    }

    @Override
    protected String resolveUrl(EntityUrlData entity) {
        String url = super.resolveUrl(entity);
        System.out.println(url);
        return url;
    }

}
