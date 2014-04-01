package de.metalcon.urlmappingserver;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.persistence.PersistentStorage;

/**
 * basic URL mapper for Metalcon entities
 * 
 * @author sebschlicht, Lukas Schmelzeisen
 * 
 */
public abstract class EntityUrlMapper implements MetalconUrlMapper {

    /**
     * URL path separator
     */
    public static String PATH_SEPARATOR = "/";

    /**
     * word separator in URL mappings
     */
    public static String WORD_SEPARATOR = "-";

    /**
     * placeholder for missing entity
     */
    public static String EMPTY_ENTITY = "_";

    /**
     * server log
     */
    protected static final Logger LOG = LoggerFactory
            .getLogger(EntityUrlMapper.class);

    /**
     * URL mapping manager to resolve other MUIDs
     */
    protected EntityUrlMappingManager manager;

    /**
     * storage to make mapping persistent
     */
    protected PersistentStorage persistentStorage;

    /**
     * type of the entities this mapper handles
     */
    protected MuidType muidType;

    /**
     * empty MUID flag
     */
    protected boolean allowEmptyMuid;

    /**
     * name of the path variable
     */
    protected String urlPathVarName;

    /**
     * all mappings of an entity
     */
    protected Map<Muid, Set<String>> mappingsOfEntities;

    /**
     * entity a single mapping leads to
     */
    protected Map<String, Muid> mappingToEntity;

    /**
     * create a new basic URL mapper
     * 
     * @param manager
     *            URL mapping manager to resolve other MUIDs
     * @param muidType
     *            type of the entities this mapper handles
     * @param allowEmptyMuid
     *            empty MUID flag
     * @param urlPathVarName
     *            name of the path variable
     */
    public EntityUrlMapper(
            EntityUrlMappingManager manager,
            MuidType muidType,
            boolean allowEmptyMuid,
            String urlPathVarName) {
        this.manager = manager;
        persistentStorage = manager.getPersistentStorage();
        this.muidType = muidType;
        this.allowEmptyMuid = allowEmptyMuid;
        this.urlPathVarName = urlPathVarName;
        mappingsOfEntities = new HashMap<Muid, Set<String>>();
        mappingToEntity = new HashMap<String, Muid>();
    }

    /**
     * @return type of the entities this mapper handles
     */
    public MuidType getMuidType() {
        return muidType;
    }

    /**
     * @return name of the path variable
     */
    public String getUrlPathVarName() {
        return urlPathVarName;
    }

    /**
     * create new mappings for the entity
     * 
     * @param entityUrlData
     *            URL data needed to register this entity
     * @return set of new mappings
     * @throws IllegalArgumentException
     *             if entity type not handled
     */
    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        if (entityUrlData.getMuid().getMuidType() != muidType) {
            throw new IllegalArgumentException("mapper handles muid type \""
                    + getMuidType() + "\" only (was: \""
                    + entityUrlData.getMuid().getMuidType() + "\")");
        }

        Set<String> mapping = createEmptyMappingSet();

        // add mapping: /<entity name>
        String nameMapping = convertToUrlText(entityUrlData.getName());
        mapping.add(nameMapping);

        // add mapping: /<entity name>-<muid>
        String uniqueMapping =
                nameMapping + WORD_SEPARATOR + entityUrlData.getMuid();
        mapping.add(uniqueMapping);

        return mapping;
    }

    @Override
    public void registerMuid(EntityUrlData entityUrlData) {
        registerUnregisteredMappings(entityUrlData,
                createMapping(entityUrlData));
    }

    @Override
    public Muid resolveMuid(Map<String, String> url, MuidType type) {
        if (type == muidType) {
            String mapping = getPathVar(url, urlPathVarName);
            return mappingToEntity.get(mapping);
        }
        throw new IllegalArgumentException("mapper handles muid type \""
                + getMuidType() + "\" only (was: \"" + type + "\")");
    }

    @Override
    public String resolveUrl(Muid muid) {
        Set<String> mappingsOfEntity = mappingsOfEntities.get(muid);
        if (mappingsOfEntity != null) {
            // TODO internal server error if empty?
            return mappingsOfEntity.iterator().next();
        }
        return null;
    }

    /**
     * register unregistered mappings for an entity
     * 
     * @param entity
     *            URL data needed to register this entity
     * @param newMappingsForEntity
     *            mappings to be registered
     * @throws IllegalArgumentException
     *             if MUID was empty while empty MUID flag unset
     */
    protected void registerUnregisteredMappings(
            EntityUrlData entity,
            Set<String> newMappingsForEntity) {
        if (!entity.hasEmptyMuid()) {
            // add further mappings without MUID if not in use yet
            for (String mapping : newMappingsForEntity) {
                if (!mappingToEntity.containsKey(mapping)) {
                    registerMapping(mapping, entity.getMuid());

                    // make mapping persistent
                    if (persistentStorage != null) {
                        storeMapping(entity, mapping);
                    }
                }
            }
        } else if (allowEmptyMuid) {
            registerMapping(EMPTY_ENTITY, Muid.EMPTY_MUID);
        } else {
            throw new IllegalArgumentException("empty MUID not allowed");
        }
    }

    /**
     * make a mapping persistent
     * 
     * @param entity
     *            URL data needed to register this entity
     * @param mapping
     *            mapping to be made persistent
     */
    protected void storeMapping(EntityUrlData entity, String mapping) {
        // MUID can not be empty here, parental MUID remains unset
        persistentStorage.saveMapping(entity.getMuid().getMuidType()
                .getRawIdentifier(), entity.getMuid().getValue(), mapping, 0);
    }

    /**
     * register a single mapping
     * 
     * @param mapping
     *            mapping to be registered
     * @param muid
     *            MUID the mapping refers to
     */
    protected void registerMapping(String mapping, Muid muid) {
        // get existing mappings of this entity
        Set<String> existingMappingsForEntity = mappingsOfEntities.get(muid);
        if (existingMappingsForEntity == null) {
            // add empty set if no mappings yet
            existingMappingsForEntity = createEmptyMappingSet();
            mappingsOfEntities.put(muid, existingMappingsForEntity);
        }

        existingMappingsForEntity.add(mapping);
        mappingToEntity.put(mapping, muid);

        // log mapping
        LOG.debug("new mapping: " + muid + "\"" + mapping + "\"");
    }

    /**
     * resolve a MUID with an other entity type
     * 
     * @param url
     *            registered URL
     * @param type
     *            entity type matching to the MUID
     * @return MUID registered for the URL
     */
    protected Muid resolveOtherMuid(Map<String, String> url, MuidType type) {
        return manager.resolveMuid(url, type);
    }

    /**
     * convert any text to a string valid for an URL
     * 
     * @param text
     *            text to be converted
     * @return valid URL text
     */
    public static String convertToUrlText(String text) {
        String urlText = text;
        // Remove non letter characters. (http://stackoverflow.com/questions/1611979/remove-all-non-word-characters-from-a-string-in-java-leaving-accented-charact)
        urlText = urlText.replaceAll("[^\\p{L}\\p{Nd} ]", "");
        // Convert whitespace to WORD_SEPERATOR
        urlText = urlText.trim();
        urlText = urlText.replaceAll("\\s+", WORD_SEPARATOR);
        return urlText;
    }

    /**
     * read value of a path variable
     * 
     * @param url
     *            path variables in URL
     * @param pathVar
     *            name of the path variable
     * @return value of the path variable
     * @throws IllegalStateException
     *             if path variable not contained in URL
     */
    protected static String getPathVar(Map<String, String> url, String pathVar) {
        String value = url.get(pathVar);
        if (value != null) {
            return value;
        }
        throw new IllegalStateException("missing path variable \"" + pathVar
                + "\"");
    }

    /**
     * create empty set to add mappings to
     * 
     * @return empty set
     */
    protected static Set<String> createEmptyMappingSet() {
        return new LinkedHashSet<String>();
    }

}
