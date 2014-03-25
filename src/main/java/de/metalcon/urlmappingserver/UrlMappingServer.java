package de.metalcon.urlmappingserver;

import org.zeromq.ZMQ;

import de.metalcon.zmqworker.ZMQRequestHandler;
import de.metalcon.zmqworker.ZMQWorker;

/**
 * launcher for URL mapping server
 * 
 * @author sebschlicht
 * 
 */
public class UrlMappingServer {

    /**
     * default value for configuration file path
     */
    protected static final String DEFAULT_CONFIG_PATH =
            "url-mapping-server-config.txt";

    /**
     * ZeroMQ context to get sockets from
     */
    protected static ZMQ.Context CONTEXT = null;

    /**
     * launch URL mapping server
     * 
     * @param args
     *            command line arguments
     *            <ul>
     *            <li>_configuration file path_</li>
     *            </ul>
     */
    public static void main(String[] args) {
        if (CONTEXT == null) {
            String configPath;
            if (args.length > 0) {
                configPath = args[0];
            } else {
                configPath = DEFAULT_CONFIG_PATH;
                System.out
                        .println("[INFO] using default configuration file path \""
                                + DEFAULT_CONFIG_PATH + "\"");
            }

            UrlMappingServerConfig config =
                    new UrlMappingServerConfig(configPath);
            if (!config.isLoaded()) {
                System.err.println("failed to load configuration");
                return;
            }

            System.out.println("ZMQ threads: " + config.num_zmq_threads);
            CONTEXT = ZMQ.context(config.num_zmq_threads);

            EntityUrlMappingManager mappingManager =
                    new EntityUrlMappingManager();
            ZMQRequestHandler requestHandler =
                    new UrlMappingRequestHandler(mappingManager);

            ZMQWorker worker =
                    new ZMQWorker(config.endpoint, requestHandler, CONTEXT);
            worker.start();
        } else {
            System.err.println("URL mapping server is running");
        }
    }

}
