package de.metalcon.urlmappingserver2.mappers.factories;

import java.util.Calendar;

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
        super(MuidType.RECORD);
        this.bandFactory = bandFactory;
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
