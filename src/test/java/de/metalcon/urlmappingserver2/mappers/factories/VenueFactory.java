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
        super("pathVenue", MuidType.VENUE);
        this.cityFactory = cityFactory;
    }

    public String getMappingCityName(VenueUrlData venue) {
        return getMappingName(venue) + WORD_SEPARATOR
                + cityFactory.getMappingName(venue.getCity());
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

    /**
     * create venue having the same name like another venue
     */
    public VenueUrlData getVenueSameName(VenueUrlData venue) {
        return new VenueUrlData(MuidFactory.generateMuid(getMuidType()),
                venue.getName(), getCity());
    }

    /**
     * create venue without city
     */
    public VenueUrlData getVenueWoCity() {
        VenueUrlData venue = (VenueUrlData) getEntityFull();
        return new VenueUrlData(venue.getMuid(), venue.getName(), null);
    }

    /**
     * create copy of another venue but remove city
     */
    public VenueUrlData getVenueWoCity(VenueUrlData venue) {
        return new VenueUrlData(MuidFactory.generateMuid(getMuidType()),
                venue.getName(), null);
    }

    protected CityUrlData getCity() {
        return (CityUrlData) cityFactory.getEntityFull();
    }

}
