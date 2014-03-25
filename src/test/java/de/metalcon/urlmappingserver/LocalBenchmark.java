package de.metalcon.urlmappingserver;

import java.util.Map;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public class LocalBenchmark extends Benchmark {

    protected UrlMappingServerConfig CONFIG = new UrlMappingServerConfig(
            "test.url-mapping-server-config.txt");

    protected UrlMappingServer server;

    protected EntityUrlMappingManager mappingManager;

    public LocalBenchmark() {
        server = new UrlMappingServer(CONFIG);
        mappingManager = server.getMappingManager();
    }

    @Override
    protected void benchmark() {
        benchmarkWrite(1000000);
        benchmarkRead(10000000);

        server.stop();
        server.cleanUp();

        server = new UrlMappingServer(CONFIG);
        server.loadFromDatabase();
        mappingManager = server.getMappingManager();

        benchmarkRead(10000000);
    }

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
