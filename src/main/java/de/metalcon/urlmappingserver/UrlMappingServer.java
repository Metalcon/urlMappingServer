package de.metalcon.urlmappingserver;

import java.io.File;
import java.io.IOException;

import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.zeromq.ZContext;

import de.metalcon.urlmappingserver.persistence.LevelDbStorage;
import de.metalcon.urlmappingserver.persistence.PersistentStorage;
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
    protected static ZContext CONTEXT = null;

    /**
     * ZeroMQ worker listening for requests
     */
    protected ZMQWorker worker;

    /**
     * levelDB to make mappings persistent
     */
    protected DB levelDb;

    /**
     * URL mapping manager
     */
    protected EntityUrlMappingManager mappingManager;

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
        UrlMappingServerConfig config = new UrlMappingServerConfig(configPath);
        if (!config.isLoaded()) {
            System.err.println("failed to load configuration");
            return;
        }

        UrlMappingServer server = new UrlMappingServer(config);
        server.waitForShutdown();
        server.cleanUp();
    }

    public UrlMappingServer(
            UrlMappingServerConfig config) {
        if (CONTEXT != null) {
            throw new IllegalStateException("URL mapping server is running");
        }

        // initialize ZMQ context
        CONTEXT = initZmqContext(config.num_zmq_threads);

        // load database
        levelDb = loadDatabase(config.database_path);
        if (levelDb == null) {
            System.err.println("failed to load database");
            return;
        }

        // initialize request handler
        PersistentStorage persistentStorage = new LevelDbStorage(levelDb);
        mappingManager = new EntityUrlMappingManager(persistentStorage);
        ZMQRequestHandler requestHandler =
                new UrlMappingRequestHandler(mappingManager);

        // start ZMQ communication
        worker = new ZMQWorker(config.endpoint, requestHandler, CONTEXT);
        if (!worker.start()) {
            throw new IllegalStateException("failed to start worker");
        }
    }

    /**
     * load all mappings from persistence database
     */
    public void loadFromDatabase() {
        mappingManager.loadFromDatabase();
    }

    /**
     * @return URL mapping manager
     */
    public EntityUrlMappingManager getMappingManager() {
        return mappingManager;
    }

    /**
     * stop the server and wait for its shutdown
     */
    public void stop() {
        worker.stop();
    }

    /**
     * wait for the server to shutdown
     */
    public void waitForShutdown() {
        worker.waitForShutdown();
    }

    /**
     * clean up server after shutdown
     */
    public void cleanUp() {
        try {
            levelDb.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("failed to close database");
        }
        CONTEXT = null;
    }

    protected static ZContext initZmqContext(int numThreads) {
        System.out.println("ZMQ threads: " + numThreads);
        return new ZContext(numThreads);
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
