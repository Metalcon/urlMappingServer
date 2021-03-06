package de.metalcon.urlmappingserver.mappers.factories;

import de.metalcon.domain.UidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.mappers.EntityFactory;

public class BandFactory extends EntityFactory {

    public BandFactory() {
        super("pathBand", UidType.BAND);
    }

    @Override
    public String getMappingId(EntityUrlData entity) {
        if (!entity.hasEmptyMuid()) {
            return super.getMappingId(entity);
        }
        return EMPTY_ENTITY;
    }

    @Override
    public String getMappingName(EntityUrlData entity) {
        if (!entity.hasEmptyMuid()) {
            return super.getMappingName(entity);
        }
        return EMPTY_ENTITY;
    }

    @Override
    public EntityUrlData getEntityFull() {
        return new BandUrlData(MuidFactory.generateMuid(getUidType()), "band"
                + crrEntityId++);
    }

    @Override
    public EntityUrlData getEntityIdentical(EntityUrlData entity) {
        return new BandUrlData(MuidFactory.generateMuid(getUidType()),
                entity.getName());
    }

    /**
     * create band without a MUID ("empty")
     */
    public BandUrlData getBandWoMuid() {
        return new BandUrlData();
    }

}
