package com.demo.redis;
import java.util.List;

//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.Protocol;
//import redis.clients.jedis.util.SafeEncoder;

//import redis.clients.jedis.*;
//import redis.clients.jedis.util.SafeEncoder;

public class RedisQueryExample {
    public static void main(String[] args) {
        // Connect to Redis server
//        Jedis jedis = new Jedis("localhost", 6379);

        // Execute the FT.AGGREGATE command
        String query = "FT.AGGREGATE idx:bikes \"@price:[1000 3000]\" " +
                "GROUPBY 1 @type " +
                "REDUCE COUNT 0 AS count " +
                "SORTBY 2 @count \"DESC\"";

//        Client client = jedis.getClient();
//        client.sendCommand(Protocol.Command.FT_AGGREGATE, SafeEncoder.encode(query));
//        client.setTimeoutInfinite(); // Set timeout to infinite to avoid connection closure during blocking commands
//
//        // Read the response and process it
//        Client.Response response = client.getResponse();
//        List<Object> result = (List<Object>) Protocol.read(response.getOutput());
//        processResult(result);

        // Close the connection
//        jedis.close();
    }

    private static void processResult(List<Object> result) {
        for (Object item : result) {
            if (item instanceof List) {
                List<Object> group = (List<Object>) item;
                String type = new String((byte[]) group.get(0));
                Long count = (Long) group.get(1);
                System.out.println("Type: " + type + ", Count: " + count);
            }
        }
    }
}