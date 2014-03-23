package de.metalcon.urlmappingserver.mappers;

import org.junit.BeforeClass;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapperTest;
import de.metalcon.urlmappingserver.api.requests.registration.GenreUrlData;

public class GenreUrlMapperTest extends EntityUrlMapperTest {

    public static final GenreUrlData GENRE = new GenreUrlData(
            Muid.create(MuidType.GENRE), VALID_NAME);

    protected static final GenreUrlData SIMILAR_GENRE = new GenreUrlData(
            Muid.create(MuidType.GENRE), GENRE.getName());

    @BeforeClass
    public static void beforeClass() {
        ENTITY = GENRE;
        SIMILAR_ENTITY = SIMILAR_GENRE;
        EntityUrlMapperTest.beforeClass();
    }

}
