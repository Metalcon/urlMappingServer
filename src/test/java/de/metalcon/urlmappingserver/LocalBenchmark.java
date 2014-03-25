package de.metalcon.urlmappingserver;

import java.util.Map;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public class LocalBenchmark extends Benchmark {

    protected EntityUrlMappingManager manager;

    public LocalBenchmark() {
        manager = new EntityUrlMappingManager();
    }

    @Override
    protected void registerMuid(EntityUrlData entity) {
        manager.registerMuid(entity);
    }

    @Override
    protected Muid resolveMuid(
            Map<String, String> urlPathVars,
            MuidType muidType) {
        return manager.resolveMuid(urlPathVars, muidType);
    }

}
