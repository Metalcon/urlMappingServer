package de.metalcon.urlmappingserver.mappers;

import java.util.Set;

import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
import de.metalcon.urlmappingserver.api.requests.registration.CityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.VenueUrlData;

/**
 * mapper for venue entities
 * 
 * @author sebschlicht
 * 
 */
public class VenueUrlMapper extends EntityUrlMapper {

    /**
     * create mapper for venue entities
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    public VenueUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, MuidType.VENUE, "pathVenue");
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        Set<String> newMappingsForVenue = super.createMapping(entityUrlData);
        VenueUrlData venueUrlData = (VenueUrlData) entityUrlData;

        // add mapping: /<venue name>-<city name>
        CityUrlData cityUrlData = venueUrlData.getCity();
        if (cityUrlData != null) {
            newMappingsForVenue.add(convertToUrlText(venueUrlData.getName())
                    + WORD_SEPERATOR + convertToUrlText(cityUrlData.getName()));
        }

        return newMappingsForVenue;
    }
}
