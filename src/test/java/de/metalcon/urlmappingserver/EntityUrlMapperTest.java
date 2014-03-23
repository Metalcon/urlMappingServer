package de.metalcon.urlmappingserver;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public abstract class EntityUrlMapperTest {

    protected static final String VALID_NAME = "Testy";

    protected static EntityUrlData ENTITY;

    protected static EntityUrlData SIMILAR_ENTITY;

    protected static MuidType MUID_TYPE;

    protected static MuidType INVALID_TYPE;

    protected EntityUrlMappingManager manager;

    protected EntityUrlMapper mapper;
    
    protected EntityUrlData entity;

    protected String mappingUnique;

    @BeforeClass
    public static void beforeClass() {
        MUID_TYPE = ENTITY.getMuid().getMuidType();
        INVALID_TYPE = getInvalidMuidType(MUID_TYPE);
    }

    @Before
    public void setUp() {
        manager = new EntityUrlMappingManager();
        mapper = manager.getMapper(MUID_TYPE);
        mapper.registerMuid(ENTITY);
        mappingUnique = generateUniqueMapping(ENTITY);
    }

    @Test
    public void testNameWithMuid() {
        checkForEntityMapping(mappingUnique);
    }

    @Test
    public void testName() {
        checkForEntityMapping(ENTITY.getName());
    }

    @Test
    public void testFirstNameRegistrationOnly() {
        assertEquals(ENTITY.getName(), SIMILAR_ENTITY.getName());
        mapper.registerMuid(SIMILAR_ENTITY);
        testName();
    }

    @Test
    public void testUniqueMappings() {
        mapper.registerMuid(SIMILAR_ENTITY);
        testNameWithMuid();
        assertEquals(SIMILAR_ENTITY.getMuid(),
                resolveMapping(generateUniqueMapping(SIMILAR_ENTITY)));
    }

    @Test(
            expected = IllegalArgumentException.class)
    public void testMuidTypeInvalid() {
        mapper.resolveMuid(generateUrl(mappingUnique), INVALID_TYPE);
    }

    protected Map<String, String> generateUrl(String mapping) {
        Map<String, String> pathVars = new HashMap<String, String>();
        pathVars.put(mapper.getUrlPathVarName(), mapping);
        return pathVars;
    }

    protected Muid resolveMapping(String mapping) {
        return mapper.resolveMuid(generateUrl(mapping), MUID_TYPE);
    }

    protected void checkForEntityMapping(String mapping) {
        assertEquals(ENTITY.getMuid(), resolveMapping(mapping));
    }

    protected static String generateUniqueMapping(EntityUrlData entity) {
        return entity.getName() + EntityUrlMapper.WORD_SEPARATOR
                + entity.getMuid();
    }

    protected static MuidType getInvalidMuidType(MuidType validMuidType) {
        short typeIdentifier = (short) (validMuidType.getRawIdentifier() + 1);
        if (typeIdentifier == 10) {
            typeIdentifier = 0;
        }
        return MuidType.parseShort(typeIdentifier);
    }

}
