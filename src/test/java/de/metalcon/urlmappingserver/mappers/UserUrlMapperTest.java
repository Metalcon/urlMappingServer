package de.metalcon.urlmappingserver.mappers;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.UserUrlData;

public class UserUrlMapperTest extends EntityUrlMapperTest {

    protected static final String VALID_LAST_NAME = "Testarum";

    protected static final UserUrlData USER = new UserUrlData(
            Muid.create(MuidType.USER), VALID_NAME, VALID_LAST_NAME);

    protected static final UserUrlData SIMILAR_USER =
            new UserUrlData(Muid.create(MuidType.USER), USER.getFirstName(),
                    USER.getLastName());

    @BeforeClass
    public static void beforeClass() {
        ENTITY = USER;
        SIMILAR_ENTITY = SIMILAR_USER;
        EntityUrlMapperTest.beforeClass();
    }

    @Override
    public void setUp() {
        super.setUp();
        mappingUnique = generateMappingUnique(USER);
    }

    @Override
    public void testName() {
        checkForEntityMapping(generateMappingName(USER));
    }

    @Override
    public void testUniqueMappings() {
        mapper.registerMuid(SIMILAR_USER);
        testNameWithMuid();
        assertEquals(SIMILAR_USER.getMuid(),
                resolveMapping(generateMappingUnique(SIMILAR_USER)));
    }

    protected static String generateMappingUnique(UserUrlData user) {
        return generateMappingName(user) + WORD_SEPARATOR + user.getMuid();
    }

    protected static String generateMappingName(UserUrlData user) {
        return user.getFirstName() + WORD_SEPARATOR + user.getLastName();
    }

}
