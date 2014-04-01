package de.metalcon.urlmappingserver.mappers.resolveUrl;

import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveUrlNamedEntityTest;
import de.metalcon.urlmappingserver.mappers.factories.UserFactory;

public class ResolveUrlUserTest extends ResolveUrlNamedEntityTest {

    protected static UserFactory USER_FACTORY = new UserFactory();

    @Override
    protected EntityFactory getFactory() {
        return USER_FACTORY;
    }

}
