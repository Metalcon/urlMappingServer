package de.metalcon.urlmappingserver.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;

public class RecordUrlMapperTest extends EntityUrlMapperTest {

    protected static final BandUrlData VALID_BAND = new BandUrlData(
            Muid.create(MuidType.BAND), VALID_NAME);

    public static final RecordUrlData RECORD = new RecordUrlData(
            Muid.create(MuidType.RECORD), VALID_NAME, VALID_BAND, 2014);

    protected static final RecordUrlData SIMILAR_RECORD = new RecordUrlData(
            Muid.create(MuidType.RECORD), RECORD.getName(), RECORD.getBand(),
            RECORD.getReleaseYear());

    protected static final RecordUrlData RECORD_WITHOUT_BAND =
            new RecordUrlData(Muid.create(MuidType.RECORD), RECORD.getName(),
                    null, RECORD.getReleaseYear());

    protected static final RecordUrlData RECORD_WITHOUT_RELEASE_YEAR =
            new RecordUrlData(Muid.create(MuidType.RECORD), RECORD.getName(),
                    RECORD.getBand(), 0);

    protected String mappingReleaseYear;

    @BeforeClass
    public static void beforeClass() {
        ENTITY = RECORD;
        SIMILAR_ENTITY = SIMILAR_RECORD;
        EntityUrlMapperTest.beforeClass();
    }

    @Test
    public void testMappingNoBand() {
        registerMuid(RECORD_WITHOUT_BAND);
        assertEquals(
                RECORD_WITHOUT_BAND.getMuid(),
                resolveMapping(RECORD_WITHOUT_BAND,
                        generateMappingUnique(RECORD_WITHOUT_BAND)));
    }

    @Test
    public void testMappingReleaseYear() {
        mappingReleaseYear = generateMappingReleaseYear(RECORD);
        checkForEntityMapping(mappingReleaseYear);
    }

    @Test
    public void testMappingNoReleaseYear() {
        registerMuid(RECORD_WITHOUT_RELEASE_YEAR);
        assertEquals(
                RECORD_WITHOUT_RELEASE_YEAR.getMuid(),
                resolveMapping(generateMappingUnique(RECORD_WITHOUT_RELEASE_YEAR)));
        mappingReleaseYear =
                generateMappingReleaseYear(RECORD_WITHOUT_RELEASE_YEAR);
        assertNull(resolveMapping(mappingReleaseYear));
    }

    @Override
    public void testFirstNameRegistrationOnly() {
        super.testFirstNameRegistrationOnly();
        testMappingReleaseYear();
    }

    protected Muid resolveMapping(RecordUrlData record, String mapping) {
        return mapper.resolveMuid(generateUrl(record, mapping), MUID_TYPE);
    }

    @Override
    protected Map<String, String> generateUrl(String mapping) {
        return generateUrl(RECORD, mapping);
    }

    protected Map<String, String> generateUrl(
            RecordUrlData record,
            String mapping) {
        Map<String, String> pathVars = new HashMap<String, String>();

        BandUrlData band = record.getBand();
        pathVars.put(new BandUrlMapper(null).getUrlPathVarName(),
                (!band.hasEmptyMuid())
                        ? (band.getName() + WORD_SEPARATOR + band.getMuid())
                        : EMPTY_ENTITY);

        pathVars.put(mapper.getUrlPathVarName(), mapping);
        return pathVars;
    }

    protected static String generateMappingReleaseYear(RecordUrlData record) {
        return record.getReleaseYear() + WORD_SEPARATOR + record.getName();
    }

}
