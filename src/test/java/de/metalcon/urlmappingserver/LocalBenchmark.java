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
        int totalWrites = 100000 * 10;
        int totalReads = totalWrites * 1;

        benchmarkWrite(totalWrites);
        benchmarkRead(totalReads);

        server.stop();
        server.cleanUp();

        // benchmark restart
        long crrNano = System.nanoTime();

        server = new UrlMappingServer(CONFIG);
        server.loadFromDatabase();

        crrNano = System.nanoTime() - crrNano;
        long crrMis = crrNano / 1000;
        long crrMs = crrMis / 1000;
        System.out.println("benchmark duration (restart): " + crrMs + "ms");
        System.out.println("per write: " + (crrMis / totalWrites) + "µs");
        System.out.println("writes per second: " + 1000
                / (crrMs / (double) totalWrites));

        mappingManager = server.getMappingManager();
        benchmarkRead(totalReads);

        server.stop();
        server.cleanUp();
        cleanUp();
    }

    protected void benchmarkWithoutPersistence() {
        mappingManager = new EntityUrlMappingManager();

        int totalWrites = 100000 * 5;
        int totalReads = totalWrites * 10;

        benchmarkWrite(totalWrites);
        benchmarkRead(totalReads);
        benchmarkRead(totalReads);

        server.stop();
        server.cleanUp();
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

    public static void main(String[] args) {
        //        new LocalBenchmark().benchmark();
        new LocalBenchmark().benchmarkWithoutPersistence();
    }

}
