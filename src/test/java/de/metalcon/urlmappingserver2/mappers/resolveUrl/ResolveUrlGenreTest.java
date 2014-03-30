package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlNamedEntityTest;
import de.metalcon.urlmappingserver2.mappers.factories.GenreFactory;

public class ResolveUrlGenreTest extends ResolveUrlNamedEntityTest {

    protected static GenreFactory GENRE_FACTORY = new GenreFactory();

    @Override
    protected EntityFactory getFactory() {
        return GENRE_FACTORY;
    }

}
