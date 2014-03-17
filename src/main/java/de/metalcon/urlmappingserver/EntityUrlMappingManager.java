package de.metalcon.urlmappingserver;

import java.util.Map;

import de.metalcon.domain.EntityType;
import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public class EntityUrlMappingManager implements MetalconUrlMapper {

    private BandUrlMapper bandUrlMapper;

    @Override
    public void registerMuid(EntityUrlData urlData) {
        switch (urlData.getMuid().getEntityType()) {
            case BAND:
                bandUrlMapper.registerMuid(urlData);
                break;

            default:
                throw new UnsupportedOperationException(
                        "unknown entity type \""
                                + urlData.getMuid().getEntityType()
                                        .getIdentifier() + "\"");
        }
    }

    @Override
    public Muid resolveMuid(Map<String, String> url, EntityType type) {
        // TODO Auto-generated method stub
        return null;
    }

}
