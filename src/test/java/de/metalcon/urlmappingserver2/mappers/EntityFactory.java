package de.metalcon.urlmappingserver2.mappers;

import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public abstract class EntityFactory {

    protected MuidType muidType;

    protected int crrEntityId;

    public EntityFactory(
            MuidType muidType) {
        this.muidType = muidType;
        crrEntityId = 1;
    }

    public MuidType getMuidType() {
        return muidType;
    }

    /**
     * create unique entity
     */
    abstract public EntityUrlData getEntityFull();

    /**
     * create copy of another entity
     */
    abstract public EntityUrlData getEntityIdentical(EntityUrlData entity);

}
