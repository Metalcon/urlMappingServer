package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TourUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidTest;

public class ResolveMuidTourTest extends ResolveMuidTest {

    @Override
    protected MuidType getMuidType() {
        return MuidType.TOUR;
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getTour();
    }

    @Override
    protected String getMappingId(EntityUrlData entity) {
        // the only tour mapping: MUID
        return entity.getMuid().toString();
    }

    public static TourUrlData getTour() {
        return new TourUrlData(MuidFactory.generateMuid(TYPE));
    }

}
