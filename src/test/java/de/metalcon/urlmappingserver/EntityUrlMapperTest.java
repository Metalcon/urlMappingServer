package de.metalcon.urlmappingserver;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public abstract class EntityUrlMapperTest {

    protected static final String VALID_NAME = "Testy";

    protected static EntityUrlData ENTITY;

    protected static String UNIQUE_MAPPING;

    protected static MuidType MUID_TYPE;

    protected static MuidType INVALID_TYPE;

    protected EntityUrlMappingManager manager;

    protected EntityUrlMapper mapper;

    @BeforeClass
    public static void beforeClass() {
        UNIQUE_MAPPING = ENTITY.getName() + "-" + ENTITY.getMuid();
        MUID_TYPE = ENTITY.getMuid().getMuidType();
        INVALID_TYPE = getInvalidMuidType(MUID_TYPE);
    }

    @Before
    public void setUp() {
        manager = new EntityUrlMappingManager();
        mapper = manager.getMapper(MUID_TYPE);
        mapper.registerMuid(ENTITY);
    }

    @Test
    public void testName() {
        checkForMapping(ENTITY.getName());
    }

    @Test
    public void testNameWithMuid() {
        checkForMapping(UNIQUE_MAPPING);
    }

    @Test(
            expected = IllegalArgumentException.class)
    public void testMuidTypeInvalid() {
        mapper.resolveMuid(generateUrl(UNIQUE_MAPPING), INVALID_TYPE);
    }

    protected Map<String, String> generateUrl(String mapping) {
        Map<String, String> pathVars = new HashMap<String, String>();
        pathVars.put(mapper.getUrlPathVarName(), mapping);
        return pathVars;
    }

    protected void checkForMapping(String mapping) {
        assertNotNull(mapper.resolveMuid(generateUrl(mapping), MUID_TYPE));
    }

    protected static MuidType getInvalidMuidType(MuidType validMuidType) {
        short typeIdentifier = (short) (validMuidType.getRawIdentifier() + 1);
        if (typeIdentifier == 10) {
            typeIdentifier = 0;
        }
        return MuidType.parseShort(typeIdentifier);
    }

}
