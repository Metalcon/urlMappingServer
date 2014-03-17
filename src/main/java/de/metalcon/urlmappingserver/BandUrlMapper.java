package de.metalcon.urlmappingserver;

import java.util.Map;
import java.util.Set;

import de.metalcon.domain.EntityType;
import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public class BandUrlMapper extends EntityUrlMapper {

    @Override
    public void registerMuid(EntityUrlData urlData) {
        Set<String> mapping = super.createMapping(urlData);
    }

    @Override
    public Muid resolveMuid(Map<String, String> url, EntityType type) {
        // TODO Auto-generated method stub
        return null;
    }

}
