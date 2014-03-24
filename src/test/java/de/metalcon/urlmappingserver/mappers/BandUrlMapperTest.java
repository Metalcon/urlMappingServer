package de.metalcon.urlmappingserver.mappers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;

public class BandUrlMapperTest extends EntityUrlMapperTest {

    protected static final BandUrlData BAND = new BandUrlData(
            Muid.create(MuidType.BAND), VALID_NAME);

    protected static final BandUrlData SIMILAR_BAND = new BandUrlData(
            Muid.create(MuidType.BAND), BAND.getName());

    @BeforeClass
    public static void beforeClass() {
        ENTITY = BAND;
        SIMILAR_ENTITY = SIMILAR_BAND;
        EntityUrlMapperTest.beforeClass();
    }

    @Test
    public void testRegistrationNoBand() {
        // this is never called directly via API but indirectly via record/track mapper
        assertNull(resolveMapping(EMPTY_ENTITY));
        mapper.registerMuid(new BandUrlData());
        assertNotNull(resolveMapping(EMPTY_ENTITY));
    }

}