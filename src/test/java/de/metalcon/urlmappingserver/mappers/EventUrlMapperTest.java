package de.metalcon.urlmappingserver.mappers;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.CityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EventUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.VenueUrlData;

public class EventUrlMapperTest extends EntityUrlMapperTest {

    protected static final SimpleDateFormat DATE_FORMATTER =
            new SimpleDateFormat("yyyy-MM-dd");

    protected static final long CRR_MS = System.currentTimeMillis();

    protected static Date VALID_DATE = new Date(CRR_MS - CRR_MS % 1000);

    protected static CityUrlData VALID_CITY = new CityUrlData(
            Muid.create(MuidType.CITY), VALID_NAME);

    protected static VenueUrlData VALID_VENUE = new VenueUrlData(
            Muid.create(MuidType.VENUE), VALID_NAME, new CityUrlData(
                    Muid.create(MuidType.CITY), VALID_NAME));

    public static final EventUrlData EVENT = new EventUrlData(
            Muid.create(MuidType.EVENT), VALID_NAME, VALID_DATE, VALID_CITY,
            VALID_VENUE);

    protected static final EventUrlData SIMILAR_EVENT = new EventUrlData(
            Muid.create(MuidType.EVENT), EVENT.getName(), EVENT.getDate(),
            EVENT.getCity(), EVENT.getVenue());

    protected static final EventUrlData EVENT_WITHOUT_DATE = new EventUrlData(
            Muid.create(MuidType.EVENT), EVENT.getName(), null,
            EVENT.getCity(), EVENT.getVenue());

    protected static final EventUrlData EVENT_WITHOUT_CITY = new EventUrlData(
            Muid.create(MuidType.EVENT), EVENT.getName(), EVENT.getDate(),
            null, EVENT.getVenue());

    protected static final EventUrlData EVENT_WITHOUT_VENUE = new EventUrlData(
            Muid.create(MuidType.EVENT), EVENT.getName(), EVENT.getDate(),
            EVENT.getCity(), null);

    protected String mappingDate;

    protected String mappingCityName;

    protected String mappingVenueName;

    @BeforeClass
    public static void beforeClass() {
        ENTITY = EVENT;
        SIMILAR_ENTITY = SIMILAR_EVENT;
        EntityUrlMapperTest.beforeClass();
    }

    @Test
    public void testMappingDate() {
        mappingDate = generateMappingDate(EVENT);
        checkForEntityMapping(mappingDate);
    }

    @Test
    public void testRegistrationNoDate() {
        mapper.registerMuid(EVENT_WITHOUT_DATE);
        assertEquals(EVENT_WITHOUT_DATE.getMuid(),
                resolveMapping(generateMappingUnique(EVENT_WITHOUT_DATE)));
    }

    @Test
    public void testMappingCityName() {
        mappingCityName = generateMappingCityName(EVENT);
        checkForEntityMapping(mappingCityName);
    }

    @Test
    public void testRegistrationNoCity() {
        mapper.registerMuid(EVENT_WITHOUT_CITY);
        assertEquals(EVENT_WITHOUT_CITY.getMuid(),
                resolveMapping(generateMappingUnique(EVENT_WITHOUT_CITY)));
    }

    @Test
    public void testMappingVenueName() {
        mappingVenueName = generateMappingVenueName(EVENT);
        checkForEntityMapping(mappingVenueName);
    }

    @Test
    public void testRegistrationNoVenue() {
        mapper.registerMuid(EVENT_WITHOUT_VENUE);
        assertEquals(EVENT_WITHOUT_VENUE.getMuid(),
                resolveMapping(generateMappingUnique(EVENT_WITHOUT_VENUE)));
    }

    @Override
    public void testFirstNameRegistrationOnly() {
        super.testFirstNameRegistrationOnly();
        testMappingDate();
        testMappingCityName();
        testMappingVenueName();
    }

    protected static String generateMappingDate(EventUrlData event) {
        return DATE_FORMATTER.format(event.getDate())
                + EventUrlMapper.WORD_SEPARATOR + event.getName();
    }

    protected static String generateMappingCityName(EventUrlData event) {
        return event.getName() + EventUrlMapper.WORD_SEPARATOR
                + event.getCity().getName();
    }

    protected static String generateMappingVenueName(EventUrlData event) {
        return event.getName() + EventUrlMapper.WORD_SEPARATOR
                + event.getVenue().getName();
    }

}
