package de.metalcon.urlmappingserver.mappers.resolveMuid;

import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveMuidTest;
import de.metalcon.urlmappingserver.mappers.factories.EventFactory;

public class ResolveMuidEventTest extends ResolveMuidTest {

    protected static EventFactory EVENT_FACTORY = new EventFactory();

    @Override
    protected EntityFactory getFactory() {
        return EVENT_FACTORY;
    }

}
