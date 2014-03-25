package de.metalcon.urlmappingserver;

import java.io.File;
import java.io.IOException;

import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
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
            // get configuration path
            String configPath;
            if (args.length > 0) {
                configPath = args[0];
            } else {
                configPath = DEFAULT_CONFIG_PATH;
                System.out
                        .println("[INFO] using default configuration file path \""
                                + DEFAULT_CONFIG_PATH + "\"");
            }

            // load server configuration
            UrlMappingServerConfig config =
                    new UrlMappingServerConfig(configPath);
            if (!config.isLoaded()) {
                System.err.println("failed to load configuration");
                return;
            }

            // initialize ZMQ context
            CONTEXT = initZmqContext(config.num_zmq_threads);

            // load database
            DB levelDb = loadDatabase(config.database_path);
            if (levelDb == null) {
                System.err.println("failed to load database");
                return;
            }

            // initialize request handler
            EntityUrlMappingManager mappingManager =
                    new EntityUrlMappingManager();
            ZMQRequestHandler requestHandler =
                    new UrlMappingRequestHandler(mappingManager);

            // start ZMQ communication
            ZMQWorker worker =
                    new ZMQWorker(config.endpoint, requestHandler, CONTEXT);
            worker.start();

            // TODO: wait for shutdown

            try {
                levelDb.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("failed to close database");
            }
        } else {
            System.err.println("URL mapping server is running");
        }
    }

    protected static ZMQ.Context initZmqContext(int numThreads) {
        System.out.println("ZMQ threads: " + numThreads);
        return ZMQ.context(numThreads);
    }

    protected static DB loadDatabase(String databasePath) {
        Options options = new Options();
        options.createIfMissing(true);
        try {
            System.out.println("loading database from path: " + databasePath);
            return JniDBFactory.factory.open(new File(databasePath), options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
