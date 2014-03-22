package de.metalcon.urlmappingserver;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public abstract class EntityUrlMapperTest {

    protected static final String VALID_NAME = "Testy";

    protected static MuidType MUID_TYPE;

    protected static EntityUrlData ENTITY;

    protected EntityUrlMappingManager manager;

    protected EntityUrlMapper mapper;

    @Before
    public void setUp() {
        manager = new EntityUrlMappingManager();
        mapper = manager.getMapper(MUID_TYPE);
        mapper.registerMuid(ENTITY);
    }

    protected Map<String, String> generateUrl(String mapping) {
        Map<String, String> pathVars = new HashMap<String, String>();
        pathVars.put(mapper.getUrlPathVarName(), mapping);
        return pathVars;
    }

    protected void checkForMapping(String mapping) {
        assertNotNull(mapper.resolveMuid(generateUrl(mapping), MUID_TYPE));
    }

    @Test
    public void testName() {
        checkForMapping(ENTITY.getName());
    }

    @Test
    public void testNameWithMuid() {
        checkForMapping(ENTITY.getName() + "-" + ENTITY.getMuid());
    }

}
