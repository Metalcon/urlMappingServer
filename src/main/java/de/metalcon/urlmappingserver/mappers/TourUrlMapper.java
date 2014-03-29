package de.metalcon.urlmappingserver.mappers;

import java.util.Set;

import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

/**
 * mapper for tour entities
 * 
 * @author sebschlicht
 * 
 */
public class TourUrlMapper extends EntityUrlMapper {

    /**
     * create mapper for tour entitites
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    public TourUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, MuidType.TOUR, false, "pathTour");
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        if (entityUrlData.getMuid().getMuidType() != muidType) {
            throw new IllegalArgumentException("mapper handles muid type \""
                    + getMuidType() + "\" only (was: \""
                    + entityUrlData.getMuid().getMuidType() + "\")");
        }

        Set<String> newMappingsForTour = createEmptyMappingSet();

        // add mapping: /<muid>
        String uniqueMapping = entityUrlData.getMuid().toString();
        newMappingsForTour.add(uniqueMapping);

        return newMappingsForTour;
    }

}
