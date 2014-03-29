package de.metalcon.urlmappingserver2.mappers.resolveUrl;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.GenreUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveUrlNamedEntityTest;

public class ResolveUrlGenreTest extends ResolveUrlNamedEntityTest {

    @Override
    protected MuidType getMuidType() {
        return MuidType.GENRE;
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getGenre();
    }

    public static GenreUrlData getGenre() {
        return new GenreUrlData(MuidFactory.generateMuid(TYPE), VALID_NAME);
    }

}
