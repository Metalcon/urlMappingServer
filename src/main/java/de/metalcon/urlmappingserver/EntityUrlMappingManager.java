package de.metalcon.urlmappingserver;

import java.util.Map;

import de.metalcon.domain.EntityType;
import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.mappers.BandUrlMapper;
import de.metalcon.urlmappingserver.mappers.CityUrlMapper;

public class EntityUrlMappingManager implements MetalconUrlMapper {

    private BandUrlMapper bandMapper;

    private CityUrlMapper cityMapper;

    public EntityUrlMappingManager() {
        bandMapper = new BandUrlMapper(this);
        cityMapper = new CityUrlMapper(this);
    }

    protected EntityUrlMapper getMapper(EntityType entityType) {
        switch (entityType) {

            case BAND:
                return bandMapper;

            case CITY:
                return cityMapper;

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
