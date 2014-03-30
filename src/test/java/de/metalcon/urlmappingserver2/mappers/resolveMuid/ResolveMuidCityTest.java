package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.CityFactory;

public class ResolveMuidCityTest extends ResolveMuidNamedEntityTest {

    protected static CityFactory CITY_FACTORY = new CityFactory();

    @Override
    protected EntityFactory getFactory() {
        return CITY_FACTORY;
    }

}
