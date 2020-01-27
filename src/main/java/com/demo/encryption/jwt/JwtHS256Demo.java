package com.demo.encryption.jwt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static org.apache.commons.codec.binary.Hex.encodeHexString;


/**
 this demo does 2 things :
    1. Generate a JWT
    2. Decode and verify a JWT

 How to use JWT tokens securely
    First, always use HTTPS to make sure JWT tokens transmission over network is safe. By using HTTPS nobody can sniff users' JWT tokens over network.
    Second, make sure JWT tokens are stored securely on users' Android, iOS and browser.
    For Android, store tokens in KeyStore
    For iOS, store tokens in KeyChain
    For browsers, use HttpOnly and Secure cookies. cookie. The HttpOnly flag protects the cookies from being accessed by JavaScript and prevents XSS attack. The Secure flag will only allow cookies to be sent to servers over HTTPS connection.

 exp ( since the EPOCH )
     RFC 7519 states that the exp and iat claim values must be NumericDate values.
     NumericDate is the last definition in Section 2. Terminology, and is defined as the number of seconds (not milliseconds) since Epoch:
     A JSON numeric value representing the number of seconds from 1970-01-01T00:00:00Z UTC until the specified UTC date/time, ignoring leap seconds. This is equivalent to the IEEE Std 1003.1, 2013 Edition [POSIX.1] definition "Seconds Since the Epoch", in which each day is accounted for by exactly 86400 seconds, other than that non-integer values can be represented.
     See RFC 3339 [RFC3339] for details regarding date/times in general and UTC in particular.

 */
public class JwtHS256Demo {

    private static final String SECRET_KEY = "11002w.123";
    private static final String ISSUER = "test.org";
    private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private JSONObject payload = new JSONObject();
    private String signature;
    private String encodedHeader;

    public static void main(String[] args) throws NoSuchAlgorithmException {

        // sender
        long issuedAt = System.currentTimeMillis() / 1000L;
        long expiresAt = issuedAt + 180L;
        JwtHS256Demo outgoingjwt = new JwtHS256Demo("fakesub",new JSONArray(),expiresAt);
        String bearerToken =  outgoingjwt.toString();
        System.out.println("bearerToken: " +bearerToken);

        // recipient like a web site should put it in the cookie or web storage and send it back ( median )
        System.out.println("website receive it and put in cookie from Set-Cookie or BearerToken header and submit back to the sender ( server)");

        // receiver
        JwtHS256Demo incomingToken = new JwtHS256Demo(bearerToken);
        if (incomingToken.isValid()) {
            System.out.println(incomingToken.getSubject());
        } else {
            System.out.println("invalid jwt request");
        }
    }

    /**
     * for sending
     */
    public JwtHS256Demo(String sub, JSONArray aud, long expires) {
        encodedHeader = encode(new JSONObject(JWT_HEADER));
        payload.put("sub", sub);
        payload.put("aud", aud);
        payload.put("exp", expires);
        payload.put("iat", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        payload.put("iss", ISSUER);
        payload.put("jti", UUID.randomUUID().toString()); //how do we use this?
        signature = hmacSha256(encodedHeader + "." + encode(payload), SECRET_KEY);
    }

    /**
     * For verification
     *
     * @param token
     * @throws java.security.NoSuchAlgorithmException
     */
    public JwtHS256Demo(String token) throws NoSuchAlgorithmException {
        encodedHeader = encode(new JSONObject(JWT_HEADER));
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid Token format");
        }
        if (encodedHeader.equals(parts[0])) {
            encodedHeader = parts[0];
        } else {
            throw new NoSuchAlgorithmException("JWT Header is Incorrect: " + parts[0]);
        }

        payload = new JSONObject(decode(parts[1]));
        if (payload.isEmpty()) {
            throw new JSONException("Payload is Empty: ");
        }
        if (!payload.has("exp")) {
            throw new JSONException("Payload doesn't contain expiry " + payload);
        }
        signature = parts[2];

        System.out.println("header: " + encodedHeader);
        System.out.println("payload: " + payload);
        System.out.println("signature: " + signature);
    }

    @Override
    public String toString() {
        return encodedHeader + "." + encode(payload) + "." + signature;
    }

    public boolean isValid() {
        boolean notExpired = payload.getLong("exp") > (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        boolean isSignatureMatched = signature.equals(hmacSha256(encodedHeader + "." + encode(payload), SECRET_KEY));
        System.out.println("is valid ? " + notExpired);
        System.out.println("is isSignatureMatched ? " + isSignatureMatched);
        return notExpired && isSignatureMatched;
    }

    public String getSubject() {
        return payload.getString("sub");
    }

    public List<String> getAudience() {
        JSONArray arr = payload.getJSONArray("aud");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            list.add(arr.getString(i));
        }
        return list;
    }

    private static String encode(JSONObject obj) {
        return encode(obj.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static String encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    /**
     * Sign with HMAC SHA256 (HS256)
     *
     * @param data
     * @return
     * @throws Exception
     */
    private String hmacSha256(String data, String secret) {
        try {

            byte[] hashedSecret = secret.getBytes(StandardCharsets.UTF_8);

            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(hashedSecret, "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return encode(signedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            return null;
        }
    }


    private static String getQueryStringHash(String canonicalUrl)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update("acanonicalurl string, replace me. ".getBytes("UTF-8"));
        byte[] digest = md.digest();
        return encodeHexString(digest);
    }


}