package de.metalcon.urlmappingserver2.mappers;

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
        registerEntity(identical);

        checkMappingName(entity);
        checkMappingId(identical);
    }

    /**
     * check if main mapping for entity is name mapping
     */
    protected void checkMappingName(EntityUrlData entity) {
        checkMainMapping(entity, FACTORY.getMappingName(entity));
    }

}
