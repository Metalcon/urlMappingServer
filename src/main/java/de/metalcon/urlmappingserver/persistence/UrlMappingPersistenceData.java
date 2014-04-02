package de.metalcon.urlmappingserver.persistence;

import java.util.Map;
import java.util.Set;

import de.metalcon.domain.Muid;

/**
 * data object containing mapping data from persistent storage
 * 
 * @author sebschlicht
 * 
 */
public class UrlMappingPersistenceData {

    protected Map<Muid, Set<String>> mappingsOfEntity;

    protected Map<Muid, Map<String, Muid>> mappingsOfRecordsOfBand;

    protected Map<Muid, Map<String, Muid>> mappingsOfTracksOfRecord;

    protected Map<Muid, Muid> parentalBands;

    public UrlMappingPersistenceData(
            Map<Muid, Set<String>> mappingsOfEntity,
            Map<Muid, Map<String, Muid>> mappingsOfRecordsOfBand,
            Map<Muid, Map<String, Muid>> mappingsOfTracksOfRecord,
            Map<Muid, Muid> parentalBands) {
        this.mappingsOfEntity = mappingsOfEntity;
        this.mappingsOfRecordsOfBand = mappingsOfRecordsOfBand;
        this.mappingsOfTracksOfRecord = mappingsOfTracksOfRecord;
        this.parentalBands = parentalBands;
    }

    public Map<Muid, Set<String>> getMappingsOfEntities() {
        return mappingsOfEntity;
    }

    public Map<Muid, Map<String, Muid>> getMappingsOfRecordsOfBand() {
        return mappingsOfRecordsOfBand;
    }

    public Map<Muid, Map<String, Muid>> getMappingsOfTracksOfRecord() {
        return mappingsOfTracksOfRecord;
    }

    public Map<Muid, Muid> getParentalBands() {
        return parentalBands;
    }

}
