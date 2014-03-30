package de.metalcon.urlmappingserver2.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver2.EntityUrlMapperTest;

public abstract class ResolveMuidTest extends EntityUrlMapperTest {

    @Test(
            expected = IllegalArgumentException.class)
    public void testMuidInvalid() {
        entity = getEntityFull();
        registerEntity(entity);
        mapper.resolveMuid(getUrl(getMappingId(entity)), INVALID_TYPE);
    }

    /**
     * entities are not accessible via any mapping if not registered yet
     */
    @Test
    public void testNotRegistered() {
        entity = getEntityFull();
        assertNull(resolveMuid(getMappingId(entity)));
    }

    /**
     * entities are always accessible via their ID mapping
     */
    @Test
    public void testMappingId() {
        entity = getEntityFull();
        registerEntity(entity);
        checkMappingId(entity);
    }

    /**
     * register entity and check if accessible via all the mappings created
     */
    @Test
    public void testAllMappings() {
        entity = getEntityFull();
        registerEntity(entity);
        checkAllMappings(entity);
    }

    /**
     * if identical entity registered, the first has to be accessible via all
     * its mappings and the second one via ID mapping only
     */
    @Test
    public void testIdenticalRegistered() {
        entity = getEntityFull();
        registerEntity(entity);
        EntityUrlData identical = getIdentical(entity);
        registerEntity(identical);

        checkAllMappings(entity);
        checkMappingId(identical);
    }

    /**
     * if multiple entities registered, they all have to be accessible via all
     * their mappings
     */
    @Test
    public void testMultipleRegistered() {
        List<EntityUrlData> entities = new LinkedList<EntityUrlData>();
        for (int i = 0; i < 10; i++) {
            entities.add(getEntityFull());
        }
        for (EntityUrlData entity : entities) {
            registerEntity(entity);
            checkAllMappings(entity);
        }
    }

    /**
     * check if an entity is accessible via all the mappings created for it
     */
    protected void checkAllMappings(EntityUrlData entity) {
        checkForMapping(entity, getMappingId(entity));
    }

    /**
     * check if an entity is accessible via its ID mapping
     */
    protected void checkMappingId(EntityUrlData entity) {
        checkForMapping(entity, getMappingId(entity));
    }

    /**
     * check if specific mapping resolves correct MUID
     */
    protected void checkForMapping(EntityUrlData entity, String mapping) {
        assertEquals(entity.getMuid(), resolveMuid(mapping));
    }

}
