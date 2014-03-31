package de.metalcon.urlmappingserver2.mappers.resolveMuid.track;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;
import de.metalcon.urlmappingserver2.mappers.resolveMuid.ResolveMuidTrackTest;

public class ResolveMuidTrackEmptyRecordSameBandTest extends
        ResolveMuidTrackTest {

    @BeforeClass
    public static void beforeClass() {
        TRACK_FACTORY.setUseSameBand(true);
        TRACK_FACTORY.setUseEmptyBand(false);
        TRACK_FACTORY.setUseSameParent(false);
        TRACK_FACTORY.setUseEmptyParent(true);
    }

    @Override
    protected void registerEntity(EntityUrlData entity) {
        super.registerEntity(entity);

        // records always empty, bands always equal
        TrackUrlData track = (TrackUrlData) entity;
        if (!RUNNING) {
            RUNNING = true;
            BAND = track.getRecord().getBand();
        } else {
            assertTrue(track.getRecord().hasEmptyMuid());
            assertEquals(BAND, track.getRecord().getBand());
        }
    }

}
