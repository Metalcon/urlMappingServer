package de.metalcon.urlmappingserver.persistence;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;

/**
 * persistence layer based upon levelDB
 * 
 * @author sebschlicht
 * 
 */
public class LevelDbStorage implements PersistentStorage {

    /**
     * byte buffer to (de-)serialize MUID values
     */
    protected static ByteBuffer MUID_BUFFER = ByteBuffer.allocate(8);

    /**
     * byte buffer to (de-)serialize MUID types
     */
    protected static ByteBuffer MUID_TYPE_BUFFER = ByteBuffer.allocate(2);

    /**
     * levelDB to make URL mappings persistent
     */
    protected DB db;

    /**
     * create levelDB persistence layer
     * 
     * @param db
     *            levelDB to make URL mappings persistent
     */
    public LevelDbStorage(
            DB db) {
        this.db = db;
    }

    @Override
    public void saveMapping(EntityUrlData entity, String mapping) {
        // value: MUID
        MUID_BUFFER.putLong(entity.getMuid().getValue());
        byte[] value = MUID_BUFFER.array();
        MUID_BUFFER.clear();

        // key: MUID type [+ parent MUID] + mapping
        byte[] key;
        if (entity instanceof RecordUrlData) {
            RecordUrlData record = (RecordUrlData) entity;
            key =
                    buildKey(MuidType.RECORD, record.getBand().getMuid(),
                            mapping);
        } else if (entity instanceof TrackUrlData) {
            TrackUrlData track = (TrackUrlData) entity;
            key =
                    buildKey(MuidType.TRACK, track.getRecord().getMuid(),
                            mapping);
        } else {
            key = buildKey(entity.getMuid().getMuidType(), null, mapping);
        }

        db.put(key, value);
    }

    @Override
    public UrlMappingData restoreMappings() {
        Map<Muid, Set<String>> mappingsOfEntity =
                new HashMap<Muid, Set<String>>();
        Map<Muid, Map<String, Muid>> mappingsOfRecordsOfBand =
                new HashMap<Muid, Map<String, Muid>>();
        Map<Muid, Map<String, Muid>> mappingsOfTracksOfRecord =
                new HashMap<Muid, Map<String, Muid>>();
        DBIterator iterator = db.iterator();

        try {
            byte[] baMuidType = new byte[2];
            short muidType;
            byte[] baMuidParent = new byte[8];
            Muid muidParent = null;
            Set<String> currentMappings;
            Map<String, Muid> mappingLink;
            Muid muid;

            int arrayCursor;
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                arrayCursor = 0;
                byte[] key = iterator.peekNext().getKey();

                // extract MUID type
                System.arraycopy(key, arrayCursor, baMuidType, 0,
                        baMuidType.length);
                arrayCursor += baMuidType.length;
                MUID_TYPE_BUFFER.put(baMuidType);
                MUID_TYPE_BUFFER.flip();
                muidType = MUID_TYPE_BUFFER.getShort();
                MUID_TYPE_BUFFER.clear();

                if (muidType == MuidType.RECORD.getRawIdentifier()
                        || muidType == MuidType.TRACK.getRawIdentifier()) {
                    // extract parental MUID
                    System.arraycopy(key, arrayCursor, baMuidParent, 0,
                            baMuidParent.length);
                    arrayCursor += baMuidParent.length;
                    MUID_BUFFER.put(baMuidParent);
                    MUID_BUFFER.flip();
                    muidParent = new Muid(MUID_BUFFER.getLong());
                    MUID_BUFFER.clear();
                }

                // extract mapping
                byte[] baMapping = new byte[key.length - arrayCursor];
                System.arraycopy(key, arrayCursor, baMapping, 0,
                        baMapping.length);
                String mapping = asString(baMapping);

                // deserialize MUID
                MUID_BUFFER.put(iterator.peekNext().getValue());
                MUID_BUFFER.flip();
                muid = new Muid(MUID_BUFFER.getLong());
                MUID_BUFFER.clear();

                // add links between mappings
                if (muidType == MuidType.RECORD.getRawIdentifier()) {
                    // band -> record [mapping -> MUID]
                    mappingLink = mappingsOfRecordsOfBand.get(muidParent);
                    if (mappingLink == null) {
                        mappingLink = new HashMap<String, Muid>();
                        mappingsOfRecordsOfBand.put(muidParent, mappingLink);
                    }

                    mappingLink.put(mapping, muid);
                } else if (muidType == MuidType.TRACK.getRawIdentifier()) {
                    // record -> track [mapping -> MUID]
                    mappingLink = mappingsOfTracksOfRecord.get(muidParent);
                    if (mappingLink == null) {
                        mappingLink = new HashMap<String, Muid>();
                        mappingsOfTracksOfRecord.put(muidParent, mappingLink);
                    }

                    mappingLink.put(mapping, muid);
                } else {
                    // add simple URL mapping [MUID -> mapping]
                    currentMappings = mappingsOfEntity.get(muid);
                    if (currentMappings == null) {
                        currentMappings = new LinkedHashSet<String>();
                        mappingsOfEntity.put(muid, currentMappings);
                    }

                    currentMappings.add(mapping);
                }
            }
        } finally {
            try {
                iterator.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return new UrlMappingData(mappingsOfEntity, mappingsOfRecordsOfBand,
                mappingsOfTracksOfRecord);
    }

    protected static byte[] buildKey(
            MuidType muidType,
            Muid muidParent,
            String mapping) {
        byte[] key;
        int keyLength = 2;

        // prepare key parts
        MUID_TYPE_BUFFER.putShort(muidType.getRawIdentifier());
        if (muidParent != null) {
            MUID_BUFFER.putLong(muidParent.getValue());
            keyLength += 8;
        }
        byte[] baMapping = mapping.getBytes();
        keyLength += baMapping.length;

        // create key
        key = new byte[keyLength];
        ByteBuffer keyBuffer = ByteBuffer.wrap(key);

        keyBuffer.put(MUID_TYPE_BUFFER.array());
        MUID_TYPE_BUFFER.clear();
        if (muidParent != null) {
            keyBuffer.put(MUID_BUFFER.array());
            MUID_BUFFER.clear();
        }
        keyBuffer.put(baMapping);

        return key;
    }

    protected static String asString(byte[] bytes) {
        return new String(bytes);
    }
}
