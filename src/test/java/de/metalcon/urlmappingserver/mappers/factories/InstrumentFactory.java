package de.metalcon.urlmappingserver.mappers.factories;

import de.metalcon.domain.UidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.InstrumentUrlData;
import de.metalcon.urlmappingserver.mappers.EntityFactory;

public class InstrumentFactory extends EntityFactory {

    public InstrumentFactory() {
        super("pathInstrument", UidType.INSTRUMENT);
    }

    @Override
    public EntityUrlData getEntityFull() {
        return new InstrumentUrlData(MuidFactory.generateMuid(getUidType()),
                "instrument" + crrEntityId++);
    }

    @Override
    public EntityUrlData getEntityIdentical(EntityUrlData entity) {
        return new InstrumentUrlData(MuidFactory.generateMuid(getUidType()),
                entity.getName());
    }

}
