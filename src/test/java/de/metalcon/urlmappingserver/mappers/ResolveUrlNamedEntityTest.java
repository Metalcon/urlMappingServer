package de.metalcon.urlmappingserver.mappers;

import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public abstract class ResolveUrlNamedEntityTest extends ResolveUrlTest {

    /**
     * register entity so that main mapping is name mapping and check this
     */
    @Test
    public void testShortestMappingName() {
        entity = FACTORY.getEntityFull();
        registerEntity(entity);
        checkMappingName(entity);
    }

    @Override
    public void testShortestMappingId() {
        entity = FACTORY.getEntityFull();
        registerEntity(entity);

        EntityUrlData identical = FACTORY.getEntityIdentical(entity);
        mapper.registerMuid(identical);

        checkMappingName(entity);
        checkMappingId(identical);
    }

    @Override
    public void testMultipleRegistered() {
        for (int i = 0; i < 10; i++) {
            entity = FACTORY.getEntityFull();
            registerEntity(entity);
            checkMappingName(entity);
        }
    }

    /**
     * check if main mapping for entity is name mapping
     */
    protected void checkMappingName(EntityUrlData entity) {
        checkMainMapping(entity, FACTORY.getMappingName(entity));
    }

}
