package de.metalcon.urlmappingserver;

import java.util.Map.Entry;

import de.metalcon.domain.Muid;
import de.metalcon.domain.UidType;
import de.metalcon.urlmappingserver.api.exception.UnknownMuidException;
import de.metalcon.urlmappingserver.api.exception.UnknownUrlException;
import de.metalcon.urlmappingserver.api.exception.UrlMappingException;
import de.metalcon.urlmappingserver.api.requests.ResolveMuidRequest;
import de.metalcon.urlmappingserver.api.requests.ResolveUrlRequest;

/**
 * exception factory generating exceptions with useful error messages
 * 
 * @author sebschlicht
 * 
 */
public class ExceptionFactory {

    public static IllegalArgumentException usageWrongMapper(
            final UidType uidType,
            final UidType validType) {
        return new IllegalArgumentException("mapper handles muid type \""
                + validType + "\" only (was: \"" + uidType + "\")");
    }

    public static IllegalArgumentException usageEmptyMuidNotAllowed(
            final UidType type) {
        return new IllegalArgumentException("empty MUID is not allowed for "
                + type);
    }

    public static IllegalStateException
        internalUrlResolveFailed(final Muid muid) {
        return new IllegalStateException(
                "failed to resolve URL of registered MUID " + muid);
    }

    public static UrlMappingException
        usageUnknownUrl(ResolveUrlRequest request) {
        String url = "";
        for (Entry<String, String> pathVar : request.getUrlPathVars()
                .entrySet()) {
            if (url.length() > 0) {
                url += "\n";
            }
            url += pathVar.getKey() + " = " + pathVar.getValue();
        }

        throw new UnknownUrlException("failed to resolve URL for entity type "
                + request.getUidType() + ":\n" + url);
    }

    public static UrlMappingException usageUnknownMuid(
            ResolveMuidRequest request) {
        throw new UnknownMuidException("failed to resolve MUID \""
                + request.getMuid() + "\"");
    }
}
