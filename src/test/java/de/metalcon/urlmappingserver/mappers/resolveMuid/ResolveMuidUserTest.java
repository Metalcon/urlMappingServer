package de.metalcon.urlmappingserver.mappers.resolveMuid;

import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver.mappers.factories.UserFactory;

public class ResolveMuidUserTest extends ResolveMuidNamedEntityTest {

    protected static UserFactory USER_FACTORY = new UserFactory();

    @Override
    protected EntityFactory getFactory() {
        return USER_FACTORY;
    }

}
