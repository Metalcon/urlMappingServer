package de.metalcon.urlmappingserver.mappers.factories;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.UserUrlData;
import de.metalcon.urlmappingserver.mappers.EntityFactory;

public class UserFactory extends EntityFactory {

    public UserFactory() {
        super("pathUser", MuidType.USER);
    }

    @Override
    public EntityUrlData getEntityFull() {
        return new UserUrlData(MuidFactory.generateMuid(getMuidType()), "user",
                "Mr. " + crrEntityId++);
    }

    @Override
    public EntityUrlData getEntityIdentical(EntityUrlData entity) {
        UserUrlData user = (UserUrlData) entity;
        return new UserUrlData(MuidFactory.generateMuid(getMuidType()),
                user.getFirstName(), user.getLastName());
    }

}
