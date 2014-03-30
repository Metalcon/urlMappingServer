package de.metalcon.urlmappingserver2.mappers.factories;

import de.metalcon.domain.MuidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.CityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.VenueUrlData;
import de.metalcon.urlmappingserver2.mappers.EntityFactory;

public class VenueFactory extends EntityFactory {

    protected CityFactory cityFactory;

    public VenueFactory(
            CityFactory cityFactory) {
        super(MuidType.VENUE);
        this.cityFactory = cityFactory;
    }

    @Override
    public EntityUrlData getEntityFull() {
        return new VenueUrlData(MuidFactory.generateMuid(getMuidType()),
                "venue" + crrEntityId++, getCity());
    }

    @Override
    public EntityUrlData getEntityIdentical(EntityUrlData entity) {
        VenueUrlData venue = (VenueUrlData) entity;
        return new VenueUrlData(MuidFactory.generateMuid(getMuidType()),
                venue.getName(), venue.getCity());
    }

    public VenueUrlData getVenueSameName(VenueUrlData venue) {
        return new VenueUrlData(MuidFactory.generateMuid(getMuidType()),
                venue.getName(), getCity());
    }

    public VenueUrlData getVenueWoCity() {
        VenueUrlData venue = (VenueUrlData) getEntityFull();
        return new VenueUrlData(venue.getMuid(), venue.getName(), null);
    }

    public VenueUrlData getVenueWoCity(VenueUrlData venue) {
        return new VenueUrlData(MuidFactory.generateMuid(getMuidType()),
                venue.getName(), null);
    }

    protected CityUrlData getCity() {
        return (CityUrlData) cityFactory.getEntityFull();
    }

}
