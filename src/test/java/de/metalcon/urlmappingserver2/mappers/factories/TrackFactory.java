package de.metalcon.urlmappingserver2.mappers.factories;

import java.util.Map;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;

public class TrackFactory extends EntityFactory {

    protected boolean useSameParent;

    protected boolean useEmptyParent;

    protected BandFactory bandFactory;

    protected RecordFactory recordFactory;

    protected RecordUrlData record;

    protected int trackNumber;

    public TrackFactory(
            BandFactory bandFactory,
            RecordFactory recordFactory) {
        super("pathTrack", MuidType.TRACK);
        useSameParent = false;
        useEmptyParent = false;
        this.bandFactory = bandFactory;
        this.recordFactory = recordFactory;
        setUseSameBand(false);
        setUseEmptyBand(false);
        trackNumber = 1;
    }

    public void setUseSameBand(boolean useSameBand) {
        recordFactory.setUseSameParent(useSameBand);
    }

    public void setUseEmptyBand(boolean useEmptyBand) {
        recordFactory.setUseEmptyParent(useEmptyBand);
    }

    public void setUseSameParent(boolean useSameParent) {
        this.useSameParent = useSameParent;
        if (!useSameParent) {
            record = null;
        }
    }

    public void setUseEmptyParent(boolean useEmptyParent) {
        this.useEmptyParent = useEmptyParent;
    }

    public String getMappingTrackNumber(TrackUrlData track) {
        return track.getTrackNumber() + WORD_SEPARATOR + getMappingName(track);
    }

    @Override
    public Map<String, String> getUrl(EntityUrlData entity, String mapping) {
        TrackUrlData track = (TrackUrlData) entity;
        RecordUrlData record = track.getRecord();

        Map<String, String> url =
                recordFactory
                        .getUrl(record, recordFactory.getMappingId(record));
        url.put(urlPathVarName, mapping);

        return url;
    }

    @Override
    public EntityUrlData getEntityFull() {
        RecordUrlData record = getRecord();
        BandUrlData band = !record.hasEmptyMuid() ? null : record.getBand();
        if (band != null) {
            record = null;
        }

        return new TrackUrlData(MuidFactory.generateMuid(getMuidType()),
                "track" + crrEntityId++, band, record, getTrackNumber());
    }

    @Override
    public EntityUrlData getEntityIdentical(EntityUrlData entity) {
        TrackUrlData track = (TrackUrlData) entity;
        RecordUrlData record = track.getRecord();
        BandUrlData band = (!record.hasEmptyMuid()) ? null : record.getBand();
        if (band != null) {
            record = null;
        }

        return new TrackUrlData(MuidFactory.generateMuid(getMuidType()),
                track.getName(), band, record, track.getTrackNumber());
    }

    public TrackUrlData getTrackSameName(TrackUrlData track) {
        RecordUrlData record = getRecord();
        BandUrlData band = !record.hasEmptyMuid() ? null : record.getBand();
        if (band != null) {
            record = null;
        }
        return new TrackUrlData(MuidFactory.generateMuid(getMuidType()),
                track.getName(), band, record, getTrackNumber());
    }

    public TrackUrlData getTrackSameNameWoTrackNumber(TrackUrlData track) {
        RecordUrlData record = getRecord();
        BandUrlData band = !record.hasEmptyMuid() ? null : record.getBand();
        if (band != null) {
            record = null;
        }
        return new TrackUrlData(MuidFactory.generateMuid(getMuidType()),
                track.getName(), band, record, 0);
    }

    public RecordUrlData getRecord() {
        if (!useEmptyParent) {
            if (useSameParent) {
                if (record == null) {
                    record = (RecordUrlData) recordFactory.getEntityFull();
                }
                return record;
            }
            return (RecordUrlData) recordFactory.getEntityFull();
        }
        return recordFactory.getRecordWoMuid();
    }

    public int getTrackNumber() {
        return trackNumber++;
    }

}
