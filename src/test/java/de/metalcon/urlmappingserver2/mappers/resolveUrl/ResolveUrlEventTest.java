package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlTest;
import de.metalcon.urlmappingserver2.mappers.factories.EventFactory;

public class ResolveUrlEventTest extends ResolveUrlTest {

    protected static EventFactory EVENT_FACTORY = new EventFactory();

    /**
     * if multiple events registered their main mapping is ID mapping
     */
    @Test
    public void testMultipleRegistered() {
        List<EntityUrlData> events = new LinkedList<EntityUrlData>();
        for (int i = 0; i < 10; i++) {
            events.add(EVENT_FACTORY.getEntityFull());
        }
        for (EntityUrlData event : events) {
            registerEntity(event);
            checkMappingId(event);
        }
    }

    @Override
    protected EntityFactory getFactory() {
        return EVENT_FACTORY;
    }

}
