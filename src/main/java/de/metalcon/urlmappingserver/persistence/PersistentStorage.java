package de.metalcon.urlmappingserver.persistence;

import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

/**
 * interface for underlying persistent storage
 * 
 * @author sebschlicht
 * 
 */
public interface PersistentStorage {

    void saveMapping(EntityUrlData entity, String mapping);

    UrlMappingData restoreMappings();

}
