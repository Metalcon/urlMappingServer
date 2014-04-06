package de.metalcon.urlmappingserver;

import static org.junit.Assert.fail;

import java.util.Map;

import net.hh.request_dispatcher.Callback;
import net.hh.request_dispatcher.Dispatcher;
import net.hh.request_dispatcher.service_adapter.ZmqAdapter;

import org.zeromq.ZMQ;

import de.metalcon.api.responses.Response;
import de.metalcon.api.responses.errors.ErrorResponse;
import de.metalcon.domain.Muid;
import de.metalcon.domain.UidType;
import de.metalcon.urlmappingserver.api.requests.UrlMappingRegistrationRequest;
import de.metalcon.urlmappingserver.api.requests.UrlMappingRequest;
import de.metalcon.urlmappingserver.api.requests.UrlMappingResolveRequest;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.responses.MuidResolvedResponse;

public class ZmqBenchmark extends Benchmark {

    protected ZMQ.Context context;

    protected Dispatcher dispatcher;

    public ZmqBenchmark() {
        context = ZMQ.context(CONFIG.getNumIOThreads());

        CONFIG.endpoint = "inproc:///tmp/zmqWorker";
        server = new UrlMappingServer(CONFIG, context);

        dispatcher = new Dispatcher();
        ZmqAdapter<UrlMappingRequest, Response> adapter =
                new ZmqAdapter<UrlMappingRequest, Response>(context, server
                        .getConfig().getEndpoint());
        dispatcher.registerServiceAdapter("URL_MAPPING", adapter);
        dispatcher.setDefaultService(UrlMappingRegistrationRequest.class,
                "URL_MAPPING");
        dispatcher.setDefaultService(UrlMappingResolveRequest.class,
                "URL_MAPPING");
        System.out.println("dispatcher sending to "
                + server.getConfig().getEndpoint());
    }

    @Override
    protected void registerMuid(final EntityUrlData entity) {
        dispatcher.execute(new UrlMappingRegistrationRequest(entity),
                new Callback<Response>() {

                    @Override
                    public void onSuccess(Response response) {
                        if (response instanceof ErrorResponse) {
                            fail("failed to register "
                                    + entity.getMuid().getType() + " \""
                                    + entity.getName() + "\" ("
                                    + entity.getMuid() + ")");
                        }
                    }

                });
        dispatcher.gatherResults();
    }

    @Override
    protected Muid resolveMuid(
            final Map<String, String> urlPathVars,
            final UidType muidType) {
        final Muid[] muid = new Muid[1];
        dispatcher.execute(new UrlMappingResolveRequest(urlPathVars, muidType),
                new Callback<Response>() {

                    @Override
                    public void onSuccess(Response response) {
                        if (response instanceof MuidResolvedResponse) {
                            muid[0] =
                                    ((MuidResolvedResponse) response).getMuid();
                        } else {
                            fail("failed to resolve " + muidType);
                        }
                    }
                });
        dispatcher.gatherResults();
        return muid[0];
    }

    @Override
    protected void benchmark(int numWrites, int numReads) {
        super.benchmark(numWrites, numReads);
        benchmarkRead(numReads);
        cleanUp();
    }

    @Override
    protected void cleanUp() {
        dispatcher.close();
        context.close();
        super.cleanUp();
    }

    public static void main(String[] args) {
        int numWrites = 1000;
        int numReads = numWrites * 10;

        ZmqBenchmark benchmark = new ZmqBenchmark();
        benchmark.benchmark(numWrites, numReads);
    }

}
