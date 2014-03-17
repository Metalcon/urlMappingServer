package de.metalcon.urlmappingserver;

import java.util.Map;

import de.metalcon.domain.EntityType;
import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.mappers.BandUrlMapper;
import de.metalcon.urlmappingserver.mappers.CityUrlMapper;
import de.metalcon.urlmappingserver.mappers.EventUrlMapper;
import de.metalcon.urlmappingserver.mappers.GenreUrlMapper;
import de.metalcon.urlmappingserver.mappers.InstrumentUrlMapper;
import de.metalcon.urlmappingserver.mappers.RecordUrlMapper;
import de.metalcon.urlmappingserver.mappers.TourUrlMapper;

public class EntityUrlMappingManager implements MetalconUrlMapper {

    private BandUrlMapper bandMapper;

    private CityUrlMapper cityMapper;

    private EventUrlMapper eventMapper;

    private GenreUrlMapper genreMapper;

    private InstrumentUrlMapper instrumentMapper;

    private RecordUrlMapper recordMapper;

    private TourUrlMapper tourMapper;

    public EntityUrlMappingManager() {
        bandMapper = new BandUrlMapper(this);
        cityMapper = new CityUrlMapper(this);
        eventMapper = new EventUrlMapper(this);
        genreMapper = new GenreUrlMapper(this);
        instrumentMapper = new InstrumentUrlMapper(this);
        recordMapper = new RecordUrlMapper(this);
        tourMapper = new TourUrlMapper(this);
    }

    protected EntityUrlMapper getMapper(EntityType entityType) {
        switch (entityType) {

            case BAND:
                return bandMapper;

            case CITY:
                return cityMapper;

            case EVENT:
                return eventMapper;

            case GENRE:
                return genreMapper;

            case INSTRUMENT:
                return instrumentMapper;

            case RECORD:
                return recordMapper;

            case TOUR:
                return tourMapper;

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
