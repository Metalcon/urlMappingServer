package de.metalcon.urlmappingserver;

import java.util.Map;

import de.metalcon.domain.Muid;
import de.metalcon.domain.UidType;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

/**
 * interface for URL mappers
 * 
 * @author sebschlicht
 * 
 */
public interface MetalconUrlMapper {

    /**
     * register an URL for a MUID
     * 
     * @param urlData
     *            URL data needed to register the MUID
     */
    void registerMuid(EntityUrlData urlData);

    /**
     * resolve a MUID via the URL it was registered to
     * 
     * @param url
     *            registered URL
     * @param type
     *            entity type matching to the MUID
     * @return MUID registered for the URL<br>
     *         <b>null</b> if none registered
     */
    Muid resolveMuid(Map<String, String> url, UidType type);

    /**
     * resolve an URL for a MUID
     * 
     * @param muid
     *            MUID to get the URL for
     * @return URL registered for this MUID
     */
    String resolveUrl(Muid muid);

}
