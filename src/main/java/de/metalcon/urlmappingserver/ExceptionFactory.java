package de.metalcon.urlmappingserver;

import de.metalcon.domain.Muid;
import de.metalcon.domain.UidType;

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

}
