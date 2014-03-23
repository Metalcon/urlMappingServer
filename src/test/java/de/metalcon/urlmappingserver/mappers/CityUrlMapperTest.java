package de.metalcon.urlmappingserver.mappers;

import org.junit.BeforeClass;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.CityUrlData;

public class CityUrlMapperTest extends EntityUrlMapperTest {

    public static final CityUrlData CITY = new CityUrlData(
            Muid.create(MuidType.CITY), VALID_NAME);

    protected static final CityUrlData SIMILAR_CITY = new CityUrlData(
            Muid.create(MuidType.CITY), CITY.getName());

    @BeforeClass
    public static void beforeClass() {
        ENTITY = CITY;
        SIMILAR_ENTITY = SIMILAR_CITY;
        EntityUrlMapperTest.beforeClass();
    }

}
