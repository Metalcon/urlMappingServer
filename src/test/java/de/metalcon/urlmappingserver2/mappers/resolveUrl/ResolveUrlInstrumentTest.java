package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.InstrumentUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlNamedEntityTest;

public class ResolveUrlInstrumentTest extends ResolveUrlNamedEntityTest {

    @Override
    protected MuidType getInstanceMuidType() {
        return getMuidType();
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getInstrument();
    }

    protected static MuidType getMuidType() {
        return MuidType.INSTRUMENT;
    }

    public static InstrumentUrlData getInstrument() {
        return new InstrumentUrlData(MuidFactory.generateMuid(getMuidType()),
                VALID_NAME);
    }

}
