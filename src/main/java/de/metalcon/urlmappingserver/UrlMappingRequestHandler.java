package de.metalcon.urlmappingserver;

import net.hh.request_dispatcher.server.RequestHandler;
import de.metalcon.api.responses.Response;
import de.metalcon.api.responses.SuccessResponse;
import de.metalcon.api.responses.errors.UsageErrorResponse;
import de.metalcon.domain.Muid;
import de.metalcon.exceptions.MetalconRuntimeException;
import de.metalcon.urlmappingserver.api.requests.UrlMappingRegistrationRequest;
import de.metalcon.urlmappingserver.api.requests.UrlMappingRequest;
import de.metalcon.urlmappingserver.api.requests.UrlMappingResolveRequest;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.responses.MuidResolvedResponse;
import de.metalcon.urlmappingserver.api.responses.UnknownMuidResponse;

public class UrlMappingRequestHandler implements
        RequestHandler<UrlMappingRequest, Response> {

    private static final long serialVersionUID = -6731628161635337159L;

    private EntityUrlMappingManager urlMappingManager;

    public UrlMappingRequestHandler(
            EntityUrlMappingManager urlMappingManager) {
        this.urlMappingManager = urlMappingManager;
    }

    @Override
    public Response handleRequest(UrlMappingRequest request) {
        if (request instanceof UrlMappingRegistrationRequest) {
            return handleRegistrationRequest((UrlMappingRegistrationRequest) request);
        } else if (request instanceof UrlMappingResolveRequest) {
            return handleResolveRequest((UrlMappingResolveRequest) request);
        }

        return new UsageErrorResponse("unknown request type",
                "use requests defined in URL mapping server API");
    }

    private Response handleRegistrationRequest(
            UrlMappingRegistrationRequest request) {
        EntityUrlData urlData = request.getUrlData();

        try {
            urlMappingManager.registerMuid(urlData);
        } catch (MetalconRuntimeException e) {
            return new UsageErrorResponse("unknown entity type \""
                    + urlData.getMuid().getMuidType() + "\"",
                    "use a valid MUID");
        }

        return new SuccessResponse();
    }

    private Response handleResolveRequest(UrlMappingResolveRequest request) {
        Muid muid =
                urlMappingManager.resolveMuid(request.getUrlPathVars(),
                        request.getMuidType());

        if (muid != null) {
            return new MuidResolvedResponse(muid);
        }
        return new UnknownMuidResponse();
    }

}
