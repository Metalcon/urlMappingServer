package de.metalcon.urlmappingserver.mappers;

import java.util.Map;
import java.util.Set;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

/**
 * mapper for band entities
 * 
 * @author sebschlicht
 * 
 */
public class BandUrlMapper extends EntityUrlMapper {

    /**
     * create mapper for band entities
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    public BandUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, MuidType.BAND, true, "pathBand");
    }

    /**
     * @return all mappings of a band
     */
    public Map<Muid, Set<String>> getMappingsOfBand() {
        return mappingsOfEntities;
    }

    @Override
    protected void storeMapping(EntityUrlData entity, String mapping) {
        // MUID can be empty here, parental MUID remains unset
        persistentStorage.saveMapping(MuidType.BAND.getRawIdentifier(), entity
                .getMuid().getValue(), mapping, 0);
    }

    @Override
    public void registerMuid(EntityUrlData entityUrlData) {
        if (!entityUrlData.hasEmptyMuid()) {
            super.registerMuid(entityUrlData);
        } else {
            registerMapping(EMPTY_ENTITY, Muid.EMPTY_MUID);
        }
    }

}
