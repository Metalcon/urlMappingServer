package de.metalcon.urlmappingserver;

import java.util.Map;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public class LocalBenchmark extends Benchmark {

    protected EntityUrlMappingManager mappingManager;

    public LocalBenchmark() {
        server = new UrlMappingServer(CONFIG, null);
        mappingManager = server.getMappingManager();
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

    protected void loadFromDatabase(int numWrites, int numReads) {
        // benchmark restart
        long crrNano = System.nanoTime();

        server = new UrlMappingServer(CONFIG, null);
        server.loadFromDatabase();

        crrNano = System.nanoTime() - crrNano;
        long crrMis = crrNano / 1000;
        long crrMs = crrMis / 1000;
        System.out.println("benchmark duration (restart): " + crrMs + "ms");
        System.out.println("per write: " + (crrMis / numWrites) + "µs");
        System.out.println("writes per second: " + 1000
                / (crrMs / (double) numWrites));

        mappingManager = server.getMappingManager();
        benchmarkRead(numReads);
        benchmarkRead(numReads);
    }

    protected void benchmarkWithoutPersistence(int numWrites, int numReads) {
        super.benchmark(numWrites, numReads);
        benchmarkRead(numReads);
        cleanUp();
    }

    public static void main(String[] args) {
        int numWrites = 100000 * 5;
        int numReads = numWrites * 10;

        LocalBenchmark benchmark = new LocalBenchmark();
        benchmark.benchmark(numWrites, numReads);
        benchmark.server.close();

        benchmark.loadFromDatabase(numWrites, numReads);
        //        new LocalBenchmark().benchmark(numWrites, numReads);
        //        new LocalBenchmark().benchmarkWithoutPersistence(numWrites, numReads);
        benchmark.server.close();
    }

}
