package de.metalcon.urlmappingserver.mappers;

import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;

/**
 * mapper for genre entities
 * 
 * @author sebschlicht
 * 
 */
public class GenreUrlMapper extends EntityUrlMapper {

    /**
     * create mapper for genre entities
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    public GenreUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, MuidType.GENRE, false, "pathGenre");
    }

}
