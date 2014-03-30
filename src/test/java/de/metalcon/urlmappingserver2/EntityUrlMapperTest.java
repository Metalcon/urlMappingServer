package de.metalcon.urlmappingserver2;

import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public abstract class EntityUrlMapperTest {

    protected static final String EMPTY_ENTITY = EntityUrlMapper.EMPTY_ENTITY;

    protected static final String WORD_SEPARATOR =
            EntityUrlMapper.WORD_SEPARATOR;

    protected static final String VALID_NAME = "Testy";

    protected static boolean READY = false;

    protected static MuidType TYPE;

    protected static MuidType INVALID_TYPE;

    // static
    // ----------
    // scope

    protected EntityUrlMappingManager manager;

    protected EntityUrlMapper mapper;

    protected EntityUrlData entity;

    @Before
    public void setUp() {
        if (!READY) {
            TYPE = getMuidType();
            INVALID_TYPE = getInvalidMuidType(TYPE);
            READY = true;
        }
        manager = new EntityUrlMappingManager();
        mapper = manager.getMapper(TYPE);
    }

    abstract protected MuidType getMuidType();

    abstract protected EntityUrlData getEntityFull();

    /**
     * check if entity not registered yet, then register
     */
    protected void registerEntity(EntityUrlData entity) {
        assertNull(resolveMuid(getMappingId(entity)));
        mapper.registerMuid(entity);
    }

    protected Muid resolveMuid(String mapping) {
        return mapper.resolveMuid(getUrl(mapping), TYPE);
    }

    protected Map<String, String> getUrl(String mapping) {
        Map<String, String> url = new HashMap<String, String>();
        url.put(mapper.getUrlPathVarName(), mapping);
        return url;
    }

    protected String getMappingId(EntityUrlData entity) {
        return getMappingName(entity) + WORD_SEPARATOR + entity.getMuid();
    }

    protected String getMappingName(EntityUrlData entity) {
        return entity.getName().replace(" ", "-");
    }

    private static MuidType getInvalidMuidType(MuidType validType) {
        short identifierInvalidType =
                (short) (validType.getRawIdentifier() + 1);
        if (identifierInvalidType == 10) {
            identifierInvalidType = 0;
        }
        return MuidType.parseShort(identifierInvalidType);
    }

    @AfterClass
    public static void afterClass() {
        READY = false;
    }

}
