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

    protected static byte[] BA_MUID = new byte[8];

    /**
     * byte buffer to (de-)serialize MUID types
     */
    protected static ByteBuffer MUID_TYPE_BUFFER = ByteBuffer.allocate(2);

    protected static byte[] BA_MUID_TYPE = new byte[2];

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
    public void saveMapping(
            short muidType,
            long muidValue,
            String mapping,
            long muidParentValue) {
        // key: MUID type + parental MUID + mapping
        byte[] key = buildKey(muidType, muidParentValue, mapping);

        // value: MUID
        MUID_BUFFER.putLong(muidValue);
        byte[] value = MUID_BUFFER.array();
        MUID_BUFFER.clear();

        db.put(key, value);
    }

    @Override
    public void saveParent(long muidValue, long muidParentValue) {
        // key: MUID
        MUID_BUFFER.putLong(muidValue);
        byte[] key = MUID_BUFFER.array();
        MUID_BUFFER.clear();

        // value: parental MUID
        MUID_BUFFER.putLong(muidParentValue);
        byte[] value = MUID_BUFFER.array();
        MUID_BUFFER.clear();

        db.put(key, value);

        System.out.println("storing parent: " + muidValue + " -> "
                + muidParentValue + " [" + new Muid(muidValue).getMuidType()
                + "]");
    }

    @Override
    public UrlMappingPersistenceData restoreMappings() {
        Map<Muid, Set<String>> mappingsOfEntities =
                new HashMap<Muid, Set<String>>();
        Map<Muid, Map<String, Muid>> mappingsOfRecordsOfBand =
                new HashMap<Muid, Map<String, Muid>>();
        Map<Muid, Map<String, Muid>> mappingsOfTracksOfRecord =
                new HashMap<Muid, Map<String, Muid>>();
        Map<Muid, Muid> parentalBands = new HashMap<Muid, Muid>();
        DBIterator iterator = db.iterator();

        try {
            short muidType;
            long muidParentValue;
            Muid muidParent;
            byte[] value;
            long muidValue;
            Muid muid;

            Set<String> mappingsOfEntity;
            Map<String, Muid> mappingsToEntity;

            int arrayCursor;
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                arrayCursor = 0;
                byte[] key = iterator.peekNext().getKey();
                value = iterator.peekNext().getValue();

                // parent entry
                if (key.length == 8) {
                    muid = new Muid(extractMuid(key, 0));
                    muidParent = new Muid(extractMuid(value, 0));
                    if (muidParent.getMuidType() == MuidType.BAND) {
                        parentalBands.put(muid, muidParent);
                    }

                    continue;
                }

                // extract MUID type
                muidType = extractMuidType(key, arrayCursor);
                arrayCursor += BA_MUID_TYPE.length;

                // extract parental MUID
                muidParentValue = extractMuid(key, arrayCursor);
                arrayCursor += BA_MUID.length;

                // extract mapping
                String mapping = extractMapping(key, arrayCursor);

                // deserialize MUID
                muidValue = deserializeMuidValue(value);
                muid = new Muid(muidValue);

                //                System.out.println("[" + muidType + "] " + muid + " ("
                //                        + mapping + ") @ " + muidParentValue);

                // check if empty MUID and allowed
                if (muidType != MuidType.BAND.getRawIdentifier()
                        && muidType != MuidType.RECORD.getRawIdentifier()
                        && muid.equals(Muid.EMPTY_MUID)) {
                    throw new IllegalStateException(
                            "empty MUID not allowed for MUID type with value \""
                                    + muidType + "\"");
                }

                // add links between mappings
                if (muidType == MuidType.RECORD.getRawIdentifier()) {
                    // band -> record mapping
                    muidParent = new Muid(muidParentValue);
                    mappingsToEntity = mappingsOfRecordsOfBand.get(muidParent);
                    if (mappingsToEntity == null) {
                        mappingsToEntity = new HashMap<String, Muid>();
                        mappingsOfRecordsOfBand.put(muidParent,
                                mappingsToEntity);
                    }

                    // mapping -> record MUID
                    mappingsToEntity.put(mapping, muid);
                } else if (muidType == MuidType.TRACK.getRawIdentifier()) {
                    // record -> track mapping
                    muidParent = new Muid(muidParentValue);
                    mappingsToEntity = mappingsOfTracksOfRecord.get(muidParent);
                    if (mappingsToEntity == null) {
                        mappingsToEntity = new HashMap<String, Muid>();
                        mappingsOfTracksOfRecord.put(muidParent,
                                mappingsToEntity);
                    }

                    // mapping -> track MUID
                    mappingsToEntity.put(mapping, muid);
                } else {
                    // MUID parent ignored

                    // add simple URL mapping [MUID -> mapping]
                    mappingsOfEntity = mappingsOfEntities.get(muid);
                    if (mappingsOfEntity == null) {
                        mappingsOfEntity = new LinkedHashSet<String>();
                        mappingsOfEntities.put(muid, mappingsOfEntity);
                    }

                    mappingsOfEntity.add(mapping);
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

        return new UrlMappingPersistenceData(mappingsOfEntities,
                mappingsOfRecordsOfBand, mappingsOfTracksOfRecord,
                parentalBands);
    }

    protected static short extractMuidType(byte[] key, int arrayCursor) {
        System.arraycopy(key, arrayCursor, BA_MUID_TYPE, 0, BA_MUID_TYPE.length);
        MUID_TYPE_BUFFER.put(BA_MUID_TYPE);
        MUID_TYPE_BUFFER.flip();
        short muidType = MUID_TYPE_BUFFER.getShort();
        MUID_TYPE_BUFFER.clear();
        return muidType;
    }

    protected static long extractMuid(byte[] key, int arrayCursor) {
        System.arraycopy(key, arrayCursor, BA_MUID, 0, BA_MUID.length);
        return deserializeMuidValue(BA_MUID);
    }

    protected static long deserializeMuidValue(byte[] baMuidValue) {
        MUID_BUFFER.put(baMuidValue);
        MUID_BUFFER.flip();
        long muidValue = MUID_BUFFER.getLong();
        MUID_BUFFER.clear();
        return muidValue;
    }

    protected static String extractMapping(byte[] key, int arrayCursor) {
        byte[] baMapping = new byte[key.length - arrayCursor];
        System.arraycopy(key, arrayCursor, baMapping, 0, baMapping.length);
        return asString(baMapping);
    }

    protected static byte[] buildKey(
            short muidType,
            long muidParentValue,
            String mapping) {
        // prepare key parts
        int keyLength = 10;
        byte[] baMapping = mapping.getBytes();
        keyLength += baMapping.length;
        byte[] key = new byte[keyLength];
        ByteBuffer keyBuffer = ByteBuffer.wrap(key);

        // MUID type
        MUID_TYPE_BUFFER.putShort(muidType);
        keyBuffer.put(MUID_TYPE_BUFFER.array());
        MUID_TYPE_BUFFER.clear();

        // MUID of parent
        MUID_BUFFER.putLong(muidParentValue);
        keyBuffer.put(MUID_BUFFER.array());
        MUID_BUFFER.clear();

        // mapping   
        keyBuffer.put(baMapping);

        return key;
    }

    protected static String asString(byte[] bytes) {
        return new String(bytes);
    }
}
