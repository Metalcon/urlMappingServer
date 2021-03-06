package de.metalcon.urlmappingserver.mappers.resolveUrl.track;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;
import de.metalcon.urlmappingserver.mappers.resolveUrl.ResolveUrlTrackTest;

public class ResolveUrlTrackSameRecordTest extends ResolveUrlTrackTest {

    protected static RecordUrlData RECORD;

    @BeforeClass
    public static void beforeClass() {
        TRACK_FACTORY.setUseSameBand(false);
        TRACK_FACTORY.setUseEmptyBand(false);
        TRACK_FACTORY.setUseSameParent(true);
        TRACK_FACTORY.setUseEmptyParent(false);
    }

    @Override
    protected void registerEntity(EntityUrlData entity) {
        super.registerEntity(entity);

        // records always equal
        TrackUrlData track = (TrackUrlData) entity;
        if (!RUNNING) {
            RECORD = track.getRecord();
            RUNNING = true;
        }
        assertEquals(RECORD, track.getRecord());
    }

}
