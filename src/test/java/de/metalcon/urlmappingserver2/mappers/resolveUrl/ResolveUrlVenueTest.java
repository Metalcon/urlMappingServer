package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.VenueUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.VenueFactory;

public class ResolveUrlVenueTest extends ResolveUrlNamedEntityTest {

    protected static VenueFactory VENUE_FACTORY = new VenueFactory(
            ResolveUrlCityTest.CITY_FACTORY);

    /**
     * register venue so that main mapping is city name mapping and check this
     */
    @Test
    public void testShortestMappingCityName() {
        VenueUrlData venue = (VenueUrlData) VENUE_FACTORY.getEntityFull();
        registerEntity(venue);

        VenueUrlData venueSameName = VENUE_FACTORY.getVenueSameName(venue);
        registerEntity(venueSameName);

        checkMappingName(venue);
        checkMappingCityName(venueSameName);
    }

    /**
     * register venue so that main mapping would be city name mapping but is ID
     * mapping as no city for venue set and check this
     */
    @Test
    public void testShortestMappingCityNameWoCity() {
        VenueUrlData venue = (VenueUrlData) VENUE_FACTORY.getEntityFull();
        registerEntity(venue);

        VenueUrlData venueSameNameWoCity = VENUE_FACTORY.getVenueWoCity(venue);
        registerEntity(venueSameNameWoCity);

        checkMappingName(venue);
        checkMappingId(venueSameNameWoCity);
    }

    @Override
    protected EntityFactory getFactory() {
        return VENUE_FACTORY;
    }

    /**
     * check if main mapping for venue is city name mapping
     */
    protected void checkMappingCityName(VenueUrlData venue) {
        checkMainMapping(venue, VENUE_FACTORY.getMappingCityName(venue));
    }

}
