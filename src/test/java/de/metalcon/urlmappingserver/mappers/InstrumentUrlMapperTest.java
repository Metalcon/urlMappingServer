package de.metalcon.urlmappingserver.mappers;

import org.junit.BeforeClass;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.InstrumentUrlData;

public class InstrumentUrlMapperTest extends EntityUrlMapperTest {

    protected static final InstrumentUrlData INSTRUMENT =
            new InstrumentUrlData(Muid.create(MuidType.INSTRUMENT), VALID_NAME);

    protected static final InstrumentUrlData SIMILAR_INSTRUMENT =
            new InstrumentUrlData(Muid.create(MuidType.INSTRUMENT),
                    INSTRUMENT.getName());

    @BeforeClass
    public static void beforeClass() {
        ENTITY = INSTRUMENT;
        SIMILAR_ENTITY = SIMILAR_INSTRUMENT;
        EntityUrlMapperTest.beforeClass();
    }

}
