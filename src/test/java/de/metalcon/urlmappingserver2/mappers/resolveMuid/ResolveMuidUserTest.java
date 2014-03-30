package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.UserUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;

public class ResolveMuidUserTest extends ResolveMuidNamedEntityTest {

    @Override
    protected MuidType getInstanceMuidType() {
        return getMuidType();
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getUser();
    }

    @Override
    protected EntityUrlData getIdentical(EntityUrlData entity) {
        UserUrlData user = (UserUrlData) entity;
        return new UserUrlData(MuidFactory.generateMuid(getMuidType()),
                user.getFirstName(), user.getLastName());
    }

    protected static MuidType getMuidType() {
        return MuidType.USER;
    }

    public static UserUrlData getUser() {
        return new UserUrlData(MuidFactory.generateMuid(getMuidType()),
                VALID_NAME, "user" + CRR_ENTITY_ID++);
    }

}
