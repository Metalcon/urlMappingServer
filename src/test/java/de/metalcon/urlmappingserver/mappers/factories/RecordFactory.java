package de.metalcon.urlmappingserver.mappers.factories;

import java.util.Calendar;
import java.util.Map;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver.mappers.EntityFactory;

public class RecordFactory extends EntityFactory {

    protected boolean useSameParent;

    protected boolean useEmptyParent;

    protected BandFactory bandFactory;

    protected BandUrlData band = null;

    protected int releaseYear;

    public RecordFactory(
            BandFactory bandFactory) {
        super("pathRecord", MuidType.RECORD);
        useSameParent = false;
        useEmptyParent = false;
        this.bandFactory = bandFactory;
        releaseYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    public boolean usesSameParent() {
        return useSameParent;
    }

    public void setUseSameParent(boolean useSameParent) {
        this.useSameParent = useSameParent;
        if (!useSameParent) {
            band = null;
        }
    }

    public boolean usesEmptyParent() {
        return useEmptyParent;
    }

    public void setUseEmptyParent(boolean useEmptyParent) {
        this.useEmptyParent = useEmptyParent;
    }

    public String getMappingReleaseYear(RecordUrlData record) {
        return record.getReleaseYear() + WORD_SEPARATOR
                + getMappingName(record);
    }

    @Override
    public String getMappingName(EntityUrlData entity) {
        if (!entity.hasEmptyMuid()) {
            return super.getMappingName(entity);
        }
        return EMPTY_ENTITY;
    }

    @Override
    public String getMappingId(EntityUrlData entity) {
        if (!entity.hasEmptyMuid()) {
            return super.getMappingId(entity);
        }
        return EMPTY_ENTITY;
    }

    @Override
    public Map<String, String> getUrl(EntityUrlData entity, String mapping) {
        RecordUrlData record = (RecordUrlData) entity;
        BandUrlData band = record.getBand();

        Map<String, String> url =
                bandFactory.getUrl(band, bandFactory.getMappingId(band));
        url.put(urlPathVarName, mapping);

        return url;
    }

    @Override
    public EntityUrlData getEntityFull() {
        return new RecordUrlData(MuidFactory.generateMuid(getMuidType()),
                "record" + crrEntityId++, getBand(), getReleaseYear());
    }

    @Override
    public EntityUrlData getEntityIdentical(EntityUrlData entity) {
        RecordUrlData record = (RecordUrlData) entity;
        return new RecordUrlData(MuidFactory.generateMuid(getMuidType()),
                record.getName(), record.getBand(), record.getReleaseYear());
    }

    /**
     * create record having the same name like another record
     */
    public RecordUrlData getRecordSameName(RecordUrlData record) {
        return new RecordUrlData(MuidFactory.generateMuid(getMuidType()),
                record.getName(), getBand(), getReleaseYear());
    }

    /**
     * create record without a MUID ("empty")
     */
    public RecordUrlData getRecordWoMuid() {
        return new RecordUrlData(getBand());
    }

    /**
     * create record without release year
     */
    public RecordUrlData getRecordWoReleaseYear() {
        RecordUrlData record = (RecordUrlData) getEntityFull();
        return new RecordUrlData(record.getMuid(), record.getName(),
                record.getBand(), 0);
    }

    /**
     * create copy of another record but remove release year
     */
    public RecordUrlData getRecordWoReleaseYear(RecordUrlData record) {
        return new RecordUrlData(MuidFactory.generateMuid(getMuidType()),
                record.getName(), record.getBand(), 0);
    }

    protected BandUrlData getBand() {
        if (!useEmptyParent) {
            if (useSameParent) {
                if (band == null) {
                    band = (BandUrlData) bandFactory.getEntityFull();
                }
                return band;
            }
            return (BandUrlData) bandFactory.getEntityFull();
        }
        return bandFactory.getBandWoMuid();
    }

    protected int getReleaseYear() {
        return releaseYear++;
    }

}
