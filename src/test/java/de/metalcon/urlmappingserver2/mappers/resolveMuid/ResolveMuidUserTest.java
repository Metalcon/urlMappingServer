package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.UserFactory;

public class ResolveMuidUserTest extends ResolveMuidNamedEntityTest {

    protected static UserFactory USER_FACTORY = new UserFactory();

    @Override
    protected EntityFactory getFactory() {
        return USER_FACTORY;
    }

}
