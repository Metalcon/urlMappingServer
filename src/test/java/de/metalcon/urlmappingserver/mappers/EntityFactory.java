package de.metalcon.urlmappingserver.mappers;

import java.util.HashMap;
import java.util.Map;

import de.metalcon.domain.UidType;
import de.metalcon.urlmappingserver.EntityUrlMapper;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;

public abstract class EntityFactory {

	protected static final String PATH_SEPARATOR = EntityUrlMapper.PATH_SEPARATOR;

	protected static final String EMPTY_ENTITY = EntityUrlMapper.EMPTY_ENTITY;

	protected static final String WORD_SEPARATOR = EntityUrlMapper.WORD_SEPARATOR;

	protected final String urlPathVarName;

	protected UidType uidType;

	protected int crrEntityId;

	public EntityFactory(final String urlPathVarName, final UidType muidType) {
		this.urlPathVarName = urlPathVarName;
		this.uidType = muidType;
		crrEntityId = 1;
	}

	public UidType getUidType() {
		return uidType;
	}

	public String getMappingId(final EntityUrlData entity) {
		return getMappingName(entity) + WORD_SEPARATOR + entity.getMuid();
	}

	public String getMappingName(final EntityUrlData entity) {
		return EntityUrlMapper.convertToUrlText(entity.getName());
	}

	public Map<String, String> getUrl(final EntityUrlData entity,
			final String mapping) {
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
