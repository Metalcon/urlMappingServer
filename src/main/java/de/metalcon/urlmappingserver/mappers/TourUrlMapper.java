package de.metalcon.urlmappingserver.mappers;

import java.util.Set;

import de.metalcon.domain.UidType;
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
            final EntityUrlMappingManager manager) {
        super(manager, UidType.TOUR, false, "pathTour");
    }

    @Override
    protected Set<String> createMapping(final EntityUrlData entityUrlData) {
        checkUidType(entityUrlData.getMuid().getType());
        Set<String> newMappingsForTour = createEmptyMappingSet();

        // add mapping: /<muid>
        String uniqueMapping = entityUrlData.getMuid().toString();
        newMappingsForTour.add(uniqueMapping);

        return newMappingsForTour;
    }

}
