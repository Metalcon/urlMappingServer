package de.metalcon.urlmappingserver.mappers;

import java.util.Set;

import de.metalcon.domain.EntityType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TourUrlData;

public class TourUrlMapper extends EntityUrlMapper {

    /**
     * create mapper for tour entitites
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    public TourUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, EntityType.TOUR, "pathTour");
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        Set<String> newMappingsForTour = super.createMapping(entityUrlData);
        TourUrlData tourUrlData = (TourUrlData) entityUrlData;

        // add mapping: /<tour year>-<tour name>
        int year = tourUrlData.getYear();
        if (year != 0) {
            String sYear = String.valueOf(year);
            newMappingsForTour.add(sYear + WORD_SEPERATOR
                    + convertToUrlText(tourUrlData.getName()));
        }

        return newMappingsForTour;
    }

}
