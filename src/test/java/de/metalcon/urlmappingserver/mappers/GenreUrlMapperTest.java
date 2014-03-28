package de.metalcon.urlmappingserver.mappers;

import org.junit.BeforeClass;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.GenreUrlData;

public class GenreUrlMapperTest extends EntityUrlMapperTest {

    protected static final GenreUrlData GENRE = new GenreUrlData(
            MuidFactory.generateMuid(MuidType.GENRE), VALID_NAME);

    protected static final GenreUrlData SIMILAR_GENRE = new GenreUrlData(
            MuidFactory.generateMuid(MuidType.GENRE), GENRE.getName());

    @BeforeClass
    public static void beforeClass() {
        ENTITY = GENRE;
        SIMILAR_ENTITY = SIMILAR_GENRE;
        EntityUrlMapperTest.beforeClass();
    }

}
