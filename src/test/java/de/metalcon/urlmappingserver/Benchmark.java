package de.metalcon.urlmappingserver;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidType;
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

    protected static long ID = 1;

    protected List<EntityUrlData> registered;

    public Benchmark() {
        registered = new LinkedList<EntityUrlData>();
    }

    protected void benchmark() {
        benchmarkWrite(1000000);
        benchmarkRead(10000000);
        cleanUp();
    }

    protected void benchmarkWrite(long totalWrites) {
        short type = 9;
        long crrNano = System.nanoTime();
        long skipped = 0;

        System.out.println("progress:");
        for (long write = 0; write < totalWrites; write++) {
            EntityUrlData entity = generatedData(type);
            type -= 1;
            if (type == -1) {
                type = 9;
            }
            if (entity == null) {
                skipped += 1;
                continue;
            }

            registered.add(entity);

            // register MUID
            registerMuid(entity);

            if (write % (totalWrites / 10) == 0 && write != 0) {
                System.out.println((write / (double) totalWrites * 100) + "%");
            }
        }
        System.out.println(skipped + " skipped");
        crrNano = System.nanoTime() - crrNano;
        long crrMis = crrNano / 1000;
        long crrMs = crrMis / 1000;
        System.out.println("total number: " + totalWrites);
        System.out.println("benchmark duration (write): " + crrMs + "ms");
        System.out.println("per write: " + (crrMis / totalWrites) + "µs");
        System.out.println("writes per second: " + 1000
                / (crrMs / (double) totalWrites));
    }

    protected void benchmarkRead(long totalReads) {
        long read = 0;
        long crrNano = System.nanoTime();
        Muid muid;
        System.out.println("reading from " + registered.size());

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
                muid =
                        resolveMuid(generateUrl(entity), entity.getMuid()
                                .getMuidType());
                if (muid == null) {
                    System.err.println("failed to resolve MUID ("
                            + entity.getMuid().getMuidType() + ")");
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

    abstract protected void registerMuid(EntityUrlData entity);

    abstract protected Muid resolveMuid(
            Map<String, String> urlPathVars,
            MuidType muidType);

    protected void cleanUp() {
        registered.clear();
    }

    protected static EntityUrlData generatedData(short type) {
        MuidType muidType = MuidType.parseShort(type);
        Muid muid = Muid.create(muidType);
        String name = "id" + ID;
        ID += 1;

        switch (muidType) {
            case BAND:
                return new BandUrlData(muid, name);
            case CITY:
                return new CityUrlData(muid, name);
            case EVENT:
                return new EventUrlData(muid, name, new Date(
                        System.currentTimeMillis()),
                        (CityUrlData) generatedData(MuidType.CITY
                                .getRawIdentifier()),
                        (VenueUrlData) generatedData(MuidType.VENUE
                                .getRawIdentifier()));
            case GENRE:
                return new GenreUrlData(muid, name);
            case INSTRUMENT:
                return new InstrumentUrlData(muid, name);
            case RECORD:
                // 10% of all records are collections
                if ((ID / 10) % 10 == 9) {
                    return new RecordUrlData(muid, name, null,
                            1914 + (int) (ID % 100));
                }
                return new RecordUrlData(muid, name,
                        (BandUrlData) generatedData(MuidType.BAND
                                .getRawIdentifier()), 1914 + (int) (ID % 100));
            case TOUR:
                return new TourUrlData(muid, name, 1914 + (int) (ID % 100));
            case TRACK:
                // FIXME debugging: all tracks have unknown record
                // 10% of all tracks have unknown record
                //                if ((ID / 10) % 10 == 2) {
                //                return new TrackUrlData(muid, name,
                //                        (BandUrlData) generatedData(MuidType.BAND
                //                                .getRawIdentifier()), null, 1 + (int) (ID % 30));
                //                }
                // 10% of all tracks have unknown record and unknown band
                //                if ((ID / 10) % 10 == 5) {
                //                    return new TrackUrlData(muid, name, null, null,
                //                            1 + (int) (ID % 30));
                //                }
                // 10% of remaining 80% of all records have unknown band
                //                return new TrackUrlData(muid, name, null,
                //                        (RecordUrlData) generatedData(MuidType.RECORD
                //                                .getRawIdentifier()), 1 + (int) (ID % 30));
                return null;
            case USER:
                return new UserUrlData(muid, String.valueOf(ID),
                        String.valueOf(ID));
            case VENUE:
                return new VenueUrlData(muid, name,
                        (CityUrlData) generatedData(MuidType.CITY
                                .getRawIdentifier()));
        }

        throw new UnsupportedOperationException("unknown MUID type");
    }

    protected static Map<String, String> generateUrl(EntityUrlData entity) {
        Map<String, String> pathVars = new HashMap<String, String>();
        String mapping = entity.getName() + "-" + entity.getMuid();

        BandUrlData band;
        RecordUrlData record;
        switch (entity.getMuid().getMuidType()) {

            case BAND:
                pathVars.put("pathBand", mapping);
                break;

            case CITY:
                pathVars.put("pathCity", mapping);
                break;

            case EVENT:
                pathVars.put("pathEvent", mapping);
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
                pathVars.put("pathTour", mapping);
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
