package de.metalcon.urlmappingserver.mappers.resolveMuid.track;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;
import de.metalcon.urlmappingserver.mappers.resolveMuid.ResolveMuidTrackTest;

public class ResolveMuidTrackEmptyRecordEmptyBandTest extends
        ResolveMuidTrackTest {

    @BeforeClass
    public static void beforeClass() {
        TRACK_FACTORY.setUseSameBand(false);
        TRACK_FACTORY.setUseEmptyBand(true);
        TRACK_FACTORY.setUseSameParent(false);
        TRACK_FACTORY.setUseEmptyParent(true);
    }

    @Override
    protected void registerEntity(EntityUrlData entity) {
        super.registerEntity(entity);

        // records always empty, bands always empty
        TrackUrlData track = (TrackUrlData) entity;
        assertTrue(track.getRecord().hasEmptyMuid());
        assertTrue(track.getRecord().getBand().hasEmptyMuid());
    }

}
