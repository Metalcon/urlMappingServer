package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import org.junit.Test;

import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.RecordFactory;

public class ResolveMuidRecordTest extends ResolveMuidNamedEntityTest {

    protected static RecordFactory RECORD_FACTORY = new RecordFactory(
            ResolveMuidBandTest.BAND_FACTORY);

    @Test
    public void testEmptyMuid() {
        entity = RECORD_FACTORY.getRecordWoMuid();
        mapper.registerMuid(entity);
        checkForMapping(entity, RECORD_FACTORY.getMappingId(entity));
    }

    @Override
    protected EntityFactory getFactory() {
        return RECORD_FACTORY;
    }

}
