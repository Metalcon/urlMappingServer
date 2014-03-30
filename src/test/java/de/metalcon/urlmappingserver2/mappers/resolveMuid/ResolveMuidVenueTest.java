package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import org.junit.Test;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.CityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.VenueUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;

public class ResolveMuidVenueTest extends ResolveMuidNamedEntityTest {

    protected VenueUrlData venue;

    /**
     * register venue and check if accessible via city name mapping
     */
    @Test
    public void testMappingCityName() {
        venue = getVenue();
        registerEntity(venue);
        checkMappingCityName(venue);
    }

    /**
     * register venue without a city and check if still accessible via all other
     * mappings
     */
    @Test
    public void testMappingWoCityName() {
        venue = getVenueWoCity();
        registerEntity(venue);

        checkMappingId(venue);
        checkMappingName(venue);
    }

    @Override
    protected MuidType getInstanceMuidType() {
        return getMuidType();
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getVenue();
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
        checkForMapping(venue, getMappingCityName(venue));
    }

    protected String getMappingCityName(VenueUrlData venue) {
        return getMappingName(venue) + WORD_SEPARATOR
                + getMappingName(venue.getCity());
    }

    public static VenueUrlData getVenue() {
        return new VenueUrlData(MuidFactory.generateMuid(getMuidType()),
                VALID_NAME, getCity());
    }

    protected static VenueUrlData getVenueWoCity() {
        return new VenueUrlData(MuidFactory.generateMuid(getMuidType()),
                VALID_NAME, null);
    }

    protected static MuidType getMuidType() {
        return MuidType.VENUE;
    }

    protected static CityUrlData getCity() {
        return ResolveMuidCityTest.getCity();
    }

}