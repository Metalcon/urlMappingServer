package de.metalcon.urlmappingserver2.mappers.factories;

import java.util.Calendar;
import java.util.Map;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;

public class RecordFactory extends EntityFactory {

    protected BandFactory bandFactory;

    public RecordFactory(
            BandFactory bandFactory) {
        super("pathRecord", MuidType.RECORD);
        this.bandFactory = bandFactory;
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
     * create record without a MUID ("empty")
     */
    public RecordUrlData getRecordWoMuid() {
        return new RecordUrlData(getBand());
    }

    protected BandUrlData getBand() {
        return (BandUrlData) bandFactory.getEntityFull();
    }

    protected int getReleaseYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

}
