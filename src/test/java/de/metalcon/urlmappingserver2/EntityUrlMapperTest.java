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
import de.metalcon.urlmappingserver2.mappers.EntityFactory;

public abstract class EntityUrlMapperTest {

    protected static final String EMPTY_ENTITY = EntityUrlMapper.EMPTY_ENTITY;

    protected static final String WORD_SEPARATOR =
            EntityUrlMapper.WORD_SEPARATOR;

    protected static final String VALID_NAME = "Testy";

    protected static boolean READY = false;

    protected static EntityFactory FACTORY;

    private static MuidType TYPE;

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
            FACTORY = getFactory();
            TYPE = FACTORY.getMuidType();
            INVALID_TYPE = getInvalidMuidType(TYPE);
            READY = true;
        }
        manager = new EntityUrlMappingManager();
        mapper = manager.getMapper(TYPE);
    }

    abstract protected EntityFactory getFactory();

    /**
     * check if entity not registered yet, then register
     */
    protected void registerEntity(EntityUrlData entity) {
        assertNull(resolveMuid(entity, getMappingId(entity)));
        mapper.registerMuid(entity);
    }

    protected Muid resolveMuid(EntityUrlData entity, String mapping) {
        return mapper.resolveMuid(getUrl(entity, mapping), TYPE);
    }

    protected Map<String, String> getUrl(EntityUrlData entity, String mapping) {
        Map<String, String> url = new HashMap<String, String>();
        url.put(mapper.getUrlPathVarName(), mapping);
        return url;
    }

    protected String getMappingId(EntityUrlData entity) {
        return getMappingName(entity) + WORD_SEPARATOR + entity.getMuid();
    }

    protected String getMappingName(EntityUrlData entity) {
        return EntityUrlMapper.convertToUrlText(entity.getName());
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
