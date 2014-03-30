package de.metalcon.urlmappingserver2.mappers.factories;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.GenreUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;

public class GenreFactory extends EntityFactory {

    public GenreFactory() {
        super(MuidType.GENRE);
    }

    @Override
    public EntityUrlData getEntityFull() {
        return new GenreUrlData(MuidFactory.generateMuid(getMuidType()),
                "genre" + crrEntityId++);
    }

    @Override
    public EntityUrlData getEntityIdentical(EntityUrlData entity) {
        return new GenreUrlData(MuidFactory.generateMuid(getMuidType()),
                entity.getName());
    }

}
