package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.InstrumentUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;

public class ResolveMuidInstrumentTest extends ResolveMuidNamedEntityTest {

    @Override
    protected MuidType getInstanceMuidType() {
        return getMuidType();
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getInstrument();
    }

    @Override
    protected EntityUrlData getIdentical(EntityUrlData entity) {
        return new InstrumentUrlData(MuidFactory.generateMuid(getMuidType()),
                entity.getName());
    }

    protected static MuidType getMuidType() {
        return MuidType.INSTRUMENT;
    }

    public static InstrumentUrlData getInstrument() {
        return new InstrumentUrlData(MuidFactory.generateMuid(getMuidType()),
                "instrument" + CRR_ENTITY_ID++);
    }

}
