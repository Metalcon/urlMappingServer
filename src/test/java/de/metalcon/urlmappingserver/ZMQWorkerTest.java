package de.metalcon.urlmappingserver;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zeromq.ZMQ;

import de.metalcon.domain.EntityType;
import de.metalcon.domain.Muid;
import de.metalcon.domain.MuidConverter;
import de.metalcon.urlmappingserver.api.requests.UrlMappingRegistrationRequest;
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

public class ZMQWorkerTest {

    private static ZMQ.Context CONTEXT;

    private static ZMQWorker WORKER;

    private ZMQ.Socket socket;

    private static short ID = 1;

    private static String ADDRESS = "tcp://127.0.0.1:6001"; //"inproc:///tmp/zmqw";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        CONTEXT = ZMQ.context(1);

        WORKER =
                new ZMQWorker(ADDRESS, new UrlMappingRequestHandler(
                        new EntityUrlMappingManager()), CONTEXT);
        WORKER.start();
    }

    @Before
    public void setUp() {
        socket = CONTEXT.socket(ZMQ.DEALER);
        socket.connect(ADDRESS);
    }

    @After
    public void tearDown() {
        socket.close();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        CONTEXT.term();
    }

    protected EntityUrlData generatedData(short type) {
        Muid muid =
                new Muid(MuidConverter.generateMUID(type, (byte) 0,
                        (int) System.currentTimeMillis(), ID));
        String name = "id" + ID;
        ID += 1;

        if (EntityType.BAND.getRawIdentifier() == type) {
            return new BandUrlData(muid, name);
        } else if (EntityType.CITY.getRawIdentifier() == type) {
            return new CityUrlData(muid, name);
        } else if (EntityType.EVENT.getRawIdentifier() == type) {
            return new EventUrlData(muid, name, new Date(
                    System.currentTimeMillis()),
                    (CityUrlData) generatedData(EntityType.CITY
                            .getRawIdentifier()),
                    (VenueUrlData) generatedData(EntityType.VENUE
                            .getRawIdentifier()));
        } else if (EntityType.GENRE.getRawIdentifier() == type) {
            return new GenreUrlData(muid, name);
        } else if (EntityType.INSTRUMENT.getRawIdentifier() == type) {
            return new InstrumentUrlData(muid, name);
        } else if (EntityType.RECORD.getRawIdentifier() == type) {
            return new RecordUrlData(muid, name,
                    (BandUrlData) generatedData(EntityType.BAND
                            .getRawIdentifier()), ID);
        } else if (EntityType.TOUR.getRawIdentifier() == type) {
            return new TourUrlData(muid, name, ID);
        } else if (EntityType.TRACK.getRawIdentifier() == type) {
            return new TrackUrlData(muid, name,
                    (BandUrlData) generatedData(EntityType.BAND
                            .getRawIdentifier()),
                    (RecordUrlData) generatedData(EntityType.RECORD
                            .getRawIdentifier()), ID);
        } else if (EntityType.USER.getRawIdentifier() == type) {
            return new UserUrlData(muid, String.valueOf(ID), String.valueOf(ID));
        } else if (EntityType.VENUE.getRawIdentifier() == type) {
            return new VenueUrlData(muid, name,
                    (CityUrlData) generatedData(EntityType.CITY
                            .getRawIdentifier()));
        }
        throw new UnsupportedOperationException("unknown entity type");
    }

    @Test
    public void test() throws InterruptedException {
        List<Thread> threads = new LinkedList<Thread>();
        for (int i = 0; i < 1; i++) {
            ZMQ.Socket socketReceive = CONTEXT.socket(ZMQ.DEALER);
            socketReceive.connect(ADDRESS);
            Thread thread = new Thread(new Receive(socketReceive));
            threads.add(thread);
            thread.start();

            ZMQ.Socket socketSend = CONTEXT.socket(ZMQ.DEALER);
            socketSend.connect(ADDRESS);
            thread = new Thread(new Send(socketSend));
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

    private class Receive implements Runnable {

        private ZMQ.Socket socketReceive;

        public Receive(
                ZMQ.Socket socketReceive) {
            this.socketReceive = socketReceive;
        }

        @Override
        public void run() {
            for (int i = 0; i < 1000000; i++) {
                SerializationUtils.deserialize(socketReceive.recv());

                if (i % 10000 == 0) {
                    System.out.println(i + " received");
                }
            }
        }
    }

    private class Send implements Runnable {

        private ZMQ.Socket socketSend;

        public Send(
                ZMQ.Socket socketSend) {
            this.socketSend = socketSend;
        }

        @Override
        public void run() {
            long ms = System.currentTimeMillis();
            short type = 0;

            for (int i = 0; i < 1000000; i++) {
                EntityUrlData urlData = generatedData(type);
                UrlMappingRegistrationRequest request =
                        new UrlMappingRegistrationRequest(urlData);

                byte[] serRequest = SerializationUtils.serialize(request);
                socketSend.send(serRequest);

                type += 1;
                if (type == 10) {
                    type = 0;
                }
                if (i % 10000 == 0) {
                    System.out.println(i + " sent");
                }
            }
            System.out.println("done: " + (System.currentTimeMillis() - ms)
                    + "ms");
        }

    }
}
