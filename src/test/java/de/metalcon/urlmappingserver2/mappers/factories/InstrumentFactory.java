package de.metalcon.urlmappingserver2.mappers.factories;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.InstrumentUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;

public class InstrumentFactory extends EntityFactory {

    public InstrumentFactory() {
        super("pathInstrument", MuidType.INSTRUMENT);
    }

    @Override
    public EntityUrlData getEntityFull() {
        return new InstrumentUrlData(MuidFactory.generateMuid(getMuidType()),
                "instrument" + crrEntityId++);
    }

    @Override
    public EntityUrlData getEntityIdentical(EntityUrlData entity) {
        return new InstrumentUrlData(MuidFactory.generateMuid(getMuidType()),
                entity.getName());
    }

}
