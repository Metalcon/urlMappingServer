package de.metalcon.urlmappingserver;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.zeromq.ZMQ;

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
import de.metalcon.zmqworker.ZMQWorker;

public class Benchmark {

    protected static final String ADDRESS_TCP = "tcp://127.0.0.1:6001";

    protected static final String ADDRESS_INPROC = "inproc:///tmp/zmqw";

    protected String address;

    protected static ZMQ.Context CONTEXT;

    protected static ZMQWorker WORKER;

    protected ZMQ.Socket socket;

    protected static long ID = 1;

    //    public static void main(String[] args) {
    //        String address = ADDRESS_TCP;
    //
    //        CONTEXT = ZMQ.context(1);
    //        WORKER =
    //                new ZMQWorker(address, new UrlMappingRequestHandler(
    //                        new EntityUrlMappingManager()), CONTEXT);
    //        WORKER.start();
    //
    //        Benchmark benchmark = new Benchmark(ADDRESS_TCP);
    //        benchmark.start();
    //    }

    //    public Benchmark(
    //            String address) {
    //        this.address = address;
    //        socket = CONTEXT.socket(ZMQ.DEALER);
    //        socket.connect(address);
    //    }

    public void start() {

        // TODO: send/receive via DEALER socket

        socket.close();
        CONTEXT.term();
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
                return new RecordUrlData(muid, name,
                        (BandUrlData) generatedData(MuidType.BAND
                                .getRawIdentifier()), 1914 + (int) (ID % 100));
            case TOUR:
                return new TourUrlData(muid, name, 1914 + (int) (ID % 100));
            case TRACK:
                return new TrackUrlData(muid, name, null,
                        (RecordUrlData) generatedData(MuidType.RECORD
                                .getRawIdentifier()), 1 + (int) (ID % 30));
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

    public static void main(String[] args) {
        short type = 9;
        EntityUrlMappingManager manager = new EntityUrlMappingManager();

        long totalWrites = 1000000;
        long crrNano = System.nanoTime();
        List<EntityUrlData> registered = new LinkedList<EntityUrlData>();

        System.out.println("progress:");
        for (long write = 0; write < totalWrites; write++) {
            EntityUrlData entity = generatedData(type);
            registered.add(entity);
            manager.registerMuid(entity);

            type -= 1;
            if (type == -1) {
                type = 9;
            }

            if (write % (totalWrites / 10) == 0 && write != 0) {
                System.out.println((write / (double) totalWrites * 100) + "%");
            }
        }
        crrNano = System.nanoTime() - crrNano;
        long crrMis = crrNano / 1000;
        long crrMs = crrMis / 1000;
        System.out.println("benchmark duration (write): " + crrMs + "ms");
        System.out.println("per write: " + (crrMis / totalWrites) + "µs");
        System.out.println("writes per second: " + 1000
                / (crrMs / (double) totalWrites));

        long totalReads = 10000000;
        long read = 0;
        crrNano = System.nanoTime();
        Muid muid;
        while (true) {
            for (EntityUrlData entity : registered) {
                if (read++ >= totalReads) {
                    break;
                }
                muid =
                        manager.resolveMuid(generateUrl(entity), entity
                                .getMuid().getMuidType());
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
        crrMis = crrNano / 1000;
        crrMs = crrMis / 1000;
        System.out.println("benchmark duration (read): " + crrMs + "ms");
        System.out.println("per read: " + (crrNano / totalReads) + "ns");
        System.out.println("reads per second: " + 1000
                / (crrMs / (double) totalReads));
    }

    protected static Map<String, String> generateUrl(EntityUrlData entity) {
        Map<String, String> pathVars = new HashMap<String, String>();

        String mapping = entity.getName() + "-" + entity.getMuid();
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
                BandUrlData band = ((RecordUrlData) entity).getBand();
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
                RecordUrlData record = ((TrackUrlData) entity).getRecord();
                pathVars.put("pathBand", record.getBand().getName() + "-"
                        + record.getBand().getMuid());
                pathVars.put("pathRecord",
                        record.getName() + "-" + record.getMuid());
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
