package de.metalcon.urlmappingserver2.mappers.factories;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TourUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;

public class TourFactory extends EntityFactory {

    public TourFactory() {
        super("pathTour", MuidType.TOUR);
    }

    @Override
    public String getMappingId(EntityUrlData entity) {
        // the only tour mapping: MUID
        return entity.getMuid().toString();
    }

    @Override
    public EntityUrlData getEntityFull() {
        return new TourUrlData(MuidFactory.generateMuid(getMuidType()));
    }

    @Override
    public EntityUrlData getEntityIdentical(EntityUrlData entity) {
        return getEntityFull();
    }

}
