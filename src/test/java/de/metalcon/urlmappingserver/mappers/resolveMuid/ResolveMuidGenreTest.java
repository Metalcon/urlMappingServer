package de.metalcon.urlmappingserver.mappers.resolveMuid;

import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver.mappers.factories.GenreFactory;

public class ResolveMuidGenreTest extends ResolveMuidNamedEntityTest {

    protected static GenreFactory GENRE_FACTORY = new GenreFactory();

    @Override
    protected EntityFactory getFactory() {
        return GENRE_FACTORY;
    }

}
