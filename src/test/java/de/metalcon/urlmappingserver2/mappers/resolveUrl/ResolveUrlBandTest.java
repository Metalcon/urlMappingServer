package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlTest;

public class ResolveUrlBandTest extends ResolveUrlTest {

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

}
