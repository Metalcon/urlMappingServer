package de.metalcon.urlmappingserver.mappers.resolveMuid;

import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver.mappers.factories.InstrumentFactory;

public class ResolveMuidInstrumentTest extends ResolveMuidNamedEntityTest {

    protected static InstrumentFactory INSTRUMENT_FACTORY =
            new InstrumentFactory();

    @Override
    protected EntityFactory getFactory() {
        return INSTRUMENT_FACTORY;
    }

}
