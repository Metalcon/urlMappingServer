package de.metalcon.urlmappingserver;

import de.metalcon.utils.Config;

/**
 * configuration object for URL mapping server
 * 
 * @author sebschlicht
 * 
 */
public class UrlMappingServerConfig extends Config {

    private static final long serialVersionUID = 8052135476445780765L;

    /**
     * load URL mapping server configuration
     * 
     * @param configPath
     *            path to configuration file
     */
    public UrlMappingServerConfig(
            String configPath) {
        super(configPath);
    }

    /**
     * endpoint the server will listen on
     */
    public String endpoint;

    /**
     * number of ZMQ threads
     */
    public int num_zmq_threads;

    /**
     * database path for levelDB
     */
    public String database_path;

}
