package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.UserUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlNamedEntityTest;

public class ResolveUrlUserTest extends ResolveUrlNamedEntityTest {

    protected static final String VALID_LAST_NAME = "Testarum";

    @Override
    protected MuidType getInstanceMuidType() {
        return getMuidType();
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getUser();
    }

    protected static MuidType getMuidType() {
        return MuidType.USER;
    }

    public static UserUrlData getUser() {
        return new UserUrlData(MuidFactory.generateMuid(getMuidType()),
                VALID_NAME, VALID_LAST_NAME);
    }

}
