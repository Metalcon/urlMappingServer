package de.metalcon.urlmappingserver.mappers;

import de.metalcon.domain.EntityType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;

public class CityUrlMapper extends EntityUrlMapper {

    /**
     * create mapper for city entities
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    public CityUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, EntityType.CITY, "pathCity");
    }

}
