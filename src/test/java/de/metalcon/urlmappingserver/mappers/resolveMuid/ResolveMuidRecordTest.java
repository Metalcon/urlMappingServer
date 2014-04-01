package de.metalcon.urlmappingserver.mappers.resolveMuid;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver.mappers.factories.RecordFactory;

public abstract class ResolveMuidRecordTest extends ResolveMuidNamedEntityTest {

    protected static RecordFactory RECORD_FACTORY = new RecordFactory(
            ResolveMuidBandTest.BAND_FACTORY);

    protected RecordUrlData record;

    /**
     * register record and check if accessible via release year mapping
     */
    @Test
    public void testMappingReleaseYear() {
        record = (RecordUrlData) RECORD_FACTORY.getEntityFull();
        registerEntity(record);
        checkMappingReleaseYear(record);
    }

    /**
     * register record without a release year and check if still accessible via
     * all other mappings
     */
    @Test
    public void testMappingWoReleaseYear() {
        record = RECORD_FACTORY.getRecordWoReleaseYear();
        registerEntity(record);

        checkMappingId(record);
        checkMappingName(record);
    }

    /**
     * register record without MUID and check if accessible via ID mapping<br>
     * (using EMPTY_ENTITY)
     */
    @Test
    public void testEmptyMuid() {
        entity = RECORD_FACTORY.getRecordWoMuid();
        mapper.registerMuid(entity);
        checkForMapping(entity, RECORD_FACTORY.getMappingId(entity));
    }

    @Override
    public void testNotRegistered() {
        super.testNotRegistered();
        assertNull(resolveMuid(entity,
                RECORD_FACTORY.getMappingReleaseYear((RecordUrlData) entity)));
    }

    @Override
    protected EntityFactory getFactory() {
        return RECORD_FACTORY;
    }

    @Override
    protected void checkAllMappings(EntityUrlData entity) {
        super.checkAllMappings(entity);
        checkMappingReleaseYear((RecordUrlData) entity);
    }

    /**
     * check if a record is accessible via its release year mapping
     */
    protected void checkMappingReleaseYear(RecordUrlData record) {
        checkForMapping(record, RECORD_FACTORY.getMappingReleaseYear(record));
    }

}
