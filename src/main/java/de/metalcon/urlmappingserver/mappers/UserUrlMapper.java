package de.metalcon.urlmappingserver.mappers;

import java.util.Set;

import de.metalcon.domain.EntityType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.UserUrlData;

public class UserUrlMapper extends EntityUrlMapper {

    /**
     * create mapper for user entitites
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    public UserUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, EntityType.USER, "pathUser");
    }

    @Override
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        Set<String> newMappingsForUser = super.createMapping(entityUrlData);
        UserUrlData userUrlData = (UserUrlData) entityUrlData;

        // add mapping: /<user's first name>-<user's last name>
        String firstName = convertToUrlText(userUrlData.getFirstName());
        String lastName = convertToUrlText(userUrlData.getLastName());
        newMappingsForUser.add(firstName + WORD_SEPERATOR + lastName);

        return newMappingsForUser;
    }

}
