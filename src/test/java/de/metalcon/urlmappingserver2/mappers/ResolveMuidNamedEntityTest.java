package de.metalcon.urlmappingserver2.mappers;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public abstract class ResolveMuidNamedEntityTest extends ResolveMuidTest {

    @Override
    public void testNotRegistered() {
        super.testNotRegistered();
        assertNull(resolveMuid(getMappingName(entity)));
    }

    /**
     * register an entity and check if accessible via shorter name mapping
     */
    @Test
    public void testMappingName() {
        entity = getEntityFull();
        registerEntity(entity);
        checkMappingName(entity);
    }

    /**
     * shorter name mapping must not be overridden by later entity registrations
     * with same name - new entity accessible via ID mapping only
     */
    @Test
    public void testMappingNameNoOverride() {
        entity = getEntityFull();
        registerEntity(entity);
        EntityUrlData identical = getEntityFull();
        registerEntity(identical);
        checkMappingId(identical);
        checkAllMappings(entity);
    }

    @Override
    protected void checkAllMappings(EntityUrlData entity) {
        super.checkAllMappings(entity);
        checkForMapping(entity, getMappingName(entity));
    }

    /**
     * check if an entity is accessible via its shorter name mapping
     */
    protected void checkMappingName(EntityUrlData entity) {
        checkForMapping(entity, getMappingName(entity));
    }

}
