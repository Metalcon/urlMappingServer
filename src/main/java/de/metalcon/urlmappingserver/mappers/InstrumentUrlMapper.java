package de.metalcon.urlmappingserver.mappers;

import de.metalcon.domain.EntityType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.EntityUrlMappingManager;

/**
 * mapper for instrument entities
 * 
 * @author sebschlicht
 * 
 */
public class InstrumentUrlMapper extends EntityUrlMapper {

    /**
     * create mapper for instrument entities
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     */
    public InstrumentUrlMapper(
            EntityUrlMappingManager manager) {
        super(manager, EntityType.INSTRUMENT, "pathInstrument");
    }

}
