package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import org.junit.Test;

import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.BandFactory;

public class ResolveUrlBandTest extends ResolveUrlNamedEntityTest {

    protected static BandFactory BAND_FACTORY = new BandFactory();

    @Test
    public void testEmptyMuid() {
        entity = BAND_FACTORY.getBandWoMuid();
        mapper.registerMuid(entity);
        checkMainMapping(entity, BAND_FACTORY.getMappingId(entity));
    }

    @Override
    protected EntityFactory getFactory() {
        return BAND_FACTORY;
    }

}
