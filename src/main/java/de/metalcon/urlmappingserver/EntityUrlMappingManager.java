package de.metalcon.urlmappingserver;

import java.util.Map;
import java.util.Set;

import de.metalcon.domain.Muid;
import de.metalcon.domain.UidType;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.mappers.BandUrlMapper;
import de.metalcon.urlmappingserver.mappers.CityUrlMapper;
import de.metalcon.urlmappingserver.mappers.EventUrlMapper;
import de.metalcon.urlmappingserver.mappers.GenreUrlMapper;
import de.metalcon.urlmappingserver.mappers.InstrumentUrlMapper;
import de.metalcon.urlmappingserver.mappers.RecordUrlMapper;
import de.metalcon.urlmappingserver.mappers.TourUrlMapper;
import de.metalcon.urlmappingserver.mappers.TrackUrlMapper;
import de.metalcon.urlmappingserver.mappers.UserUrlMapper;
import de.metalcon.urlmappingserver.mappers.VenueUrlMapper;
import de.metalcon.urlmappingserver.persistence.PersistentStorage;
import de.metalcon.urlmappingserver.persistence.UrlMappingPersistenceData;

/**
 * URL mapping manager for all Metalcon entities
 * 
 * @author sebschlicht
 * 
 */
public class EntityUrlMappingManager implements MetalconUrlMapper {

    /**
     * storage to make mapping persistent
     */
    private final PersistentStorage persistentStorage;

    private final BandUrlMapper bandMapper;

    private final CityUrlMapper cityMapper;

    private final EventUrlMapper eventMapper;

    private final GenreUrlMapper genreMapper;

    private final InstrumentUrlMapper instrumentMapper;

    private final RecordUrlMapper recordMapper;

    private final TourUrlMapper tourMapper;

    private final TrackUrlMapper trackMapper;

    private final UserUrlMapper userMapper;

    private final VenueUrlMapper venueMapper;

    /**
     * create URL mapping manager for all Metalcon entities<br>
     * not persistent
     */
    public EntityUrlMappingManager() {
        this(null);
    }

    /**
     * create URL mapping manager for all Metalcon entities
     * 
     * @param persistentStorage
     *            storage to make mapping persistent
     */
    public EntityUrlMappingManager(
            final PersistentStorage persistentStorage) {
        this.persistentStorage = persistentStorage;
        bandMapper = new BandUrlMapper(this);
        cityMapper = new CityUrlMapper(this);
        eventMapper = new EventUrlMapper(this);
        genreMapper = new GenreUrlMapper(this);
        instrumentMapper = new InstrumentUrlMapper(this);
        recordMapper = new RecordUrlMapper(this, bandMapper);
        tourMapper = new TourUrlMapper(this);
        trackMapper = new TrackUrlMapper(this, recordMapper);
        userMapper = new UserUrlMapper(this);
        venueMapper = new VenueUrlMapper(this);
    }

    /**
     * @return storage to make mapping persistent
     */
    public PersistentStorage getPersistentStorage() {
        return persistentStorage;
    }

    /**
     * get the mapper handling a specific entity type
     * 
     * @param muidType
     *            type of the entity the mapper is needed for
     * @return URL mapper for the entity type passed
     * @throws UnsupportedOperationException
     *             if no mapper for this entity type existing
     */
    public EntityUrlMapper getMapper(final UidType muidType) {
        switch (muidType) {

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

            case TRACK:
                return trackMapper;

            case USER:
                return userMapper;

            case VENUE:
                return venueMapper;

            default:
                throw new UnsupportedOperationException(
                        "unknown entity type \"" + muidType.getIdentifier()
                                + "\"");
        }
    }

    @Override
    public void registerMuid(final EntityUrlData urlData) {
        getMapper(urlData.getMuid().getType()).registerMuid(urlData);
    }

    @Override
    public Muid resolveMuid(final Map<String, String> url, final UidType type) {
        return getMapper(type).resolveMuid(url, type);
    }

    @Override
    public String resolveUrl(final Muid muid) {
        return getMapper(muid.getType()).resolveUrl(muid);
    }

    public void loadFromDatabase() {
        if (persistentStorage == null) {
            return;
        }
        UrlMappingPersistenceData persistenceData =
                persistentStorage.restoreMappings();

        Map<Muid, Set<String>> mappingsOfEntities =
                persistenceData.getMappingsOfEntities();
        EntityUrlMapper mapper;
        Set<String> mappingsOfEntity;

        // loop through all MUIDs
        for (Muid muid : mappingsOfEntities.keySet()) {
            // get correct mapper
            mapper = getMapper(muid.getType());
            mappingsOfEntity = mappingsOfEntities.get(muid);

            // register all mappings for this MUID
            for (String mapping : mappingsOfEntity) {
                mapper.registerMapping(mapping, muid);
            }
        }

        // register mappings and hierarchy for records
        recordMapper.setMappingsOfRecordsOfBand(persistenceData
                .getMappingsOfRecordsOfBand());
        // register mappings and hierarchy for tracks
        trackMapper.setMappingsToTracksOfRecords(
                persistenceData.getMappingsOfTracksOfRecord(),
                persistenceData.getParentalBands());
    }

}
