package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.CityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlNamedEntityTest;

public class ResolveUrlCityTest extends ResolveUrlNamedEntityTest {

    @Override
    protected MuidType getMuidType() {
        return MuidType.CITY;
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getCity();
    }

    public static CityUrlData getCity() {
        return new CityUrlData(MuidFactory.generateMuid(TYPE), VALID_NAME);
    }

}
