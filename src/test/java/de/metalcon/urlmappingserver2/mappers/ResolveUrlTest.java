package de.metalcon.urlmappingserver2.mappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver2.EntityUrlMapperTest;

public abstract class ResolveUrlTest extends EntityUrlMapperTest {

    @Test
    public void testNotRegistered() {
        entity = getEntityFull();
        assertNull(resolveUrl(entity.getMuid()));
    }

    @Test
    public void testShortestMappingName() {
        entity = getEntityFull();
        registerEntity(entity);
        checkMappingName(entity);
    }

    @Test
    public void testShortedMappingId() {
        entity = getEntityFull();
        registerEntity(entity);
        EntityUrlData identical = getEntityFull();
        registerEntity(identical);
        checkMappingId(identical);
        checkMappingName(entity);
    }

    protected void checkMappingId(EntityUrlData entity) {
        assertEquals(getMappingId(entity), resolveUrl(entity.getMuid()));
    }

    protected void checkMappingName(EntityUrlData entity) {
        assertEquals(getMappingName(entity), resolveUrl(entity.getMuid()));
    }

    protected String resolveUrl(Muid muid) {
        return mapper.resolveUrl(muid);
    }

}
