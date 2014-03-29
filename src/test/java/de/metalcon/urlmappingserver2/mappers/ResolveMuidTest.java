package de.metalcon.urlmappingserver2.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver2.EntityUrlMapperTest;

public abstract class ResolveMuidTest extends EntityUrlMapperTest {

    @Test
    public void testNotRegistered() {
        entity = getEntityFull();
        assertNull(resolveMuid(getMappingId(entity)));
        assertNull(resolveMuid(getMappingName(entity)));
    }

    @Test
    public void testMappingId() {
        entity = getEntityFull();
        registerEntity(entity);
        checkMappingId(entity);
    }

    @Test
    public void testMappingName() {
        entity = getEntityFull();
        registerEntity(entity);
        checkMappingName(entity);
    }

    @Test
    public void testAllMappings() {
        entity = getEntityFull();
        registerEntity(entity);
        checkAllMappings(entity);
    }

    @Test
    public void testMappingNameNoOverride() {
        entity = getEntityFull();
        registerEntity(entity);
        EntityUrlData identical = getEntityFull();
        registerEntity(identical);
        checkMappingId(identical);
        checkAllMappings(entity);
    }

    protected void checkAllMappings(EntityUrlData entity) {
        checkForMapping(entity, getMappingId(entity));
        checkForMapping(entity, getMappingName(entity));
    }

    protected void checkMappingId(EntityUrlData entity) {
        checkForMapping(entity, getMappingId(entity));
    }

    protected void checkMappingName(EntityUrlData entity) {
        checkForMapping(entity, getMappingName(entity));
    }

    /**
     * check if specific mapping resolves correct MUID
     */
    protected void checkForMapping(EntityUrlData entity, String mapping) {
        assertEquals(entity.getMuid(), resolveMuid(mapping));
    }

}
