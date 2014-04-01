package de.metalcon.urlmappingserver.mappers.resolveUrl;

import org.junit.Test;

import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveUrlNamedEntityTest;
import de.metalcon.urlmappingserver.mappers.factories.BandFactory;

public class ResolveUrlBandTest extends ResolveUrlNamedEntityTest {

    protected static BandFactory BAND_FACTORY = new BandFactory();

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
