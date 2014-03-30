package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EventUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlTest;

public class ResolveUrlEventTest extends ResolveUrlTest {

    /**
     * if multiple events registered their main mapping is ID mapping
     */
    @Test
    public void testMultipleRegistered() {
        List<EntityUrlData> events = new LinkedList<EntityUrlData>();
        for (int i = 0; i < 10; i++) {
            events.add(getEntityFull());
        }
        for (EntityUrlData event : events) {
            registerEntity(event);
            checkMappingId(event);
        }
    }

    @Override
    protected MuidType getInstanceMuidType() {
        return getMuidType();
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getEvent();
    }

    @Override
    protected String getMappingId(EntityUrlData entity) {
        // the only event mapping: MUID
        return entity.getMuid().toString();
    }

    protected static MuidType getMuidType() {
        return MuidType.EVENT;
    }

    public static EventUrlData getEvent() {
        return new EventUrlData(MuidFactory.generateMuid(getMuidType()));
    }

}
