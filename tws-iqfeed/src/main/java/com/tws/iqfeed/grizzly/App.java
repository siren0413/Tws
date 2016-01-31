package com.tws.iqfeed.grizzly;

import org.glassfish.grizzly.*;
import org.glassfish.grizzly.asyncqueue.WritableMessage;
import org.glassfish.grizzly.connectionpool.SingleEndpointPool;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.glassfish.grizzly.memory.BuffersBuffer;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.glassfish.grizzly.utils.DelayedExecutor;
import org.glassfish.grizzly.utils.StringFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.*;

/**
 * Created by admin on 1/29/2016.
 */
public class App {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        Connection connection = null;

// Create a FilterChain using FilterChainBuilder
        FilterChainBuilder filterChainBuilder = FilterChainBuilder.stateless();

// Add TransportFilter, which is responsible
// for reading and writing data to the connection
        filterChainBuilder.add(new TransportFilter());

// StringFilter is responsible for Buffer <-> String conversion
        filterChainBuilder.add(new StringFilter(Charset.forName("US-ASCII"), "\n"));


// ClientFilter is responsible for redirecting server responses to the standard output
        filterChainBuilder.add(new ClientFilter());

// Create TCP transport
        final TCPNIOTransport transport =
                TCPNIOTransportBuilder.newInstance().build();
        transport.setProcessor(filterChainBuilder.build());

        transport.start();

        SingleEndpointPool pool = SingleEndpointPool
                .builder(SocketAddress.class)
                .connectorHandler(transport)
                .endpointAddress(new InetSocketAddress("127.0.0.1", 5009))
                .maxPoolSize(10)
                .reconnectDelay(1L, TimeUnit.SECONDS)
                .maxReconnectAttempts(10000)
                .keepAliveCheckInterval(1L, TimeUnit.SECONDS)
                .connectTimeout(3L, TimeUnit.SECONDS)
                .build();


        try {
            for (int i = 0; i < 500; i++) {
                Thread.sleep(1000);
                CompletionHandler<Connection> myCompletionHandler = new CompletionHandler<Connection>() {
                    @Override
                    public void cancelled() {

                    }

                    @Override
                    public void failed(Throwable throwable) {

                    }

                    @Override
                    public void completed(Connection connection) {
                        System.out.println(pool.size());
                        System.out.println("completed");
                        connection.write(String.format("HTX,%s,%s,%s,%s,%s\r\n", "AAPL", "", "", "", ""));
                        connection.addCloseListener(new CloseListener() {
                            @Override
                            public void onClosed(Closeable closeable, ICloseType iCloseType) throws IOException {
                                System.out.println("closed");
                                pool.release(connection);
                            }
                        });
                    }

                    @Override
                    public void updated(Connection connection) {

                    }
                };
                pool.take(myCompletionHandler);
            }


//            Future<Connection> connectionFuture = pool.take();
//            Connection conn = connectionFuture.get(10, TimeUnit.SECONDS);
//            conn.assertOpen();

//            String sCommand = String.format("HTX,%s,%s,%s,%s,%s\r\n", "AAPL", "", "", "", "");
//            conn.write(sCommand);

//            Future<Connection> connectionFuture2 = pool.take();
//            Connection conn2 = connectionFuture.get(10, TimeUnit.SECONDS);
//            pool.release(conn);


            System.out.println("Ready... (\"q\" to exit)");
            final BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
            do {
                final String userInput = inReader.readLine();
                if (userInput == null || "q".equals(userInput)) {
                    break;
                }

//            connection.write(userInput);
            } while (true);
        } finally {
            pool.close();
            transport.shutdownNow();
        }
//
//
//        // perform async. connect to the server
//        Future<Connection> future = transport.connect("127.0.0.1", 9300);
//        // wait for connect operation to complete
//        connection = future.get(10, TimeUnit.SECONDS);
//
//        assert connection != null;
//
//        String sCommand = String.format("HTX,%s,%s,%s,%s,%s\r\n", "AAPL", "", "", "", "");
//
//        connection.write(sCommand);


    }
}
