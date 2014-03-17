package de.metalcon.urlmappingserver.mappers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import de.metalcon.domain.EntityType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EventUrlData;

public class EventUrlMapper extends EntityUrlMapper {

    protected static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
            "yyyy-MM-dd");

    public EventUrlMapper() {
        super(EntityType.EVENT, null);
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        Set<String> newMappingsForEvent = super.createMapping(entityUrlData);

        EventUrlData eventUrlData = (EventUrlData) entityUrlData;
        Date date = eventUrlData.getDate();
        if (date != null) {
            String sDate = DATE_FORMATTER.format(date);
            newMappingsForEvent.add(sDate);
        }

        return newMappingsForEvent;
    }

}
