package de.metalcon.urlmappingserver;

import java.util.Map;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public class ZmqBenchmark extends Benchmark {

    protected UrlMappingServer server;

    protected EntityUrlMappingManager mappingManager;

    @Override
    protected void registerMuid(EntityUrlData entity) {
        mappingManager.registerMuid(entity);
    }

    @Override
    protected Muid resolveMuid(
            Map<String, String> urlPathVars,
            MuidType muidType) {
        return mappingManager.resolveMuid(urlPathVars, muidType);
    }

}
