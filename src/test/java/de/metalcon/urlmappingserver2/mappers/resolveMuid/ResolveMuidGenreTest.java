package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.GenreFactory;

public class ResolveMuidGenreTest extends ResolveMuidNamedEntityTest {

    protected static GenreFactory GENRE_FACTORY = new GenreFactory();

    @Override
    protected EntityFactory getFactory() {
        return GENRE_FACTORY;
    }

}
