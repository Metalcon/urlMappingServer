package de.metalcon.urlmappingserver;

import net.hh.request_dispatcher.RequestHandler;

import org.apache.log4j.Logger;

import de.metalcon.api.responses.Response;
import de.metalcon.api.responses.SuccessResponse;
import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.api.requests.ResolveMuidRequest;
import de.metalcon.urlmappingserver.api.requests.ResolveUrlRequest;
import de.metalcon.urlmappingserver.api.requests.UrlMappingRequest;
import de.metalcon.urlmappingserver.api.requests.UrlRegistrationRequest;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.responses.MuidResolvedResponse;
import de.metalcon.urlmappingserver.api.responses.UrlResolvedResponse;

public class UrlMappingRequestHandler implements
        RequestHandler<UrlMappingRequest, Response> {

    private static final long serialVersionUID = -6731628161635337159L;

    /**
     * request handler log
     */
    protected static Logger LOG = Logger
            .getLogger(UrlMappingRequestHandler.class);

    private final EntityUrlMappingManager urlMappingManager;

    public UrlMappingRequestHandler(
            final EntityUrlMappingManager urlMappingManager) {
        this.urlMappingManager = urlMappingManager;
    }

    @Override
    public Response handleRequest(final UrlMappingRequest request) {
        System.out.println("received request: " + request.toString());
        if (request instanceof UrlRegistrationRequest) {
            return handleRegistrationRequest((UrlRegistrationRequest) request);
        } else if (request instanceof ResolveUrlRequest) {
            return handleUrlResolveRequest((ResolveUrlRequest) request);
        } else if (request instanceof ResolveMuidRequest) {
            return handleMuidResolveRequest((ResolveMuidRequest) request);
        }

        throw new IllegalArgumentException("unknown request type \""
                + request.getClass().getName() + "\"\n"
                + "use requests defined in URL mapping server API");
    }

    private Response handleRegistrationRequest(
            final UrlRegistrationRequest request) {
        EntityUrlData urlData = request.getUrlData();

        try {
            urlMappingManager.registerMuid(urlData);
            LOG.debug("registration request:" + urlData.getName());
            return new SuccessResponse();
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }

    private Response handleUrlResolveRequest(final ResolveUrlRequest request) {
        Muid muid =
                urlMappingManager.resolveMuid(request.getUrlPathVars(),
                        request.getUidType());
        if (muid != null) {
            return new UrlResolvedResponse(muid);
        }

        throw ExceptionFactory.usageUnknownUrl(request);
    }

    private Response handleMuidResolveRequest(final ResolveMuidRequest request) {
        String url = urlMappingManager.resolveUrl(request.getMuid());
        if (url != null) {
            return new MuidResolvedResponse(url);
        }

        throw ExceptionFactory.usageUnknownMuid(request);
    }

}
