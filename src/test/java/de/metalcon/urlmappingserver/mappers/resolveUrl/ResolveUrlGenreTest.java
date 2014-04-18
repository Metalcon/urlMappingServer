package de.metalcon.urlmappingserver.mappers.resolveUrl;

import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveUrlNamedEntityTest;
import de.metalcon.urlmappingserver.mappers.factories.GenreFactory;

public class ResolveUrlGenreTest extends ResolveUrlNamedEntityTest {

    protected static GenreFactory GENRE_FACTORY = new GenreFactory();

    @Override
    protected EntityFactory getFactory() {
        return GENRE_FACTORY;
    }

}
