package com.demo.multithreading.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioHttpClient {

    private static final int POOL_SIZE = 5;
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8000;
    private static final String REQUEST = "GET /test HTTP/1.1\r\nHost: localhost\r\n\r\n";

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

        for (int i = 1; i <= POOL_SIZE; i++) {
            final int id = i;
            executor.submit(() -> {
                try {
                    sendRequest("Req" + id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
    }

    private static void sendRequest(String threadName) throws IOException {
        System.out.println(threadName + " connecting to server");

        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
        socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT), null, new CompletionHandler<Void, Void>() {
            @Override
            public void completed(Void result, Void attachment) {
                System.out.println(threadName + " connected to server");
                ByteBuffer buffer = ByteBuffer.wrap(REQUEST.getBytes());
                socketChannel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer buffer) {
                        if (buffer.hasRemaining()) {
                            socketChannel.write(buffer, buffer, this);
                        } else {
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                            socketChannel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                                @Override
                                public void completed(Integer result, ByteBuffer buffer) {
                                    buffer.flip();
                                    byte[] data = new byte[buffer.limit()];
                                    buffer.get(data);
                                    System.out.println(threadName + " received response: " + new String(data));
                                    try {
                                        socketChannel.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer buffer) {
                                    System.err.println(threadName + " failed to read response: " + exc.getMessage());
                                    try {
                                        socketChannel.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer buffer) {
                        System.err.println(threadName + " failed to send request: " + exc.getMessage());
                        try {
                            socketChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.err.println(threadName + " failed to connect to server: " + exc.getMessage());
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Add a timeout for the connection attempt
        try {
            Thread.sleep(10000); // Wait for 10 seconds to ensure the server is ready
            if (!socketChannel.isOpen()) {
                System.err.println(threadName + " connection attempt timed out");
                socketChannel.close();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println(threadName + " failed to close socket after timeout: " + e.getMessage());
        }
    }
}