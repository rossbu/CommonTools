package com.demo.http;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class HttpClientFactory {


    public static void main (String... arg) throws IOException {

        HttpClientFactory httpClientFactory = new HttpClientFactory();
        HttpClient httpClient = httpClientFactory.getCloseableHttpClient();
        HttpPost httpPost = new HttpPost("http://localhost:9999/postme");
        httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
        httpPost.setHeader("Accept","application/json");
        StringEntity stringEntity = new StringEntity("{\"key\":\"abcdbb\"}");
        ResponseHandler<String> responseHandler = httpClientFactory.getResponseHandler();
        String responseBody = httpClient.execute(httpPost, responseHandler);

    }


    public ResponseHandler<String> getResponseHandler(){
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };
        return responseHandler;
    }



    public CloseableHttpClient getCloseableHttpClient() {
        int timeout = 20;

        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000)
                .build();
        SocketConfig socketConfig =
                SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(true).build();

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }

                }
        };

        SSLContext sslContext = null;// TLS
        try {
            sslContext = SSLContext.getInstance("SSL");
//        sslContext.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SSLContext.setDefault(sslContext);
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(300);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(200);
        poolingHttpClientConnectionManager.setValidateAfterInactivity(50000);  // -1 means no validation

        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        httpClientBuilder.addInterceptorFirst(new HttpRequestHeaderInterceptor());// interceptor is et here

        CloseableHttpClient httpClient = httpClientBuilder.
                setConnectionManager(poolingHttpClientConnectionManager).
                setDefaultRequestConfig(config).
                setDefaultSocketConfig(socketConfig).
                setSSLSocketFactory(sslConnectionSocketFactory).
                build();

        return httpClient;
    }


    public CloseableHttpClient getCloseableHttpClient2() throws KeyManagementException, NoSuchAlgorithmException {

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }

                }
        };
        final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), trustAllCerts, new SecureRandom());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1.2" }, null,SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
        HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
        HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(1, false);

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .setConnectionRequestTimeout(30000)
                .setCookieSpec(CookieSpecs.STANDARD).build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(defaultRequestConfig)
                .setRetryHandler(retryHandler)
                .evictExpiredConnections()
                .build();
        return httpClient;
    }


    static class HttpRequestHeaderInterceptor implements HttpRequestInterceptor {
        @Override
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            if (request.containsHeader(HTTP.CONTENT_LEN)) {
                request.removeHeaders(HTTP.CONTENT_LEN);
            }

            if (request.containsHeader(HTTP.TRANSFER_ENCODING)) {
                request.removeHeaders(HTTP.TRANSFER_ENCODING);
            }

            if (request.containsHeader("Accept")) {
                request.removeHeaders("Accept");
                request.addHeader(HttpHeaders.ACCEPT, "application/xml, text/xml;q=.9, application/json;q=.8, application/*+xml;q=.7, application/*+json;q=.6");
            }

            if (request.containsHeader(HTTP.CONTENT_TYPE)) {
                request.removeHeaders(HTTP.CONTENT_TYPE);
                request.addHeader(HttpHeaders.CONTENT_TYPE, "text/xml;charset=UTF-8"); // for soap
//            request.addHeader("Content-type", "application/xml;charset=UTF-8");
            }
        }
    }
}
