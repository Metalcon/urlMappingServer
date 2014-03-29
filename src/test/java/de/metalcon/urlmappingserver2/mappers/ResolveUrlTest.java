package de.metalcon.urlmappingserver2.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver2.EntityUrlMapperTest;

public abstract class ResolveUrlTest extends EntityUrlMapperTest {

    /**
     * no mapping for entity available if not registered yet
     */
    @Test
    public void testNotRegistered() {
        entity = getEntityFull();
        assertNull(resolveUrl(entity));
    }

    /**
     * register entity so that main mapping is ID mapping and check this
     */
    @Test
    public void testShortestMappingId() {
        entity = getEntityFull();
        registerEntity(entity);
        checkMappingId(entity);
    }

    /**
     * check if main mapping for entity is ID mapping
     */
    protected void checkMappingId(EntityUrlData entity) {
        assertEquals(getMappingId(entity), resolveUrl(entity));
    }

    protected String resolveUrl(EntityUrlData entity) {
        return mapper.resolveUrl(entity.getMuid());
    }

}
