package de.metalcon.urlmappingserver.mappers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import de.metalcon.domain.EntityType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
import de.metalcon.urlmappingserver.api.requests.registration.CityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EventUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.VenueUrlData;

public class EventUrlMapper extends EntityUrlMapper {

    /**
     * date formatter for URL mapping
     */
    protected static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * create mapper for event entitites
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    public EventUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, EntityType.EVENT, "pathEvent");
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        Set<String> newMappingsForEvent = super.createMapping(entityUrlData);
        EventUrlData eventUrlData = (EventUrlData) entityUrlData;
        String eventName = convertToUrlText(eventUrlData.getName());

        // add mapping: /<first event date>-<event name>
        Date date = eventUrlData.getDate();
        if (date != null) {
            String sDate = DATE_FORMATTER.format(date);
            newMappingsForEvent.add(sDate + WORD_SEPERATOR + eventName);
        }

        // add mapping: /<event name>-<city name>
        CityUrlData cityUrlData = eventUrlData.getCity();
        if (cityUrlData != null) {
            newMappingsForEvent.add(eventName + WORD_SEPERATOR
                    + convertToUrlText(cityUrlData.getName()));
        }

        // add mapping: /<event name>-<venue name>
        VenueUrlData venueUrlData = eventUrlData.getVenue();
        if (venueUrlData != null) {
            newMappingsForEvent.add(eventName + WORD_SEPERATOR
                    + convertToUrlText(venueUrlData.getName()));
        }

        return newMappingsForEvent;
    }
}
