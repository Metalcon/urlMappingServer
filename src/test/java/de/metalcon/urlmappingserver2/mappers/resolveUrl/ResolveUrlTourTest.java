package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlTest;
import de.metalcon.urlmappingserver2.mappers.factories.TourFactory;

public class ResolveUrlTourTest extends ResolveUrlTest {

    protected static TourFactory TOUR_FACTORY = new TourFactory();

    /**
     * if multiple tours registered their main mapping is ID mapping
     */
    @Test
    public void testMultipleRegistered() {
        List<EntityUrlData> tours = new LinkedList<EntityUrlData>();
        for (int i = 0; i < 10; i++) {
            tours.add(TOUR_FACTORY.getEntityFull());
        }
        for (EntityUrlData tour : tours) {
            registerEntity(tour);
            checkMappingId(tour);
        }
    }

    @Override
    protected EntityFactory getFactory() {
        return TOUR_FACTORY;
    }

}
