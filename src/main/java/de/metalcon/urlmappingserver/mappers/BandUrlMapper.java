package de.metalcon.urlmappingserver.mappers;

import java.util.Map;

import de.metalcon.domain.EntityType;
import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.EntityUrlMapper;

public class BandUrlMapper extends EntityUrlMapper {

    public BandUrlMapper() {
        super(EntityType.BAND, "pathBand");
    }

    @Override
    public Muid resolveMuid(Map<String, String> url, EntityType type) {
        String bandMapping = getPathVar(url, urlPathVarName);
        if (bandMapping.equals(EMPTY_ENTITY)) {
            return Muid.EMPTY_MUID;
        }
        return mappingToEntity.get(bandMapping);
    }

}
