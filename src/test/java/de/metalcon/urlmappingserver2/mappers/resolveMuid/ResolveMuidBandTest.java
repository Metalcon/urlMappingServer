package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import org.junit.Test;

import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.BandFactory;

public class ResolveMuidBandTest extends ResolveMuidNamedEntityTest {

    protected static BandFactory BAND_FACTORY = new BandFactory();

    /**
     * register band without MUID and check if accessible via ID mapping<br>
     * (using EMPTY_ENTITY)
     */
    @Test
    public void testEmptyMuid() {
        entity = BAND_FACTORY.getBandWoMuid();
        mapper.registerMuid(entity);
        checkMappingId(entity);
    }

    @Override
    protected EntityFactory getFactory() {
        return BAND_FACTORY;
    }

}
