package de.metalcon.urlmappingserver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.metalcon.domain.Muid;
import de.metalcon.domain.UidType;
import de.metalcon.testing.MuidFactory;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.CityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EntityUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.EventUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.GenreUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.InstrumentUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.RecordUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TourUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.TrackUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.UserUrlData;
import de.metalcon.urlmappingserver.api.requests.registration.VenueUrlData;

public abstract class Benchmark {

    protected static Random RAND = new Random();

    protected static long ID = 1;

    protected UrlMappingServerConfig CONFIG = new UrlMappingServerConfig(
            "test.url-mapping-server-config.txt");

    protected UrlMappingServer server;

    protected List<EntityUrlData> registered;

    public Benchmark() {
        registered = new LinkedList<EntityUrlData>();
    }

    abstract protected void registerMuid(EntityUrlData entity);

    abstract protected Muid resolveMuid(
            Map<String, String> urlPathVars,
            UidType muidType);

    protected void benchmark(final int numWrites, final int numReads) {
        benchmarkWrite(numWrites);
        benchmarkRead(numReads);
    }

    protected void benchmarkWrite(final long totalWrites) {
        short type = 9;
        long crrNano = System.nanoTime();

        long trackWoBand = 0;
        long trackWoRecord = 0;
        long trackAno = 0;

        System.out.println("progress:");
        for (long write = 0; write < totalWrites; write++) {
            EntityUrlData entity = generatedData(type);
            type -= 1;
            if (type == -1) {
                type = 9;
            }
            if (entity == null) {
                continue;
            }

            registered.add(entity);

            // register MUID
            registerMuid(entity);

            if (entity instanceof TrackUrlData) {
                TrackUrlData track = (TrackUrlData) entity;
                if (track.getRecord().hasEmptyMuid()) {
                    if (track.getRecord().getBand().hasEmptyMuid()) {
                        trackAno += 1;
                    } else {
                        trackWoRecord += 1;
                    }
                } else {
                    if (track.getRecord().getBand().hasEmptyMuid()) {
                        trackWoBand += 1;
                    }
                }
            }

            if (write % (totalWrites / 10) == 0 && write != 0) {
                System.out.println((write / (double) totalWrites * 100) + "%");
            }
        }
        System.out.println(trackWoBand + " tracks without band");
        System.out.println(trackWoRecord + " tracks without record");
        System.out.println(trackAno + " tracks with neither band nor record");
        System.out.println();

        crrNano = System.nanoTime() - crrNano;
        long crrMis = crrNano / 1000;
        long crrMs = crrMis / 1000;
        System.out.println("total number: " + totalWrites);
        System.out.println("benchmark duration (write): " + crrMs + "ms");
        System.out.println("per write: " + (crrMis / totalWrites) + "µs");
        System.out.println("writes per second: " + 1000
                / (crrMs / (double) totalWrites));
    }

    protected void benchmarkRead(final long totalReads) {
        long read = 0;
        long crrNano = System.nanoTime();
        Muid muid;
        System.out.println("reading from " + registered.size());

        Map<String, String> url;
        while (true) {
            for (EntityUrlData entity : registered) {
                if (read % (totalReads / 10) == 0 && read != 0) {
                    System.out
                            .println((read / (double) totalReads * 100) + "%");
                }
                if (read++ >= totalReads) {
                    break;
                }

                // resolve MUID
                url = generateUrl(entity);
                muid = resolveMuid(url, entity.getMuid().getType());

                if (muid == null) {
                    System.err.println("failed to resolve MUID ("
                            + entity.getMuid().getType() + ") from ");
                    for (String pathVar : url.keySet()) {
                        System.out.println(pathVar + " = " + url.get(pathVar));
                    }
                    return;
                }
            }
            if (read >= totalReads) {
                break;
            }
        }

        crrNano = System.nanoTime() - crrNano;
        long crrMis = crrNano / 1000;
        long crrMs = crrMis / 1000;
        System.out.println("total number: " + totalReads);
        System.out.println("benchmark duration (read): " + crrMs + "ms");
        System.out.println("per read: " + (crrNano / totalReads) + "ns");
        System.out.println("reads per second: " + 1000
                / (crrMs / (double) totalReads));
    }

    protected void cleanUp() {
        registered.clear();
        try {
            server.close();
        } catch (RuntimeException e) {
            // server shutted down
        }
        System.out.println("closed");
    }

    protected static EntityUrlData generatedData(final short type) {
        UidType uidType = UidType.parseShort(type);
        Muid muid = MuidFactory.generateMuid(uidType);
        String name = "id" + ID;
        ID += 1;

        switch (uidType) {
            case BAND:
                return new BandUrlData(muid, name);
            case CITY:
                return new CityUrlData(muid, name);
            case EVENT:
                return new EventUrlData(muid);
            case GENRE:
                return new GenreUrlData(muid, name);
            case INSTRUMENT:
                return new InstrumentUrlData(muid, name);
            case RECORD:
                // 10% of all records are collections
                if (RAND.nextInt(100) < 10) {
                    return new RecordUrlData(muid, name, null,
                            1914 + (int) (ID % 100));
                }
                return new RecordUrlData(muid, name,
                        (BandUrlData) generatedData(UidType.BAND
                                .getRawIdentifier()), 1914 + (int) (ID % 100));
            case TOUR:
                return new TourUrlData(muid);
            case TRACK:
                // 10% of all tracks have unknown record
                if (RAND.nextInt(100) < 10) {
                    return new TrackUrlData(muid, name,
                            (BandUrlData) generatedData(UidType.BAND
                                    .getRawIdentifier()), null,
                            1 + (int) (ID % 30));
                }
                // 10% of all tracks have unknown record and unknown band
                if (RAND.nextInt(100) < 10) {
                    return new TrackUrlData(muid, name, null, null,
                            1 + (int) (ID % 30));
                }
                // 10% of remaining 80% of all records have unknown band
                return new TrackUrlData(muid, name, null,
                        (RecordUrlData) generatedData(UidType.RECORD
                                .getRawIdentifier()), 1 + (int) (ID % 30));
            case USER:
                return new UserUrlData(muid, String.valueOf(ID),
                        String.valueOf(ID));
            case VENUE:
                return new VenueUrlData(muid, name,
                        (CityUrlData) generatedData(UidType.CITY
                                .getRawIdentifier()));
            default:
                throw new UnsupportedOperationException("unknown MUID type");
        }

    }

    protected static Map<String, String>
        generateUrl(final EntityUrlData entity) {
        Map<String, String> pathVars = new HashMap<String, String>();
        String mapping = entity.getName() + "-" + entity.getMuid();

        BandUrlData band;
        RecordUrlData record;
        switch (entity.getMuid().getType()) {

            case BAND:
                pathVars.put("pathBand", mapping);
                break;

            case CITY:
                pathVars.put("pathCity", mapping);
                break;

            case EVENT:
                pathVars.put("pathEvent", entity.getMuid().toString());
                break;

            case GENRE:
                pathVars.put("pathGenre", mapping);
                break;

            case INSTRUMENT:
                pathVars.put("pathInstrument", mapping);
                break;

            case RECORD:
                band = ((RecordUrlData) entity).getBand();
                if (!band.hasEmptyMuid()) {
                    pathVars.put("pathBand",
                            band.getName() + "-" + band.getMuid());
                } else {
                    pathVars.put("pathBand", "_");
                }
                pathVars.put("pathRecord", mapping);
                break;

            case TOUR:
                pathVars.put("pathTour", entity.getMuid().toString());
                break;

            case TRACK:
                record = ((TrackUrlData) entity).getRecord();
                band = record.getBand();
                if (!band.hasEmptyMuid()) {
                    pathVars.put("pathBand",
                            band.getName() + "-" + band.getMuid());
                } else {
                    pathVars.put("pathBand", "_");
                }
                if (!record.hasEmptyMuid()) {
                    pathVars.put("pathRecord",
                            record.getName() + "-" + record.getMuid());
                } else {
                    pathVars.put("pathRecord", "_");
                }
                pathVars.put("pathTrack", mapping);
                break;

            case USER:
                UserUrlData user = (UserUrlData) entity;
                pathVars.put("pathUser",
                        user.getFirstName() + "-" + user.getLastName() + "-"
                                + user.getMuid());
                break;

            case VENUE:
                pathVars.put("pathVenue", mapping);
                break;

            default:
                throw new UnsupportedOperationException("unknown MUID type");

        }

        return pathVars;
    }
}
