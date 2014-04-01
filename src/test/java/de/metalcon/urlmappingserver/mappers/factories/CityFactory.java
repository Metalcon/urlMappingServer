package de.metalcon.urlmappingserver.mappers.factories;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.CityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.mappers.EntityFactory;

public class CityFactory extends EntityFactory {

    public CityFactory() {
        super("pathCity", MuidType.CITY);
    }

    @Override
    public EntityUrlData getEntityFull() {
        return new CityUrlData(MuidFactory.generateMuid(getMuidType()), "city"
                + crrEntityId++);
    }

    @Override
    public EntityUrlData getEntityIdentical(EntityUrlData entity) {
        return new CityUrlData(MuidFactory.generateMuid(getMuidType()),
                entity.getName());
    }

}
