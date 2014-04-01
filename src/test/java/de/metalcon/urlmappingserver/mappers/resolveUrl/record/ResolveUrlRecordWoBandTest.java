package de.metalcon.urlmappingserver.mappers.resolveUrl.record;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver.mappers.resolveUrl.ResolveUrlRecordTest;

public class ResolveUrlRecordWoBandTest extends ResolveUrlRecordTest {

    @BeforeClass
    public static void beforeClass() {
        RECORD_FACTORY.setUseSameParent(false);
        RECORD_FACTORY.setUseEmptyParent(true);
    }

    @Override
    protected void registerEntity(EntityUrlData entity) {
        super.registerEntity(entity);

        // band is empty
        RecordUrlData record = (RecordUrlData) entity;
        assertTrue(record.getBand().hasEmptyMuid());
    }

}
