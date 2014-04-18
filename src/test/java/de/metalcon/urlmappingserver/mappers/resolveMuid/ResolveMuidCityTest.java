package de.metalcon.urlmappingserver.mappers.resolveMuid;

import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver.mappers.factories.CityFactory;

public class ResolveMuidCityTest extends ResolveMuidNamedEntityTest {

    protected static CityFactory CITY_FACTORY = new CityFactory();

    @Override
    protected EntityFactory getFactory() {
        return CITY_FACTORY;
    }

}
