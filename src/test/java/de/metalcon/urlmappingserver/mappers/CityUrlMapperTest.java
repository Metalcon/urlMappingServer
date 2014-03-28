package de.metalcon.urlmappingserver.mappers;

import org.junit.BeforeClass;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.CityUrlData;

public class CityUrlMapperTest extends EntityUrlMapperTest {

    protected static final CityUrlData CITY = new CityUrlData(
            MuidFactory.generateMuid(MuidType.CITY), VALID_NAME);

    protected static final CityUrlData SIMILAR_CITY = new CityUrlData(
            MuidFactory.generateMuid(MuidType.CITY), CITY.getName());

    @BeforeClass
    public static void beforeClass() {
        ENTITY = CITY;
        SIMILAR_ENTITY = SIMILAR_CITY;
        EntityUrlMapperTest.beforeClass();
    }

}
