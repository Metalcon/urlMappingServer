package de.metalcon.urlmappingserver.mappers;

import org.junit.BeforeClass;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;

public class UserUrlMapperTest extends EntityUrlMapperTest {

    public static final BandUrlData USER = new BandUrlData(
            Muid.create(MuidType.USER), VALID_NAME);

    protected static final BandUrlData SIMILAR_USER = new BandUrlData(
            Muid.create(MuidType.USER), USER.getName());

    @BeforeClass
    public static void beforeClass() {
        ENTITY = USER;
        SIMILAR_ENTITY = SIMILAR_USER;
        EntityUrlMapperTest.beforeClass();
    }

}
