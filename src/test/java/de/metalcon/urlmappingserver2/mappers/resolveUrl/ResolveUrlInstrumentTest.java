package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.InstrumentFactory;

public class ResolveUrlInstrumentTest extends ResolveUrlNamedEntityTest {

    protected static InstrumentFactory INSTRUMENT_FACTORY =
            new InstrumentFactory();

    @Override
    protected EntityFactory getFactory() {
        return INSTRUMENT_FACTORY;
    }

}
