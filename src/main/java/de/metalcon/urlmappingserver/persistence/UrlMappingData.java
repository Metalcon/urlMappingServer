package de.metalcon.urlmappingserver.persistence;

import java.util.Map;
import java.util.Set;

import de.metalcon.domain.Muid;

public class UrlMappingData {

    protected Map<Muid, Set<String>> mappingsOfEntity;

    protected Map<Muid, Map<String, Muid>> mappingsOfRecordsOfBand;

    protected Map<Muid, Map<String, Muid>> mappingsOfTracksOfRecord;

    public UrlMappingData(
            Map<Muid, Set<String>> mappingsOfEntity,
            Map<Muid, Map<String, Muid>> mappingsOfRecordsOfBand,
            Map<Muid, Map<String, Muid>> mappingsOfTracksOfRecord) {
        this.mappingsOfEntity = mappingsOfEntity;
        this.mappingsOfRecordsOfBand = mappingsOfRecordsOfBand;
        this.mappingsOfTracksOfRecord = mappingsOfTracksOfRecord;
    }

    public Map<Muid, Set<String>> getMappingsOfEntity() {
        return mappingsOfEntity;
    }

    public Map<Muid, Map<String, Muid>> getMappingsOfRecordsOfBand() {
        return mappingsOfRecordsOfBand;
    }

    public Map<Muid, Map<String, Muid>> getMappingsOfTracksOfRecord() {
        return mappingsOfTracksOfRecord;
    }

}
