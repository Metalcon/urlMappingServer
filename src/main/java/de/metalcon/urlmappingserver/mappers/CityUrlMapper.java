package de.metalcon.urlmappingserver.mappers;

import de.metalcon.domain.UidType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;

/**
 * mapper for city entities
 * 
 * @author sebschlicht
 * 
 */
public class CityUrlMapper extends EntityUrlMapper {

    /**
     * create mapper for city entities
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    public CityUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, UidType.CITY, false, "pathCity");
    }

}
