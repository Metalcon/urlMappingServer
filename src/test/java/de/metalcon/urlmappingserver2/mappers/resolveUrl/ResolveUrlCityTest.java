package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.CityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlNamedEntityTest;

public class ResolveUrlCityTest extends ResolveUrlNamedEntityTest {

    @Override
    protected MuidType getInstanceMuidType() {
        return getMuidType();
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getCity();
    }

    protected static MuidType getMuidType() {
        return MuidType.CITY;
    }

    public static CityUrlData getCity() {
        return new CityUrlData(MuidFactory.generateMuid(getMuidType()),
                VALID_NAME);
    }

}
