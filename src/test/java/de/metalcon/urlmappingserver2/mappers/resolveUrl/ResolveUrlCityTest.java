package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.CityFactory;

public class ResolveUrlCityTest extends ResolveUrlNamedEntityTest {

    protected static CityFactory CITY_FACTORY = new CityFactory();

    @Override
    protected EntityFactory getFactory() {
        return CITY_FACTORY;
    }

}
