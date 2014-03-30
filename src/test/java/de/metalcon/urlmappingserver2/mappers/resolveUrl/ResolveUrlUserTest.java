package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.UserFactory;

public class ResolveUrlUserTest extends ResolveUrlNamedEntityTest {

    protected static UserFactory USER_FACTORY = new UserFactory();

    @Override
    protected EntityFactory getFactory() {
        return USER_FACTORY;
    }

}
