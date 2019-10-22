package com.demo.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.*;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;

import javax.net.ssl.SSLContext;

/**
 * An example of HttpClient can be customized to authenticate preemptively using BASIC scheme.
 * <b/>
 * Generally, preemptive authentication can be considered less
 * secure than a response to an authentication challenge and therefore discouraged.
 * First, we need to create the HttpContext – pre-populating it with an authentication cache with the right type of authentication scheme pre-selected.
 * This will mean that the negotiation from the previous example is no longer necessary – Basic Authentication is already chosen:
    <pre>
     The entire Client-Server communication is now clear:

     the Client sends the HTTP Request with no credentials
     the Server sends back a challenge
     the Client negotiates and identifies the right authentication scheme
     the Client sends a second Request, this time with credentials
    </pre>

    https://hc.apache.org/httpcomponents-client-4.5.x/examples.html
 * <b/>
 */
public class ClientPreemptiveBasicAuthenticationDemo {
    static UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials("username", "password");

    public static void main(String[] args) throws Exception {

        ClientPreemptiveBasicAuthenticationDemo clientPreemptiveBasicAuthenticationDemo = new ClientPreemptiveBasicAuthenticationDemo();
        var http_host = "http://www.google.com/api/test";
        HttpHost targetHost = new HttpHost(http_host, 443, "https");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        HttpPost httpPost = new HttpPost("http://www.google.com/api/test");
        credsProvider.setCredentials(AuthScope.ANY, usernamePasswordCredentials);
        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());
        final HttpClientContext httpClientContext = HttpClientContext.create();
        httpClientContext.setCredentialsProvider(credsProvider);
        httpClientContext.setAuthCache(authCache);
        CloseableHttpResponse response = clientPreemptiveBasicAuthenticationDemo.getClient().execute(httpPost, httpClientContext);
        System.out.println(response.getStatusLine().getStatusCode());
    }

    private CloseableHttpClient client;

    public CloseableHttpClient getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    try {
                        client = createInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return client;
    }

    protected CloseableHttpClient createInstance() throws Exception {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true).build();

        // Socket setting by registerring http/https
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, (var1, var2) -> true);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory).build();

        // Connection pooling
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connManager.setMaxTotal(100); // total connection
        connManager.setDefaultMaxPerRoute(20); // Increase default max connection per route to 20

        // Connection Strategy
        ConnectionKeepAliveStrategy keepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(org.apache.http.HttpResponse response, HttpContext context) {
                long keepAlive = super.getKeepAliveDuration(response, context);
                if (keepAlive == -1) {
                    // Keep connections alive 5 seconds if a keep-alive value
                    // has not be explicitly set by the server
                    keepAlive = 5000;
                }
                return keepAlive;
            }
        };

        // socket setting
        org.apache.http.config.SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(5000).build();

        // connection config
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setBufferSize(256 * 1024)
                .setCharset(StandardCharsets.UTF_8)
                .build();


        // request config
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(90 * 1000)
                .setConnectionRequestTimeout(90 * 1000)
                .setSocketTimeout(90 * 1000)
                .setAuthenticationEnabled(true)
                .build();

        // set header ACCEPT Accept: application/xml, text/xml, application/xml, application/json, application/*+xml, text/xml, application/*+xml, application/*+json
        List<Header> headers = new ArrayList<>();
        Header header1 = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        Header header2 = new BasicHeader(HttpHeaders.ACCEPT, "application/json");
        headers.add(header1);
        headers.add(header2);

        // set credentials
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);

        credentialsProvider.setCredentials(authScope, usernamePasswordCredentials);
        CloseableHttpClient httpClient = HttpClients.custom()
                .addInterceptorFirst(new PreemptiveAuthInterceptor())
                .setSSLContext(sslContext)
                .setDefaultRequestConfig(requestConfig)
                .setDefaultHeaders(headers)
                .setConnectionManager(connManager)
                .setDefaultConnectionConfig(connectionConfig)
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
        return httpClient;
    }

    static class PreemptiveAuthInterceptor implements HttpRequestInterceptor {

        @Override
        public void process(final HttpRequest request, final HttpContext context) throws HttpException {
            AuthState authState = (AuthState) context.getAttribute(HttpClientContext.TARGET_AUTH_STATE);
            // If no auth scheme avaialble yet, try to initialize it preemptively
            if (authState.getAuthScheme() == null) {
                AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
                CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(HttpClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
                if (authScheme != null) {
                    Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()));
                    if (creds == null) {
                        throw new HttpException("No credentials for preemptive authentication");
                    }
                    authState.setAuthScheme(authScheme);
                    authState.setCredentials(creds);
                }
            }
        }
    }
}