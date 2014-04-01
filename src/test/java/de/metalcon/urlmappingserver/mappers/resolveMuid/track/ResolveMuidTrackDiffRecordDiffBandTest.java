package de.metalcon.urlmappingserver.mappers.resolveMuid.track;

import static org.junit.Assert.assertNotSame;

import org.junit.BeforeClass;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;
import de.metalcon.urlmappingserver.mappers.resolveMuid.ResolveMuidTrackTest;

public class ResolveMuidTrackDiffRecordDiffBandTest extends
        ResolveMuidTrackTest {

    @BeforeClass
    public static void beforeClass() {
        TRACK_FACTORY.setUseSameBand(false);
        TRACK_FACTORY.setUseEmptyBand(false);
        TRACK_FACTORY.setUseSameParent(false);
        TRACK_FACTORY.setUseEmptyParent(false);
    }

    @Override
    protected void registerEntity(EntityUrlData entity) {
        super.registerEntity(entity);

        // records never equal, bands never equal
        TrackUrlData track = (TrackUrlData) entity;
        if (!RUNNING) {
            RUNNING = true;
        } else {
            assertNotSame(RECORD.getMuid(), track.getRecord().getMuid());
            assertNotSame(BAND.getMuid(), track.getRecord().getBand().getMuid());
        }
        RECORD = track.getRecord();
        BAND = RECORD.getBand();
    }

}
