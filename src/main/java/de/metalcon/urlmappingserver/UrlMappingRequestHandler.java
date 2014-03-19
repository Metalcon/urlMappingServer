package de.metalcon.urlmappingserver;

import de.metalcon.exceptions.MetalconRuntimeException;
import de.metalcon.urlmappingserver.api.requests.UrlMappingRegistrationRequest;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.zmqworker.ZMQRequestHandler;
import de.metalcon.zmqworker.responses.Response;
import de.metalcon.zmqworker.responses.SuccessResponse;
import de.metalcon.zmqworker.responses.errors.UsageErrorResponse;

public class UrlMappingRequestHandler implements ZMQRequestHandler {

    private EntityUrlMappingManager urlMappingManager;

    public UrlMappingRequestHandler(
            EntityUrlMappingManager urlMappingManager) {
        this.urlMappingManager = urlMappingManager;
    }

    @Override
    public Response handleRequest(Object request) {
        return new SuccessResponse();

        //        if (request instanceof UrlMappingRegistrationRequest) {
        //            return handleRegistrationRequest((UrlMappingRegistrationRequest) request);
        //        } else if (request instanceof UrlMappingResolveRequest) {
        //            UrlMappingResolveRequest resolveRequest =
        //                    (UrlMappingResolveRequest) request;
        //            Muid muid =
        //                    urlMappingManager.resolveMuid(
        //                            resolveRequest.getUrlPathVars(),
        //                            resolveRequest.getEntityType());
        //
        //            return new SuccessResponse();
        //        }
        //
        //        return null;
    }

    private Response handleRegistrationRequest(
            UrlMappingRegistrationRequest request) {
        EntityUrlData urlData = request.getUrlData();

        try {
            urlMappingManager.registerMuid(urlData);
        } catch (MetalconRuntimeException e) {
            return new UsageErrorResponse("unknown entity type \""
                    + urlData.getMuid().getEntityType() + "\"",
                    "use a valid MUID");
        }

        return new SuccessResponse();
    }
}
