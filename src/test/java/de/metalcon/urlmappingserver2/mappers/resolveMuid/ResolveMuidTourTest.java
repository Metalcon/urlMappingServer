package de.metalcon.urlmappingserver2.mappers.resolveMuid;

import de.metalcon.urlmappingserver2.mappers.EntityFactory;
import de.metalcon.urlmappingserver2.mappers.ResolveMuidTest;
import de.metalcon.urlmappingserver2.mappers.factories.TourFactory;

public class ResolveMuidTourTest extends ResolveMuidTest {

    protected static TourFactory TOUR_FACTORY = new TourFactory();

    @Override
    protected EntityFactory getFactory() {
        return TOUR_FACTORY;
    }

}
