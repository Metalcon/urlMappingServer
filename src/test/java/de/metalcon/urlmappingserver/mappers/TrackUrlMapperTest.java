package de.metalcon.urlmappingserver.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;

public class TrackUrlMapperTest extends EntityUrlMapperTest {

    protected static final BandUrlData VALID_BAND = new BandUrlData(
            MuidFactory.generateMuid(MuidType.BAND), VALID_NAME);

    protected static final RecordUrlData VALID_RECORD = new RecordUrlData(
            MuidFactory.generateMuid(MuidType.RECORD), VALID_NAME,
            new BandUrlData(MuidFactory.generateMuid(MuidType.BAND),
                    VALID_BAND.getName()), 2014);

    protected static final RecordUrlData RECORD_WITHOUT_BAND =
            new RecordUrlData(MuidFactory.generateMuid(MuidType.RECORD),
                    VALID_NAME, null, 2014);

    protected static final TrackUrlData TRACK = new TrackUrlData(
            MuidFactory.generateMuid(MuidType.TRACK), VALID_NAME, null,
            VALID_RECORD, 2014);

    protected static final TrackUrlData SIMILAR_TRACK = new TrackUrlData(
            MuidFactory.generateMuid(MuidType.TRACK), TRACK.getName(), null,
            TRACK.getRecord(), TRACK.getTrackNumber());

    protected static final TrackUrlData TRACK_WITHOUT_BAND = new TrackUrlData(
            MuidFactory.generateMuid(MuidType.TRACK), TRACK.getName(), null,
            RECORD_WITHOUT_BAND, TRACK.getTrackNumber());

    protected static final TrackUrlData TRACK_WITHOUT_BAND_AND_RECORD =
            new TrackUrlData(MuidFactory.generateMuid(MuidType.TRACK),
                    TRACK.getName(), null, null, TRACK.getTrackNumber());

    protected static final TrackUrlData TRACK_WITHOUT_RECORD =
            new TrackUrlData(MuidFactory.generateMuid(MuidType.TRACK),
                    TRACK.getName(), TRACK.getRecord().getBand(), null,
                    TRACK.getTrackNumber());

    protected static final TrackUrlData TRACK_WITHOUT_TRACK_NUMBER =
            new TrackUrlData(MuidFactory.generateMuid(MuidType.TRACK),
                    TRACK.getName(), null, TRACK.getRecord(), 0);

    protected static final EntityUrlMappingManager MANAGER =
            new EntityUrlMappingManager();

    protected static final String PATH_BAND = MANAGER.getMapper(MuidType.BAND)
            .getUrlPathVarName();

    protected static final String PATH_RECORD = MANAGER.getMapper(
            MuidType.RECORD).getUrlPathVarName();

    protected String mappingTrackNumber;

    protected String mappingTrackNumberTrackName;

    @BeforeClass
    public static void beforeClass() {
        ENTITY = TRACK;
        SIMILAR_ENTITY = SIMILAR_TRACK;
        EntityUrlMapperTest.beforeClass();
    }

    @Test
    public void testMappingNoBand() {
        registerMuid(TRACK_WITHOUT_BAND);
        assertEquals(
                TRACK_WITHOUT_BAND.getMuid(),
                resolveMapping(TRACK_WITHOUT_BAND,
                        generateMappingUnique(TRACK_WITHOUT_BAND)));
        assertNotNull(manager.resolveMuid(
                generateUrl(TRACK_WITHOUT_BAND,
                        generateMappingUnique(TRACK_WITHOUT_BAND)),
                MuidType.BAND));
        assertNull(manager.resolveMuid(
                generateUrl(TRACK_WITHOUT_RECORD,
                        generateMappingUnique(TRACK_WITHOUT_RECORD)),
                MuidType.RECORD));
    }

    @Test
    public void testMappingNoRecord() {
        registerMuid(TRACK_WITHOUT_RECORD);
        assertEquals(
                TRACK_WITHOUT_RECORD.getMuid(),
                resolveMapping(TRACK_WITHOUT_RECORD,
                        generateMappingUnique(TRACK_WITHOUT_RECORD)));
        assertNull(manager.resolveMuid(
                generateUrl(TRACK_WITHOUT_BAND,
                        generateMappingUnique(TRACK_WITHOUT_BAND)),
                MuidType.BAND));
        assertNotNull(manager.resolveMuid(
                generateUrl(TRACK_WITHOUT_RECORD,
                        generateMappingUnique(TRACK_WITHOUT_RECORD)),
                MuidType.RECORD));
    }

    @Test
    public void testMappingNoBandNoRecord() {
        registerMuid(TRACK_WITHOUT_BAND_AND_RECORD);
        assertEquals(
                TRACK_WITHOUT_BAND_AND_RECORD.getMuid(),
                resolveMapping(TRACK_WITHOUT_BAND_AND_RECORD,
                        generateMappingUnique(TRACK_WITHOUT_BAND_AND_RECORD)));
        assertNotNull(manager
                .resolveMuid(generateUrl(TRACK_WITHOUT_BAND_AND_RECORD, null),
                        MuidType.BAND));
    }

    @Test
    public void testMappingTrackNumber() {
        mappingTrackNumber = generateMappingTrackNumber(TRACK);
        checkForEntityMapping(mappingTrackNumber);
    }

    @Test
    public void testMappingNoTrackNumber() {
        registerMuid(TRACK_WITHOUT_TRACK_NUMBER);
        assertEquals(
                TRACK_WITHOUT_TRACK_NUMBER.getMuid(),
                resolveMapping(generateMappingUnique(TRACK_WITHOUT_TRACK_NUMBER)));
        mappingTrackNumber =
                generateMappingTrackNumber(TRACK_WITHOUT_TRACK_NUMBER);
        assertNull(resolveMapping(mappingTrackNumber));
    }

    @Test
    public void testMappingTrackNumberTrackName() {
        mappingTrackNumberTrackName =
                generateMappingTrackNumberTrackName(TRACK);
        checkForEntityMapping(mappingTrackNumberTrackName);
    }

    @Test
    public void testMappingNoTrackNumberTrackName() {
        registerMuid(TRACK_WITHOUT_TRACK_NUMBER);
        assertEquals(
                TRACK_WITHOUT_TRACK_NUMBER.getMuid(),
                resolveMapping(generateMappingUnique(TRACK_WITHOUT_TRACK_NUMBER)));
        mappingTrackNumberTrackName =
                generateMappingTrackNumberTrackName(TRACK_WITHOUT_TRACK_NUMBER);
        assertNull(resolveMapping(TRACK_WITHOUT_TRACK_NUMBER,
                mappingTrackNumberTrackName));
    }

    @Override
    public void testFirstNameRegistrationOnly() {
        super.testFirstNameRegistrationOnly();
        testMappingTrackNumber();
    }

    protected Muid resolveMapping(TrackUrlData track, String mapping) {
        return mapper.resolveMuid(generateUrl(track, mapping), MUID_TYPE);
    }

    @Override
    protected Map<String, String> generateUrl(String mapping) {
        return generateUrl(TRACK, mapping);
    }

    protected Map<String, String>
        generateUrl(TrackUrlData track, String mapping) {
        Map<String, String> pathVars = new HashMap<String, String>();

        BandUrlData band = track.getRecord().getBand();
        pathVars.put(PATH_BAND, (!band.hasEmptyMuid()) ? (band.getName()
                + WORD_SEPARATOR + band.getMuid()) : EMPTY_ENTITY);

        RecordUrlData record = track.getRecord();
        pathVars.put(PATH_RECORD, (!record.hasEmptyMuid()) ? (record.getName()
                + WORD_SEPARATOR + record.getMuid()) : EMPTY_ENTITY);

        pathVars.put(mapper.getUrlPathVarName(), mapping);
        return pathVars;
    }

    protected static String generateMappingTrackNumber(TrackUrlData track) {
        return String.valueOf(track.getTrackNumber());
    }

    protected static String generateMappingTrackNumberTrackName(
            TrackUrlData track) {
        return track.getTrackNumber() + WORD_SEPARATOR + track.getName();
    }

}
