package com.demo.http;

/**
 * Created by tbu on 2/6/2018.
 */
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class WorkingKeepAliveUseCase {

    public void requestWithKeepAlive() throws ClientProtocolException,
            IOException, InterruptedException, URISyntaxException {

        CloseableHttpClient httpclient = HttpClients.custom().build();

        HttpEntity entity = null;

        // Put your interface IP in here
        InetAddress ip1 = InetAddress.getByName("172.16.203.113");

        RequestConfig requestConfig = RequestConfig.custom()
                .setLocalAddress(ip1).setCircularRedirectsAllowed(false)
                .build();

        ArrayList<String> fragments = new ArrayList<>();
        fragments
                .add("http://10.104.107.22:1354/tracked/VOD/MegaEngineering/hls_high.m3u8/Manifest");
        fragments
                .add("http://10.104.107.22:1354/tracked/VOD/MegaEngineering/hls_high.m3u8/QualityLevels(67562)/Fragments(audio=0)");
        fragments
                .add("http://10.104.107.22:1354/tracked/VOD/MegaEngineering/hls_high.m3u8/QualityLevels(67562)/Fragments(audio=110000000)");
        fragments
                .add("http://10.104.107.22:1354/tracked/VOD/MegaEngineering/hls_high.m3u8/QualityLevels(67562)/Fragments(audio=220000000)");
        fragments
                .add("http://10.104.107.22:1354/tracked/VOD/MegaEngineering/hls_high.m3u8/QualityLevels(67562)/Fragments(audio=290000000)");

        HttpGet httpget = new HttpGet();
        httpget.setConfig(requestConfig);

        boolean manifestRequest = true;
        String sessionTracker = null;
        CloseableHttpResponse response = null;

        for (String fragment : fragments) {

            if (!manifestRequest) {
                fragment += "?vtxid=" + sessionTracker;
            }
            URL url = new URL(fragment);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());

            // Set the uri to get a new fragment
            httpget.setURI(uri);

            System.out.println("executing request " + httpget.getRequestLine()
                    + " with address " + ip1.toString());

            response = httpclient.execute(httpget);

            try {
                entity = response.getEntity();
                String responseBody = null;
                if (entity != null) {
                    long len = entity.getContentLength();
                    if (len != -1 && len < 2048) {
                        responseBody = EntityUtils.toString(entity);
                    } else {
                        InputStream instream = entity.getContent();
                        try {
                            StringWriter writer = new StringWriter();
                            IOUtils.copy(instream, writer, "UTF-8");
                            responseBody = writer.toString();
                        } finally {
                            instream.close();
                        }
                    }
                }

                if (manifestRequest) {
                    sessionTracker = StringUtils.substringBetween(responseBody,
                            "vtxid=", "\"");
                    manifestRequest = false;
                }

                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    System.out.println(header.toString());
                }

                Thread.sleep(500);

            } finally {
                // Make 100% sure that the content stream is consumed
                EntityUtils.consume(entity);
                response.close();
            }
        }
        httpclient.close();
    }

    public static void main(String[] args) throws ClientProtocolException,
            IOException, InterruptedException, URISyntaxException {

        WorkingKeepAliveUseCase test = new WorkingKeepAliveUseCase();
        test.requestWithKeepAlive();
    }
}
