package de.metalcon.urlmappingserver2.mappers.factories;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EventUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;

public class EventFactory extends EntityFactory {

    public EventFactory() {
        super(MuidType.EVENT);
    }

    @Override
    public EntityUrlData getEntityFull() {
        return new EventUrlData(MuidFactory.generateMuid(getMuidType()));
    }

    @Override
    public EntityUrlData getEntityIdentical(EntityUrlData entity) {
        return getEntityFull();
    }

}
