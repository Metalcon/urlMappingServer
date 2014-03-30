package de.metalcon.urlmappingserver2.mappers.factories;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;

public class BandFactory extends EntityFactory {

    public BandFactory() {
        super(MuidType.BAND);
    }

    /**
     * create unique band
     */
    @Override
    public EntityUrlData getEntityFull() {
        return new BandUrlData(MuidFactory.generateMuid(getMuidType()), "band"
                + crrEntityId++);
    }

    /**
     * create copy of another band
     */
    @Override
    public EntityUrlData getEntityIdentical(EntityUrlData entity) {
        return new BandUrlData(MuidFactory.generateMuid(getMuidType()),
                entity.getName());
    }

    /**
     * create band without a MUID ("empty")
     */
    public BandUrlData getBandWoMuid() {
        return new BandUrlData();
    }

}
