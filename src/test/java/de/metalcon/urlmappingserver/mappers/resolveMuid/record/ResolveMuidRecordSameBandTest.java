package de.metalcon.urlmappingserver.mappers.resolveMuid.record;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;

import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver.mappers.resolveMuid.ResolveMuidRecordTest;

public class ResolveMuidRecordSameBandTest extends ResolveMuidRecordTest {

    protected static BandUrlData BAND;

    @BeforeClass
    public static void beforeClass() {
        RECORD_FACTORY.setUseSameParent(true);
        RECORD_FACTORY.setUseEmptyParent(false);
    }

    @Override
    protected void registerEntity(EntityUrlData entity) {
        super.registerEntity(entity);

        // bands equal always
        RecordUrlData record = (RecordUrlData) entity;
        if (BAND == null) {
            BAND = record.getBand();
        }
        assertEquals(BAND, record.getBand());
    }

}
