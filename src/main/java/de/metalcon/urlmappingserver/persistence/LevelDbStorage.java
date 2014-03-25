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
        DBIterator iterator = db.iterator();

        try {
            short muidType;
            Set<String> mappings;
            Muid muid;

            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                muidType =

                BUFFER.put(iterator.peekNext().getValue());
                BUFFER.flip();

                muid = new Muid(BUFFER.getLong());

                mappings = mappingsOfEntity.get(muid);
                if (mappings == null) {
                    mappings = new LinkedHashSet<String>();
                    mappingsOfEntity.put(muid, mappings);
                }

                mappings.add(mapping);
            }
        } finally {
            try {
                iterator.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return mappingsOfEntity;
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
        if (muidParent != null) {
            keyBuffer.put(MUID_BUFFER.array());
        }
        keyBuffer.put(baMapping);

        return key;
    }

    protected static String asString(byte[] bytes) {
        return new String(bytes);
    }
}
