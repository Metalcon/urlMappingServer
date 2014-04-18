package de.metalcon.urlmappingserver;

import de.metalcon.utils.Config;
import de.metalcon.zmqworker.ZmqConfig;

/**
 * configuration object for URL mapping server
 * 
 * @author sebschlicht
 * 
 */
public class UrlMappingServerConfig extends Config implements ZmqConfig {

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
     * database path for levelDB
     */
    public String database_path;

    @Override
    public String getEndpoint() {
        return endpoint;
    }

}
