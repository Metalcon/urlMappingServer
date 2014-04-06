package de.metalcon.urlmappingserver;

import static org.junit.Assert.assertNull;

import org.junit.AfterClass;
import org.junit.Before;

import de.metalcon.domain.Muid;
import de.metalcon.domain.UidType;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.mappers.EntityFactory;

public abstract class EntityUrlMapperTest {

	protected static boolean READY = false;

	protected static EntityFactory FACTORY;

	private static UidType TYPE;

	protected static UidType INVALID_TYPE;

	// static
	// ----------
	// scope

	protected EntityUrlMappingManager manager;

	protected EntityUrlMapper mapper;

	protected EntityUrlData entity;

	@Before
	public void setUp() {
		if (!READY) {
			FACTORY = getFactory();
			TYPE = FACTORY.getUidType();
			INVALID_TYPE = getInvalidMuidType(TYPE);
			READY = true;
		}
		manager = new EntityUrlMappingManager();
		mapper = manager.getMapper(TYPE);
	}

	abstract protected EntityFactory getFactory();

	/**
	 * check if entity not registered yet, then register
	 */
	protected void registerEntity(final EntityUrlData entity) {
		assertNull(resolveMuid(entity, FACTORY.getMappingId(entity)));
		mapper.registerMuid(entity);
	}

	protected Muid resolveMuid(final EntityUrlData entity, final String mapping) {
		return mapper.resolveMuid(FACTORY.getUrl(entity, mapping), TYPE);
	}

	private static UidType getInvalidMuidType(final UidType validType) {
		short identifierInvalidType = (short) (validType.getRawIdentifier() + 1);
		if (identifierInvalidType == 10) {
			identifierInvalidType = 0;
		}
		return UidType.parseShort(identifierInvalidType);
	}

	@AfterClass
	public static void afterClass() {
		READY = false;
	}

}
