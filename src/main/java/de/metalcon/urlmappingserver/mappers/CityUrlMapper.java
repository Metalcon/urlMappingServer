package de.metalcon.urlmappingserver.mappers;

import de.metalcon.domain.EntityType;
import de.metalcon.urlmappingserver.EntityUrlMapper;

public class CityUrlMapper extends EntityUrlMapper {

    /**
     * create mapper for city entities
     */
    public CityUrlMapper() {
        super(EntityType.CITY, "pathCity");
    }

}
