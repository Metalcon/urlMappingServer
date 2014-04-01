package de.metalcon.urlmappingserver.mappers.resolveUrl;

import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;
import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveUrlNamedEntityTest;
import de.metalcon.urlmappingserver.mappers.factories.TrackFactory;

public abstract class ResolveUrlTrackTest extends ResolveUrlNamedEntityTest {

    protected static TrackFactory TRACK_FACTORY = new TrackFactory(
            ResolveUrlBandTest.BAND_FACTORY,
            ResolveUrlRecordTest.RECORD_FACTORY);

    protected static boolean RUNNING = false;

    protected TrackUrlData track;

    /**
     * register track so that main mapping is track number mapping and check
     * this
     */
    @Test
    public void testRegisterSameName() {
        track = (TrackUrlData) TRACK_FACTORY.getEntityFull();
        registerEntity(track);

        TrackUrlData trackWithSameName = TRACK_FACTORY.getTrackSameName(track);
        registerEntity(trackWithSameName);

        checkMappingName(track);
        checkMappingTrackNumber(trackWithSameName);
    }

    /**
     * register track so that main mapping would be track number mapping but is
     * ID mapping as no track number for track set and check this
     */
    @Test
    public void testRegisterSameNameWoTrackNumber() {
        track = (TrackUrlData) TRACK_FACTORY.getEntityFull();
        registerEntity(track);

        TrackUrlData trackWithSameNameWoTrackNumber =
                TRACK_FACTORY.getTrackSameNameWoTrackNumber(track);
        registerEntity(trackWithSameNameWoTrackNumber);

        checkMappingName(track);
        checkMappingId(trackWithSameNameWoTrackNumber);
    }

    @Override
    protected EntityFactory getFactory() {
        return TRACK_FACTORY;
    }

    /**
     * check if main mapping for track is track number mapping
     */
    protected void checkMappingTrackNumber(TrackUrlData track) {
        checkMainMapping(track, TRACK_FACTORY.getMappingTrackNumber(track));
    }

}
