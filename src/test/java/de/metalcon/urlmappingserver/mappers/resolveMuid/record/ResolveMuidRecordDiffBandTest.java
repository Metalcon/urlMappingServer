package de.metalcon.urlmappingserver.mappers.resolveMuid.record;

import static org.junit.Assert.assertNotSame;

import org.junit.BeforeClass;

import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver.mappers.resolveMuid.ResolveMuidRecordTest;

public class ResolveMuidRecordDiffBandTest extends ResolveMuidRecordTest {

    protected static BandUrlData BAND;

    @BeforeClass
    public static void beforeClass() {
        RECORD_FACTORY.setUseSameParent(false);
        RECORD_FACTORY.setUseEmptyParent(false);
    }

    @Override
    protected void registerEntity(EntityUrlData entity) {
        super.registerEntity(entity);

        // bands never equal
        RecordUrlData record = (RecordUrlData) entity;
        assertNotSame(BAND, record.getBand());
    }

}
