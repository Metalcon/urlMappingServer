package de.metalcon.urlmappingserver.mappers;

import java.text.SimpleDateFormat;
import java.util.Set;

import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

/**
 * mapper for event entities
 * 
 * @author sebschlicht
 * 
 */
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
        super(manager, MuidType.EVENT, false, "pathEvent");
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        if (entityUrlData.getMuid().getMuidType() != muidType) {
            throw new IllegalArgumentException("mapper handles muid type \""
                    + getMuidType() + "\" only (was: \""
                    + entityUrlData.getMuid().getMuidType() + "\")");
        }

        Set<String> newMappingsForEvent = createEmptyMappingSet();

        // add mapping: /<muid>
        String uniqueMapping = entityUrlData.getMuid().toString();
        newMappingsForEvent.add(uniqueMapping);

        return newMappingsForEvent;
    }
}
