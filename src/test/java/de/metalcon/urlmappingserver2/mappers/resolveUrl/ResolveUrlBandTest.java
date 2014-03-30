package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import org.junit.Test;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlNamedEntityTest;

public class ResolveUrlBandTest extends ResolveUrlNamedEntityTest {

    @Test
    public void testEmptyMuid() {
        entity = getBandWoMuid();
        mapper.registerMuid(entity);
        checkMainMapping(entity, EMPTY_ENTITY);
    }

    @Override
    protected MuidType getInstanceMuidType() {
        return getMuidType();
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getBand();
    }

    protected static MuidType getMuidType() {
        return MuidType.BAND;
    }

    public static BandUrlData getBand() {
        return new BandUrlData(MuidFactory.generateMuid(getMuidType()),
                VALID_NAME);
    }

    protected static BandUrlData getBandWoMuid() {
        return new BandUrlData();
    }

}
