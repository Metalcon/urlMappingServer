package de.metalcon.urlmappingserver;

public class LocalBenchmark extends Benchmark {

    @Override
    protected void benchmark(int numWrites, int numReads) {
        super.benchmark(numWrites, numReads);
        cleanUp();

        // benchmark restart
        long crrNano = System.nanoTime();

        server = new UrlMappingServer(CONFIG);
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
        cleanUp();
    }

    protected void benchmarkWithoutPersistence(int numWrites, int numReads) {
        super.benchmark(numWrites, numReads);
        benchmarkRead(numReads);
        cleanUp();
    }

    public static void main(String[] args) {
        int numWrites = 100000 * 1;
        int numReads = numWrites * 10;

        new LocalBenchmark().benchmark(numWrites, numReads);
        //        new LocalBenchmark().benchmarkWithoutPersistence(numWrites, numReads);
    }

}
