package de.metalcon.urlmappingserver2.mappers.resolveMuid.track;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;
import de.metalcon.urlmappingserver2.mappers.resolveMuid.ResolveMuidTrackTest;

public class ResolveMuidTrackEmptyRecordDiffBandTest extends
        ResolveMuidTrackTest {

    @BeforeClass
    public static void beforeClass() {
        TRACK_FACTORY.setUseSameBand(false);
        TRACK_FACTORY.setUseEmptyBand(false);
        TRACK_FACTORY.setUseSameParent(false);
        TRACK_FACTORY.setUseEmptyParent(true);
    }

    @Override
    protected void registerEntity(EntityUrlData entity) {
        super.registerEntity(entity);

        // records always empty, bands never equal
        TrackUrlData track = (TrackUrlData) entity;
        if (!RUNNING) {
            RUNNING = true;
        } else {
            assertNotSame(BAND.getMuid(), track.getRecord().getBand().getMuid());
        }
        assertTrue(track.getRecord().hasEmptyMuid());
        BAND = track.getRecord().getBand();
    }

}
