package de.metalcon.urlmappingserver;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.zeromq.ZMQ;

import de.metalcon.domain.Muid;
import de.metalcon.urlmappingserver.api.requests.UrlMappingRegistrationRequest;
import de.metalcon.urlmappingserver.api.requests.registration.BandUrlData;
import de.metalcon.zmqworker.ZMQWorker;

public class ZMQWorkerTest {

    private static ZMQ.Context CONTEXT;

    private static ZMQWorker WORKER;

    private ZMQ.Socket socket;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        CONTEXT = ZMQ.context(1);
        WORKER =
                new ZMQWorker("tcp://*:6002", new UrlMappingRequestHandler(
                        new EntityUrlMappingManager()));

        new Thread(new Runnable() {

            @Override
            public void run() {
                WORKER.doWork();
            }
        }).start();
    }

    @Before
    public void setUp() {
        socket = CONTEXT.socket(ZMQ.REQ);
        socket.connect("tcp://127.0.0.1:6002");
    }

    @After
    public void tearDown() {
        socket.close();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        CONTEXT.term();
    }

    @Test
    public void test() {
        BandUrlData bandUrlData = new BandUrlData(new Muid(2), "Testy");
        UrlMappingRegistrationRequest request =
                new UrlMappingRegistrationRequest(bandUrlData);

        socket.send(SerializationUtils.serialize(request));

        Object o = SerializationUtils.deserialize(socket.recv());

        System.out.println(o);
    }

}
