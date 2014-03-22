package de.metalcon.urlmappingserver;

import java.util.Date;

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

    protected static short ID = 1;

    public static void main(String[] args) {
        String address = ADDRESS_TCP;

        CONTEXT = ZMQ.context(1);
        WORKER =
                new ZMQWorker(address, new UrlMappingRequestHandler(
                        new EntityUrlMappingManager()), CONTEXT);
        WORKER.start();

        Benchmark benchmark = new Benchmark(ADDRESS_TCP);
        benchmark.start();
    }

    public Benchmark(
            String address) {
        this.address = address;
        socket = CONTEXT.socket(ZMQ.DEALER);
        socket.connect(address);
    }

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
                                .getRawIdentifier()), ID);
            case TOUR:
                return new TourUrlData(muid, name, ID);
            case TRACK:
                return new TrackUrlData(muid, name,
                        (BandUrlData) generatedData(MuidType.BAND
                                .getRawIdentifier()),
                        (RecordUrlData) generatedData(MuidType.RECORD
                                .getRawIdentifier()), ID);
            case USER:
                return new UserUrlData(muid, String.valueOf(ID),
                        String.valueOf(ID));
            case VENUE:
                return new VenueUrlData(muid, name,
                        (CityUrlData) generatedData(MuidType.CITY
                                .getRawIdentifier()));
        }

        throw new UnsupportedOperationException("unknown entity type");
    }

}
