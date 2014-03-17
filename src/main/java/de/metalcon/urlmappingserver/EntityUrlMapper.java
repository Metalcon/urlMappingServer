package de.metalcon.urlmappingserver;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import de.metalcon.domain.EntityType;
import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

/**
 * basic URL mapper for Metalcon entities
 * 
 * @author sebschlicht
 * 
 */
public abstract class EntityUrlMapper implements MetalconUrlMapper {

    protected static String WORD_SEPERATOR = "-";

    protected static String EMPTY_ENTITY = "_";

    /**
     * URL mapping manager to resolve other MUIDs
     */
    protected EntityUrlMappingManager manager;

    /**
     * type of the entities this mapper handles
     */
    protected EntityType entityType;

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
     * @param entityType
     *            type of the entities this mapper handles
     * @param urlPathVarName
     *            name of the path variable
     */
    public EntityUrlMapper(
            EntityUrlMappingManager manager,
            EntityType entityType,
            String urlPathVarName) {
        this.manager = manager;
        this.entityType = entityType;
        mappingsOfEntities = new HashMap<Muid, Set<String>>();
        mappingToEntity = new HashMap<String, Muid>();
    }

    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        Set<String> mapping = createEmptyMappingSet();

        // add mapping: /<entity name>
        String name = convertToUrlText(entityUrlData.getName());
        mapping.add(name);

        return mapping;
    }

    @Override
    public void registerMuid(EntityUrlData entityUrlData) {
        registerMappings(entityUrlData.getMuid(), createMapping(entityUrlData));
    }

    @Override
    public Muid resolveMuid(Map<String, String> url, EntityType type) {
        String mapping = getPathVar(url, urlPathVarName);
        return mappingToEntity.get(mapping);
    }

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

        // TODO: log mapping
    }

    protected void
        registerMappings(Muid muid, Set<String> newMappingsForEntity) {

        // add first mapping with MUID
        String muidMapping =
                newMappingsForEntity.iterator().next() + WORD_SEPERATOR + muid;
        registerMapping(muidMapping, muid);

        // add further mappings without MUID if not in use yet
        for (String mapping : newMappingsForEntity) {
            if (!mappingToEntity.containsKey(mapping)) {
                registerMapping(mapping, muid);
            }
        }
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
    protected Muid resolveOtherMuid(Map<String, String> url, EntityType type) {
        return manager.getMapper(type).resolveMuid(url, type);
    }

    /**
     * convert any text to a string valid for an URL
     * 
     * @param text
     *            text to be converted
     * @return valid URL text
     */
    protected static String convertToUrlText(String text) {
        String urlText = text;
        // Remove non letter characters. (http://stackoverflow.com/questions/1611979/remove-all-non-word-characters-from-a-string-in-java-leaving-accented-charact)
        urlText = urlText.replaceAll("[^\\p{L}\\p{Nd} ]", "");
        // Convert whitespace to WORD_SEPERATOR
        urlText = urlText.trim();
        urlText = urlText.replaceAll("\\s+", WORD_SEPERATOR);
        return urlText;
    }

    protected static String getPathVar(Map<String, String> url, String pathVar) {
        String value = url.get(pathVar);
        if (value != null) {
            return value;
        }
        throw new IllegalStateException("missing path variable \"" + pathVar
                + "\"");
    }

    protected static Set<String> createEmptyMappingSet() {
        return new LinkedHashSet<String>();
    }

}
