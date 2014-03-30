package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.RecordFactory;

public class ResolveMuidRecordTest extends ResolveMuidNamedEntityTest {

    protected static RecordFactory RECORD_FACTORY = new RecordFactory(
            ResolveMuidBandTest.BAND_FACTORY);

    @Test
    public void testEmptyMuid() {
        entity = RECORD_FACTORY.getRecordWoMuid();
        mapper.registerMuid(entity);
        checkForMapping(entity, EMPTY_ENTITY);
    }

    @Override
    protected EntityFactory getFactory() {
        return RECORD_FACTORY;
    }

    @Override
    protected Map<String, String> getUrl(EntityUrlData entity, String mapping) {
        RecordUrlData record = (RecordUrlData) entity;
        BandUrlData band = record.getBand();

        Map<String, String> url = new HashMap<String, String>();

        url.put(manager.getMapper(MuidType.BAND).getUrlPathVarName(),
                getMappingId(band));
        url.put(mapper.getUrlPathVarName(), mapping);

        return url;
    }

}
