package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import org.junit.Test;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;

public class ResolveMuidBandTest extends ResolveMuidNamedEntityTest {

    @Test
    public void testEmptyMuid() {
        entity = getBandWoMuid();
        mapper.registerMuid(entity);
        checkForMapping(entity, EMPTY_ENTITY);
    }

    @Override
    protected MuidType getMuidType() {
        return MuidType.BAND;
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getBand();
    }

    public static BandUrlData getBand() {
        return new BandUrlData(MuidFactory.generateMuid(TYPE), VALID_NAME);
    }

    protected static BandUrlData getBandWoMuid() {
        return new BandUrlData();
    }

}
