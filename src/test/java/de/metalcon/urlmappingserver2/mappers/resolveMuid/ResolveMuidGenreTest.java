package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.GenreUrlData;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidNamedEntityTest;

public class ResolveMuidGenreTest extends ResolveMuidNamedEntityTest {

    @Override
    protected MuidType getInstanceMuidType() {
        return getMuidType();
    }

    @Override
    protected EntityUrlData getEntityFull() {
        return getGenre();
    }

    protected static MuidType getMuidType() {
        return MuidType.GENRE;
    }

    public static GenreUrlData getGenre() {
        return new GenreUrlData(MuidFactory.generateMuid(getMuidType()),
                VALID_NAME);
    }

}
