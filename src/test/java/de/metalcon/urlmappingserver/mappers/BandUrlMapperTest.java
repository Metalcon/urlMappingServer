package de.metalcon.urlmappingserver.mappers;

import org.junit.BeforeClass;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;

public class BandUrlMapperTest extends EntityUrlMapperTest {

    public static final BandUrlData BAND = new BandUrlData(
            Muid.create(MuidType.BAND), VALID_NAME);

    @BeforeClass
    public static void beforeClass() {
        MUID_TYPE = MuidType.BAND;
        ENTITY = BAND;
    }

}
