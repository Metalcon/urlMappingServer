package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidTest;
import de.metalcon.urlmappingserver2.mappers.factories.EventFactory;

public class ResolveMuidEventTest extends ResolveMuidTest {

    protected static EventFactory EVENT_FACTORY = new EventFactory();

    @Override
    protected EntityFactory getFactory() {
        return EVENT_FACTORY;
    }

}
