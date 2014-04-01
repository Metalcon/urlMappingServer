package de.metalcon.urlmappingserver2.mappers.resolveMuid.track;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.BeforeClass;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;
import de.metalcon.urlmappingserver2.mappers.resolveMuid.ResolveMuidTrackTest;

public class ResolveMuidTrackDiffRecordSameBandTest extends
        ResolveMuidTrackTest {

    @BeforeClass
    public static void beforeClass() {
        TRACK_FACTORY.setUseSameBand(true);
        TRACK_FACTORY.setUseEmptyBand(false);
        TRACK_FACTORY.setUseSameParent(false);
        TRACK_FACTORY.setUseEmptyParent(false);
    }

    @Override
    protected void registerEntity(EntityUrlData entity) {
        super.registerEntity(entity);

        // records never equal, bands always equal
        TrackUrlData track = (TrackUrlData) entity;
        if (!RUNNING) {
            BAND = track.getRecord().getBand();
            RUNNING = true;
        } else {
            assertNotSame(RECORD.getMuid(), track.getRecord().getMuid());
            assertEquals(BAND, track.getRecord().getBand());
        }
        RECORD = track.getRecord();
    }

}
