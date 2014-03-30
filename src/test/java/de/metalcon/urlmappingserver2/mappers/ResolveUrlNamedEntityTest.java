package de.metalcon.urlmappingserver2.mappers;

import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public abstract class ResolveUrlNamedEntityTest extends ResolveUrlTest {

    /**
     * register entity so that main mapping is name mapping and check this
     */
    @Test
    public void testShortestMappingName() {
        entity = getEntityFull();
        registerEntity(entity);
        checkMappingName(entity);
    }

    @Override
    public void testShortestMappingId() {
        entity = getEntityFull();
        registerEntity(entity);
        checkMappingName(entity);

        EntityUrlData identical = getEntityFull();
        registerEntity(identical);
        checkMappingId(identical);
    }

    /**
     * check if main mapping for entity is name mapping
     */
    protected void checkMappingName(EntityUrlData entity) {
        checkMainMapping(entity, getMappingName(entity));
    }

}
