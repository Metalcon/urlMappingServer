package de.metalcon.urlmappingserver;

import java.io.Serializable;

import de.metalcon.urlmappingserver.api.requests.UrlMappingRegistrationRequest;
import de.metalcon.zmqworker.ZMQRequestHandler;

public class UrlMappingRequestHandler implements ZMQRequestHandler {

    private EntityUrlMappingManager urlMappingManager;

    public UrlMappingRequestHandler(
            EntityUrlMappingManager urlMappingManager) {
        this.urlMappingManager = urlMappingManager;
    }

    @Override
    public Serializable handleRequest(Object request) {
        if (request instanceof UrlMappingRegistrationRequest) {
            UrlMappingRegistrationRequest registrationRequest =
                    (UrlMappingRegistrationRequest) request;
            urlMappingManager.registerMuid(registrationRequest.getUrlData());

            return "OK.";
        }

        return null;
    }

}
