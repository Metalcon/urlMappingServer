package de.metalcon.urlmappingserver;

import java.io.File;
import java.io.IOException;

import net.hh.request_dispatcher.server.RequestHandler;

import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.zeromq.ZMQ;

import de.metalcon.api.responses.Response;
import de.metalcon.urlmappingserver.api.requests.UrlMappingRequest;
import de.metalcon.urlmappingserver.persistence.LevelDbStorage;
import de.metalcon.urlmappingserver.persistence.PersistentStorage;
import de.metalcon.zmqworker.Server;

/**
 * launcher for URL mapping server
 * 
 * @author sebschlicht
 * 
 */
public class UrlMappingServer extends Server<UrlMappingRequest> {

    /**
     * default value for configuration file path
     */
    protected static final String DEFAULT_CONFIG_PATH =
            "url-mapping-server-config.txt";

    /**
     * URL mapping server configuration
     */
    protected UrlMappingServerConfig config;

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

        new UrlMappingServer(config);
    }

    /**
     * creates and startes URL mapping server living in an own ZMQ context<br>
     * has its own ZMQ worker thread
     * 
     * @param config
     *            URL mapping server configuration object
     */
    public UrlMappingServer(
            UrlMappingServerConfig config) {
        super(config);

        // load database
        levelDb = loadDatabase(config.database_path);
        if (levelDb == null) {
            throw new IllegalStateException("failed to load database");
        }

        // initialize request handler
        PersistentStorage persistentStorage = new LevelDbStorage(levelDb);
        mappingManager = new EntityUrlMappingManager(persistentStorage);
        RequestHandler<UrlMappingRequest, Response> requestHandler =
                new UrlMappingRequestHandler(mappingManager);

        // start ZMQ communication
        start(requestHandler);
    }

    /**
     * creates and startes URL mapping server<br>
     * has its own ZMQ worker thread
     * 
     * @param config
     *            URL mapping server configuration object
     * @param context
     *            ZMQ context the server will live in
     */
    public UrlMappingServer(
            UrlMappingServerConfig config,
            ZMQ.Context context) {
        super(config, context);

        // load database
        levelDb = loadDatabase(config.database_path);
        if (levelDb == null) {
            throw new IllegalStateException("failed to load database");
        }

        // initialize request handler
        PersistentStorage persistentStorage = new LevelDbStorage(levelDb);
        mappingManager = new EntityUrlMappingManager(persistentStorage);
        RequestHandler<UrlMappingRequest, Response> requestHandler =
                new UrlMappingRequestHandler(mappingManager);

        // start ZMQ communication
        //        start(requestHandler);
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
     * stop server and close database if open
     */
    @Override
    public void close() {
        if (levelDb != null) {
            try {
                levelDb.close();
                levelDb = null;
                System.out.println("database shutted down");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("failed to close database");
            }
        }
        super.close();
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
