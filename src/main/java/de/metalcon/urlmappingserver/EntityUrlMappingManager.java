package de.metalcon.urlmappingserver;

import java.util.Map;

import de.metalcon.domain.EntityType;
import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.mappers.BandUrlMapper;

public class EntityUrlMappingManager implements MetalconUrlMapper {

    private BandUrlMapper bandUrlMapper;

    public EntityUrlMappingManager() {
        bandUrlMapper = new BandUrlMapper();
    }

    protected EntityUrlMapper getMapper(EntityType entityType) {
        switch (entityType) {

            case BAND:
                return bandUrlMapper;

            default:
                throw new UnsupportedOperationException(
                        "unknown entity type \"" + entityType.getIdentifier()
                                + "\"");

        }
    }

    @Override
    public void registerMuid(EntityUrlData urlData) {
        getMapper(urlData.getMuid().getEntityType()).registerMuid(urlData);
    }

    @Override
    public Muid resolveMuid(Map<String, String> url, EntityType type) {
        return getMapper(type).resolveMuid(url, type);
    }

}
