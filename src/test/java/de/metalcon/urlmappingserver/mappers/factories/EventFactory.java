package de.metalcon.urlmappingserver.mappers.factories;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EventUrlData;
import de.metalcon.urlmappingserver.mappers.EntityFactory;

public class EventFactory extends EntityFactory {

    public EventFactory() {
        super("pathEvent", MuidType.EVENT);
    }

    @Override
    public String getMappingId(EntityUrlData entity) {
        // the only event mapping: MUID
        return entity.getMuid().toString();
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
