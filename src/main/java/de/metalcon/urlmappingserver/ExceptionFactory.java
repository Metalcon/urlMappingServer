package de.metalcon.urlmappingserver;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;

/**
 * exception factory generating exceptions with useful error messages
 * 
 * @author sebschlicht
 * 
 */
public class ExceptionFactory {

    public static IllegalArgumentException usageWrongMapper(
            MuidType muidType,
            MuidType validType) {
        return new IllegalArgumentException("mapper handles muid type \""
                + validType + "\" only (was: \"" + muidType + "\")");
    }

    public static IllegalStateException internalUrlResolveFailed(Muid muid) {
        return new IllegalStateException(
                "failed to resolve URL of registered MUID " + muid);
    }

}
