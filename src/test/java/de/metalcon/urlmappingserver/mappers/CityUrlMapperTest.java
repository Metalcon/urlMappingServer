package de.metalcon.urlmappingserver.mappers;

import org.junit.BeforeClass;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.CityUrlData;

public class CityUrlMapperTest extends EntityUrlMapperTest {

    public static final CityUrlData CITY = new CityUrlData(
            Muid.create(MuidType.CITY), VALID_NAME);

    @BeforeClass
    public static void beforeClass() {
        ENTITY = CITY;
        EntityUrlMapperTest.beforeClass();
    }

}
