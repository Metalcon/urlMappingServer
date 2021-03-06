package de.metalcon.urlmappingserver.persistence;

/**
 * interface for underlying persistent storage
 * 
 * @author sebschlicht
 * 
 */
public interface PersistentStorage {

    void saveMapping(
            short muidType,
            long muidValue,
            String mapping,
            long muidParentValue);

    void saveParent(long muidTrackValue, long muidBandValue);

    UrlMappingPersistenceData restoreMappings();

}
