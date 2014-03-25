package de.metalcon.urlmappingserver;

import java.util.Map;
import java.util.Set;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
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
    private PersistentStorage persistentStorage;

    private BandUrlMapper bandMapper;

    private CityUrlMapper cityMapper;

    private EventUrlMapper eventMapper;

    private GenreUrlMapper genreMapper;

    private InstrumentUrlMapper instrumentMapper;

    private RecordUrlMapper recordMapper;

    private TourUrlMapper tourMapper;

    private TrackUrlMapper trackMapper;

    private UserUrlMapper userMapper;

    private VenueUrlMapper venueMapper;

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
            PersistentStorage persistentStorage) {
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
    public EntityUrlMapper getMapper(MuidType muidType) {
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
    public void registerMuid(EntityUrlData urlData) {
        getMapper(urlData.getMuid().getMuidType()).registerMuid(urlData);
    }

    @Override
    public Muid resolveMuid(Map<String, String> url, MuidType type) {
        return getMapper(type).resolveMuid(url, type);
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
            mapper = getMapper(muid.getMuidType());
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
        trackMapper.setMappingsToTracksOfRecords(persistenceData
                .getMappingsOfTracksOfRecord());
    }

}
