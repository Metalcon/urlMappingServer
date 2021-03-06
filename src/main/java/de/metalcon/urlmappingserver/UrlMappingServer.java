package de.metalcon.urlmappingserver;

import java.io.File;
import java.io.IOException;

import net.hh.request_dispatcher.RequestHandler;

import org.fusesource.leveldbjni.JniDBFactory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

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
            "/usr/share/metalcon/urlMappingServer/config.txt";

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
            LOG.info("using default configuration file path \""
                    + DEFAULT_CONFIG_PATH + "\"");
        }

        // load server configuration
        UrlMappingServerConfig config = new UrlMappingServerConfig(configPath);
        if (!config.isLoaded()) {
            LOG.error("failed to load configuration");
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
        if (start(requestHandler)) {
            LOG.info("URL mapping server online");
        } else {
            LOG.error("failed to start URL mapping server");
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
     * stop server and close database if open
     */
    @Override
    public void close() {
        if (levelDb != null) {
            try {
                levelDb.close();
                levelDb = null;
                LOG.info("database shutted down");
            } catch (IOException e) {
                e.printStackTrace();
                LOG.error("failed to close database");
            }
        }
        super.close();
    }

    protected static DB loadDatabase(String databasePath) {
        Options options = new Options();
        options.createIfMissing(true);
        try {
            LOG.info("loading database from path: " + databasePath);
            return JniDBFactory.factory.open(new File(databasePath), options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
