package de.metalcon.urlmappingserver.mappers.resolveMuid;

import de.metalcon.urlmappingserver.mappers.EntityFactory;
import de.metalcon.urlmappingserver.mappers.ResolveMuidTest;
import de.metalcon.urlmappingserver.mappers.factories.TourFactory;

public class ResolveMuidTourTest extends ResolveMuidTest {

    protected static TourFactory TOUR_FACTORY = new TourFactory();

    @Override
    protected EntityFactory getFactory() {
        return TOUR_FACTORY;
    }

}
