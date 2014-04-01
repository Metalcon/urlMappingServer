package de.metalcon.urlmappingserver.mappers.resolveUrl;

import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveUrlNamedEntityTest;
import de.metalcon.urlmappingserver.mappers.factories.InstrumentFactory;

public class ResolveUrlInstrumentTest extends ResolveUrlNamedEntityTest {

    protected static InstrumentFactory INSTRUMENT_FACTORY =
            new InstrumentFactory();

    @Override
    protected EntityFactory getFactory() {
        return INSTRUMENT_FACTORY;
    }

}
