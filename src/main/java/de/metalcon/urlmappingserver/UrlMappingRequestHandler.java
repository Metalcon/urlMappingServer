package de.metalcon.urlmappingserver;

import java.io.Serializable;

import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.api.requests.UrlMappingRegistrationRequest;
import de.metalcon.urlmappingserver.api.requests.UrlMappingResolveRequest;
import de.metalcon.zmqworker.ZMQRequestHandler;
import de.metalcon.zmqworker.responses.SuccessResponse;

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
        } else if (request instanceof UrlMappingResolveRequest) {
            UrlMappingResolveRequest resolveRequest =
                    (UrlMappingResolveRequest) request;
            Muid muid =
                    urlMappingManager.resolveMuid(
                            resolveRequest.getUrlPathVars(),
                            resolveRequest.getEntityType());

            return new SuccessResponse();
        }

        return null;
    }

}
