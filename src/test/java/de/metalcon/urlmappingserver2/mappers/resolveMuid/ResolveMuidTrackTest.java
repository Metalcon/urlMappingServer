package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.TrackFactory;

public abstract class ResolveMuidTrackTest extends ResolveMuidNamedEntityTest {

    protected static TrackFactory TRACK_FACTORY = new TrackFactory(
            ResolveMuidBandTest.BAND_FACTORY,
            ResolveMuidRecordTest.RECORD_FACTORY);

    protected static boolean RUNNING = false;

    protected static BandUrlData BAND;

    protected static RecordUrlData RECORD;

    protected TrackUrlData track;

    /**
     * register track and check if accessible via track number mapping
     */
    @Test
    public void testMappingTrackNumber() {
        track = (TrackUrlData) TRACK_FACTORY.getEntityFull();
        registerEntity(track);

        checkMappingTrackNumber(track);
    }

    /**
     * register track without a track number and check if still accessible via
     * all other mappings
     */
    @Test
    public void testMappingTrackNameWoTrackName() {
        entity = TRACK_FACTORY.getEntityFull();
        registerEntity(entity);

        checkMappingId(entity);
        checkMappingName(entity);
    }

    @Override
    public void testNotRegistered() {
        super.testNotRegistered();
        assertNull(resolveMuid(entity,
                TRACK_FACTORY.getMappingTrackNumber((TrackUrlData) entity)));
    }

    @Override
    protected EntityFactory getFactory() {
        return TRACK_FACTORY;
    }

    @Override
    protected void checkAllMappings(EntityUrlData entity) {
        super.checkAllMappings(entity);
        checkMappingTrackNumber((TrackUrlData) entity);
    }

    /**
     * check if a track is accessible via its track number mapping
     */
    protected void checkMappingTrackNumber(TrackUrlData track) {
        checkForMapping(track, TRACK_FACTORY.getMappingTrackNumber(track));
    }

    @After
    public void tearDown() {
        RUNNING = false;
    }

}
