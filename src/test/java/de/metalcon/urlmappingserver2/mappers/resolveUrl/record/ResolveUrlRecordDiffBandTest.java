package de.metalcon.urlmappingserver2.mappers.resolveUrl.record;

import static org.junit.Assert.assertNotSame;

import org.junit.BeforeClass;

import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver2.mappers.resolveUrl.ResolveUrlRecordTest;

public class ResolveUrlRecordDiffBandTest extends ResolveUrlRecordTest {

    protected static BandUrlData BAND;

    @BeforeClass
    public static void beforeClass() {
        RECORD_FACTORY.setUseSameParent(false);
        RECORD_FACTORY.setUseEmptyParent(false);
    }

    @Override
    public void testShortestMappingReleaseYear() {
        // will not happen
    }

    @Override
    protected void registerEntity(EntityUrlData entity) {
        super.registerEntity(entity);

        // bands never equal
        RecordUrlData record = (RecordUrlData) entity;
        assertNotSame(BAND, record.getBand());
    }

}
