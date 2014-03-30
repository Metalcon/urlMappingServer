package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EventUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidTest;

public class ResolveMuidEventTest extends ResolveMuidTest {

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
