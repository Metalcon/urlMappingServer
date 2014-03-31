package de.metalcon.urlmappingserver2.mappers.resolveMuid.track;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;
import de.metalcon.urlmappingserver2.mappers.resolveMuid.ResolveMuidTrackTest;

public class ResolveMuidTrackDiffRecordEmptyBandTest extends
        ResolveMuidTrackTest {

    @BeforeClass
    public static void beforeClass() {
        TRACK_FACTORY.setUseSameBand(false);
        TRACK_FACTORY.setUseEmptyBand(true);
        TRACK_FACTORY.setUseSameParent(false);
        TRACK_FACTORY.setUseEmptyParent(false);
    }

    @Override
    protected void registerEntity(EntityUrlData entity) {
        super.registerEntity(entity);

        // records never equal, bands always empty
        TrackUrlData track = (TrackUrlData) entity;
        if (!RUNNING) {
            RUNNING = true;
        } else {
            assertNotSame(RECORD.getMuid(), track.getRecord().getMuid());
            assertTrue(track.getRecord().getBand().hasEmptyMuid());
        }
        RECORD = track.getRecord();
    }

}
