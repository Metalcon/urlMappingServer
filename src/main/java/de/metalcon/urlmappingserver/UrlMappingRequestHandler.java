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

    private final EntityUrlMappingManager urlMappingManager;

    public UrlMappingRequestHandler(
            final EntityUrlMappingManager urlMappingManager) {
        this.urlMappingManager = urlMappingManager;
    }

    @Override
    public Response handleRequest(final UrlMappingRequest request) {
        if (request instanceof UrlMappingRegistrationRequest) {
            return handleRegistrationRequest((UrlMappingRegistrationRequest) request);
        } else if (request instanceof UrlMappingResolveRequest) {
            return handleResolveRequest((UrlMappingResolveRequest) request);
        }

        return new UsageErrorResponse("unknown request type",
                "use requests defined in URL mapping server API");
    }

    private Response handleRegistrationRequest(
            final UrlMappingRegistrationRequest request) {
        EntityUrlData urlData = request.getUrlData();

        try {
            urlMappingManager.registerMuid(urlData);
        } catch (MetalconRuntimeException e) {
            return new UsageErrorResponse("unknown entity type \""
                    + urlData.getMuid().getType() + "\"", "use a valid MUID");
        }

        return new SuccessResponse();
    }

    private Response
        handleResolveRequest(final UrlMappingResolveRequest request) {
        Muid muid =
                urlMappingManager.resolveMuid(request.getUrlPathVars(),
                        request.getUidType());

        if (muid != null) {
            return new MuidResolvedResponse(muid);
        }
        return new UnknownMuidResponse();
    }

}
