package de.metalcon.urlmappingserver.mappers;

import de.metalcon.domain.EntityType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;

public class GenreUrlMapper extends EntityUrlMapper {

    /**
     * create mapper for genre entities
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    public GenreUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, EntityType.GENRE, "pathGenre");
    }

}
