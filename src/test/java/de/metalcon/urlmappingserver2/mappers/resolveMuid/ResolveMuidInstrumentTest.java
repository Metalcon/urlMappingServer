package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.InstrumentFactory;

public class ResolveMuidInstrumentTest extends ResolveMuidNamedEntityTest {

    protected static InstrumentFactory INSTRUMENT_FACTORY =
            new InstrumentFactory();

    @Override
    protected EntityFactory getFactory() {
        return INSTRUMENT_FACTORY;
    }

}
