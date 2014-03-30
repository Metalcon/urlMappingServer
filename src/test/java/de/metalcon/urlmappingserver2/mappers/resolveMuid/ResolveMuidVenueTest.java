package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.VenueUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.VenueFactory;

public class ResolveMuidVenueTest extends ResolveMuidNamedEntityTest {

    protected static VenueFactory VENUE_FACTORY = new VenueFactory(
            ResolveMuidCityTest.CITY_FACTORY);

    protected VenueUrlData venue;

    /**
     * register venue and check if accessible via city name mapping
     */
    @Test
    public void testMappingCityName() {
        venue = (VenueUrlData) VENUE_FACTORY.getEntityFull();
        registerEntity(venue);
        checkMappingCityName(venue);
    }

    /**
     * register venue without a city and check if still accessible via all other
     * mappings
     */
    @Test
    public void testMappingWoCityName() {
        venue = VENUE_FACTORY.getVenueWoCity();
        registerEntity(venue);

        checkMappingId(venue);
        checkMappingName(venue);
    }

    @Override
    public void testNotRegistered() {
        super.testNotRegistered();
        assertNull(resolveMuid(entity,
                VENUE_FACTORY.getMappingCityName((VenueUrlData) entity)));
    }

    @Override
    protected EntityFactory getFactory() {
        return VENUE_FACTORY;
    }

    @Override
    protected void checkAllMappings(EntityUrlData entity) {
        super.checkAllMappings(entity);
        checkMappingCityName((VenueUrlData) entity);
    }

    /**
     * check if a venue is accessible via its city name mapping
     */
    protected void checkMappingCityName(VenueUrlData venue) {
        checkForMapping(venue, VENUE_FACTORY.getMappingCityName(venue));
    }

}
