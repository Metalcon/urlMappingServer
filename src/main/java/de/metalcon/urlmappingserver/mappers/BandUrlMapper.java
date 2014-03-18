package de.metalcon.urlmappingserver.mappers;

import java.util.Map;

import de.metalcon.domain.EntityType;
import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;

/**
 * mapper for band entities
 * 
 * @author sebschlicht
 * 
 */
public class BandUrlMapper extends EntityUrlMapper {

    /**
     * create mapper for band entities
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    public BandUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, EntityType.BAND, "pathBand");
    }

    @Override
    public Muid resolveMuid(Map<String, String> url, EntityType type) {
        String bandMapping = getPathVar(url, urlPathVarName);

        // allow empty MUIDs to access records and tracks not assigned to a (single) band
        if (bandMapping.equals(EMPTY_ENTITY)) {
            return Muid.EMPTY_MUID;
        }

        return mappingToEntity.get(bandMapping);
    }

}
