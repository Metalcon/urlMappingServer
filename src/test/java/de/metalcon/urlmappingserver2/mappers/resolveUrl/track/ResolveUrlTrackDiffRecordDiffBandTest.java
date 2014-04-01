package de.metalcon.urlmappingserver2.mappers.resolveUrl.track;

import static org.junit.Assert.assertNotSame;

import org.junit.BeforeClass;

import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;
import de.metalcon.urlmappingserver2.mappers.resolveUrl.ResolveUrlTrackTest;

public class ResolveUrlTrackDiffRecordDiffBandTest extends ResolveUrlTrackTest {

    protected static RecordUrlData RECORD;

    protected static BandUrlData BAND;

    @BeforeClass
    public static void beforeClass() {
        TRACK_FACTORY.setUseSameBand(false);
        TRACK_FACTORY.setUseEmptyBand(false);
        TRACK_FACTORY.setUseSameParent(false);
        TRACK_FACTORY.setUseEmptyParent(false);
    }

    /**
     * tracks are independent: name mapping is main mapping for both
     */
    @Override
    public void testRegisterSameName() {
        track = (TrackUrlData) TRACK_FACTORY.getEntityFull();
        registerEntity(track);

        TrackUrlData trackSameName = TRACK_FACTORY.getTrackSameName(track);
        registerEntity(trackSameName);

        checkMappingName(track);
        checkMappingName(trackSameName);
    }

    /**
     * tracks are independent: name mapping is main for both
     */
    @Override
    public void testRegisterSameNameWoTrackNumber() {
        track = (TrackUrlData) TRACK_FACTORY.getEntityFull();
        registerEntity(track);

        TrackUrlData trackWithSameNameWoTrackNumber =
                TRACK_FACTORY.getTrackSameNameWoTrackNumber(track);
        registerEntity(trackWithSameNameWoTrackNumber);

        checkMappingName(track);
        checkMappingName(trackWithSameNameWoTrackNumber);
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
