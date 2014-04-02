package de.metalcon.urlmappingserver.mappers;

import java.util.HashMap;
import java.util.Map;

import de.metalcon.domain.MuidType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public abstract class EntityFactory {

    protected static final String PATH_SEPARATOR =
            EntityUrlMapper.PATH_SEPARATOR;

    protected static final String EMPTY_ENTITY = EntityUrlMapper.EMPTY_ENTITY;

    protected static final String WORD_SEPARATOR =
            EntityUrlMapper.WORD_SEPARATOR;

    protected final String urlPathVarName;

    protected MuidType muidType;

    protected int crrEntityId;

    public EntityFactory(
            String urlPathVarName,
            MuidType muidType) {
        this.urlPathVarName = urlPathVarName;
        this.muidType = muidType;
        crrEntityId = 1;
    }

    public MuidType getMuidType() {
        return muidType;
    }

    public String getMappingId(EntityUrlData entity) {
        return getMappingName(entity) + WORD_SEPARATOR + entity.getMuid();
    }

    public String getMappingName(EntityUrlData entity) {
        return EntityUrlMapper.convertToUrlText(entity.getName());
    }

    public Map<String, String> getUrl(EntityUrlData entity, String mapping) {
        Map<String, String> url = new HashMap<String, String>();
        url.put(urlPathVarName, mapping);
        return url;
    }

    /**
     * create unique entity
     */
    abstract public EntityUrlData getEntityFull();

    /**
     * create copy of another entity
     */
    abstract public EntityUrlData getEntityIdentical(EntityUrlData entity);

}
