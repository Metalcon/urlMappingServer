package de.metalcon.urlmappingserver.mappers.resolveUrl;

import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveUrlNamedEntityTest;
import de.metalcon.urlmappingserver.mappers.factories.CityFactory;

public class ResolveUrlCityTest extends ResolveUrlNamedEntityTest {

    protected static CityFactory CITY_FACTORY = new CityFactory();

    @Override
    protected EntityFactory getFactory() {
        return CITY_FACTORY;
    }

}
