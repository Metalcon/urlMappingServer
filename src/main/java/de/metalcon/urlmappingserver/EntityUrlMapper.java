package de.metalcon.urlmappingserver;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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

    /**
     * all mappings of an entity
     */
    protected Map<Muid, Set<String>> mappingsOfEntities;

    /**
     * entity a single mapping leads to
     */
    protected Map<String, Muid> mappingToEntity;

    public EntityUrlMapper() {
        mappingsOfEntities = new HashMap<Muid, Set<String>>();
        mappingToEntity = new HashMap<String, Muid>();
    }

    protected Set<String> createMapping(EntityUrlData entityUrlData) {
        Set<String> mapping = createEmptyMappingSet();
        String name = convertToUrlText(entityUrlData.getName());
        mapping.add(name);
        return mapping;
    }

    @Override
    protected void registerMuid(EntityUrlData entityUrlData) {

        Set<String> newMappingsForEntity = createMapping(entityUrlData);
        Set<String> existingMappingsForEntity =
                mappingsOfEntities.get(entityUrlData.getMuid());

    }

    protected void
        registerMappings(Muid muid, Set<String> newMappingsForEntity) {
        // get existing mappings of this entity
        Set<String> existingMappingsForEntity = mappingsOfEntities.get(muid);
        if (existingMappingsForEntity == null) {
            existingMappingsForEntity = createEmptyMappingSet();
        }

        // add first mapping with MUID
        String muidMapping =
                newMappingsForEntity.iterator().next() + WORD_SEPERATOR + muid;
        existingMappingsForEntity.add(muidMapping);
        mappingToEntity.put(muidMapping, muid);

        // add further mappings without MUID if not in use yet
        for (String mapping : newMappingsForEntity) {
            // TODO
        }
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

    protected static Set<String> createEmptyMappingSet() {
        return new LinkedHashSet<String>();
    }

}
