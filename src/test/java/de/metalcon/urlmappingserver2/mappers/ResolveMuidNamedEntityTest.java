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
     * register entity and check if accessible via shorter name mapping
     */
    @Test
    public void testMappingName() {
        entity = FACTORY.getEntityFull();
        registerEntity(entity);
        checkMappingName(entity);
    }

    @Override
    protected void checkAllMappings(EntityUrlData entity) {
        super.checkAllMappings(entity);
        checkMappingName(entity);
    }

    /**
     * check if an entity is accessible via its shorter name mapping
     */
    protected void checkMappingName(EntityUrlData entity) {
        checkForMapping(entity, getMappingName(entity));
    }

}
