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
        super(manager, MuidType.VENUE, false, "pathVenue");
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        Set<String> newMappingsForVenue = createEmptyMappingSet();
        VenueUrlData venueUrlData = (VenueUrlData) entityUrlData;

        // add mapping: /<venue name>
        String nameMapping = convertToUrlText(venueUrlData.getName());
        newMappingsForVenue.add(nameMapping);

        // add mapping: /<venue name>-<city name>
        CityUrlData cityUrlData = venueUrlData.getCity();
        if (cityUrlData != null) {
            newMappingsForVenue.add(convertToUrlText(venueUrlData.getName())
                    + WORD_SEPARATOR + convertToUrlText(cityUrlData.getName()));
        }

        // add mapping: /<venue name>-<muid>
        String uniqueMapping =
                nameMapping + WORD_SEPARATOR + venueUrlData.getMuid();
        newMappingsForVenue.add(uniqueMapping);

        return newMappingsForVenue;
    }
}
