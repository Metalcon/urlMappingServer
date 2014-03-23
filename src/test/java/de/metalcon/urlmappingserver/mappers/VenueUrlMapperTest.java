package de.metalcon.urlmappingserver.mappers;

import org.junit.BeforeClass;
import org.junit.Test;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.VenueUrlData;

public class VenueUrlMapperTest extends EntityUrlMapperTest {

    public static final VenueUrlData VENUE = new VenueUrlData(
            Muid.create(MuidType.VENUE), VALID_NAME, CityUrlMapperTest.CITY);

    protected static final VenueUrlData SIMILAR_VENUE = new VenueUrlData(
            Muid.create(MuidType.VENUE), VENUE.getName(), VENUE.getCity());

    protected static final VenueUrlData VENUE_WITHOUT_CITY = new VenueUrlData(
            Muid.create(MuidType.VENUE), VENUE.getName(), null);

    protected String mappingCityName;

    @BeforeClass
    public static void beforeClass() {
        ENTITY = VENUE;
        SIMILAR_ENTITY = SIMILAR_VENUE;
        EntityUrlMapperTest.beforeClass();
    }

    @Test
    public void testMappingCityName() {
        mappingCityName = generateMappingCityName(VENUE);
        checkForEntityMapping(mappingCityName);
    }

    @Override
    public void testFirstNameRegistrationOnly() {
        super.testFirstNameRegistrationOnly();
        testMappingCityName();
    }

    protected static String generateMappingCityName(VenueUrlData venue) {
        return venue.getName() + VenueUrlMapper.WORD_SEPARATOR
                + venue.getCity().getName();
    }

}
