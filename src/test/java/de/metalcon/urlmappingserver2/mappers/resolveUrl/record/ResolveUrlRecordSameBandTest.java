package de.metalcon.urlmappingserver2.mappers.resolveUrl.record;

import org.junit.BeforeClass;

import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver2.mappers.resolveUrl.ResolveUrlRecordTest;

public class ResolveUrlRecordSameBandTest extends ResolveUrlRecordTest {

    protected static BandUrlData BAND;

    @BeforeClass
    public static void beforeClass() {
        RECORD_FACTORY.setUseSameParent(true);
        RECORD_FACTORY.setUseEmptyParent(false);
    }

    @Override
    public void testShortestMappingReleaseYear() {
        record = (RecordUrlData) RECORD_FACTORY.getEntityFull();
        registerEntity(record);

        RecordUrlData recordWithSameName =
                RECORD_FACTORY.getRecordSameName(record);
        registerEntity(recordWithSameName);

        checkMappingName(record);
        checkMappingReleaseYear(recordWithSameName);
    }

}
