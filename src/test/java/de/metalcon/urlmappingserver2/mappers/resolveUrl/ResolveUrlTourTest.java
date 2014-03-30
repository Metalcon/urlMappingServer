package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TourUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlTest;

public class ResolveUrlTourTest extends ResolveUrlTest {

    /**
     * if multiple tours registered their main mapping is ID mapping
     */
    @Test
    public void testMultipleRegistered() {
        List<EntityUrlData> tours = new LinkedList<EntityUrlData>();
        for (int i = 0; i < 10; i++) {
            tours.add(getEntityFull());
        }
        for (EntityUrlData tour : tours) {
            registerEntity(tour);
            checkMappingId(tour);
        }
    }

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
