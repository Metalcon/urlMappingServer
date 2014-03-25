package de.metalcon.urlmappingserver.persistence;

import java.util.Map;
import java.util.Set;

import de.metalcon.domain.Muid;

public class UrlMappingData {

    protected Map<Muid, Set<String>> mappingsOfEntity;

    protected Map<Muid, Map<Muid, Set<String>>> mappingsOfRecordsOfBand;

    protected Map<Muid, Map<Muid, Set<String>>> mappingsOfTracksOfRecord;

}
