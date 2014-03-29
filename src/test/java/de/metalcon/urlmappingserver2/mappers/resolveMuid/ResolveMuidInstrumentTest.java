package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.InstrumentUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidTest;

public class ResolveMuidInstrumentTest extends ResolveMuidTest {

    @Override
    protected MuidType getMuidType() {
        return MuidType.INSTRUMENT;
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getInstrument();
    }

    public static InstrumentUrlData getInstrument() {
        return new InstrumentUrlData(MuidFactory.generateMuid(TYPE), VALID_NAME);
    }

}
