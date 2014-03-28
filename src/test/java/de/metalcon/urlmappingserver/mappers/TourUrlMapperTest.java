package de.metalcon.urlmappingserver.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.TourUrlData;

public class TourUrlMapperTest extends EntityUrlMapperTest {

    protected static final int VALID_YEAR = 2014;

    protected static final TourUrlData TOUR = new TourUrlData(
            MuidFactory.generateMuid(MuidType.TOUR), VALID_NAME, VALID_YEAR);

    protected static final TourUrlData SIMILAR_TOUR = new TourUrlData(
            MuidFactory.generateMuid(MuidType.TOUR), TOUR.getName(),
            TOUR.getYear());

    protected static final TourUrlData TOUR_WITHOUT_YEAR = new TourUrlData(
            MuidFactory.generateMuid(MuidType.TOUR), TOUR.getName(), 0);

    protected String mappingYear;

    @BeforeClass
    public static void beforeClass() {
        ENTITY = TOUR;
        SIMILAR_ENTITY = SIMILAR_TOUR;
        EntityUrlMapperTest.beforeClass();
    }

    @Test
    public void testMappingYear() {
        mappingYear = generateMappingYear(TOUR);
        checkForEntityMapping(mappingYear);
    }

    @Test
    public void testMappingNoYear() {
        registerMuid(TOUR_WITHOUT_YEAR);
        assertEquals(TOUR_WITHOUT_YEAR.getMuid(),
                resolveMapping(generateMappingUnique(TOUR_WITHOUT_YEAR)));
        mappingYear = generateMappingYear(TOUR_WITHOUT_YEAR);
        assertNull(resolveMapping(mappingYear));
    }

    @Override
    public void testFirstNameRegistrationOnly() {
        super.testFirstNameRegistrationOnly();
        testMappingYear();
    }

    protected static String generateMappingYear(TourUrlData tour) {
        return tour.getYear() + WORD_SEPARATOR + tour.getName();
    }

}
