package de.metalcon.urlmappingserver;

import java.util.Map;

import de.metalcon.domain.EntityType;
import de.metalcon.domain.Muid;
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
     * @throws RedirectException
     *             TODO
     */
    Muid resolveMuid(Map<String, String> url, EntityType type);

}
