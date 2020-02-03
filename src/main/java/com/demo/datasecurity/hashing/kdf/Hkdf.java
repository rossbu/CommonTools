package com.demo.datasecurity.hashing.kdf;

import com.demo.datasecurity.utils.BytesUtils;
import com.demo.datasecurity.utils.EngineFactory;
import com.demo.datasecurity.utils.Random;
import com.demo.datasecurity.utils.SaltUtils;

import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class implements HMAC-based Extract-and-Expand Key Derivation Function (HKDF), as described
 * in <a href="https://tools.ietf.org/html/rfc5869">RFC 5869</a>.
 *
 * @since 1.0.0
 */
public final class Hkdf {
    public static void main(String[] args) throws GeneralSecurityException {
        byte[] ikm = Random.randBytes(20);
        byte[] info = Random.randBytes(20);
        int size = 40;
        byte[] hkdfWithEmptySalt =  Hkdf.computeHkdf("HmacSha256", ikm, new byte[0], info, size);
        System.out.println(BytesUtils.bytesToHex(hkdfWithEmptySalt));
        byte[] hmacSha256s = Hkdf.computeHkdf("HmacSha256", ikm, SaltUtils.getSalt(), info, size);
        System.out.println(BytesUtils.bytesToBase64(hmacSha256s));
    }


    /**
     * Computes an HKDF.
     *
     * @param macAlgorithm the MAC algorithm used for computing the Hkdf. I.e., "HMACSHA1" or
     *     "HMACSHA256".
     * @param ikm the input keying material.
     * @param salt optional salt. A possibly non-secret random value. If no salt is provided (i.e. if
     *     salt has length 0) then an array of 0s of the same size as the hash digest is used as salt.
     * @param info optional context and application specific information.
     * @param size The length of the generated pseudorandom string in bytes. The maximal size is
     *     255.DigestSize, where DigestSize is the size of the underlying HMAC.
     * @return size pseudorandom bytes.
     * @throws GeneralSecurityException if the {@code macAlgorithm} is not supported or if {@code
     *     size} is too large or if {@code salt} is not a valid key for macAlgorithm (which should not
     *     happen since HMAC allows key sizes up to 2^64).
     */
    public static byte[] computeHkdf(
            String macAlgorithm, final byte[] ikm, final byte[] salt, final byte[] info, int size)
            throws GeneralSecurityException {
        Mac mac = EngineFactory.MAC.getInstance(macAlgorithm);
        if (size > 255 * mac.getMacLength()) {
            throw new GeneralSecurityException("size too large");
        }
        if (salt == null || salt.length == 0) {
            // According to RFC 5869, Section 2.2 the salt is optional. If no salt is provided
            // then HKDF uses a salt that is an array of zeros of the same length as the hash digest.
            mac.init(new SecretKeySpec(new byte[mac.getMacLength()], macAlgorithm));
        } else {
            mac.init(new SecretKeySpec(salt, macAlgorithm));
        }
        byte[] prk = mac.doFinal(ikm);
        byte[] result = new byte[size];
        int ctr = 1;
        int pos = 0;
        mac.init(new SecretKeySpec(prk, macAlgorithm));
        byte[] digest = new byte[0];
        while (true) {
            mac.update(digest);
            mac.update(info);
            mac.update((byte) ctr);
            digest = mac.doFinal();
            if (pos + digest.length < size) {
                System.arraycopy(digest, 0, result, pos, digest.length);
                pos += digest.length;
                ctr++;
            } else {
                System.arraycopy(digest, 0, result, pos, size - pos);
                break;
            }
        }
        return result;
    }

    /**
     * Computes symmetric key for ECIES with HKDF from the provided parameters.
     *
     * @param ephemeralPublicKeyBytes the encoded ephemeral public key, i.e. the KEM part of the
     *     hybrid encryption. In some versions of ECIES (e.g. IEEE P1363a) this argument is optional.
     *     Shoup strongly prefers the inclusion of this argument in
     *     http://eprint.iacr.org/2001/112.pdf (see discussion of the value C0 in Section 15.6, and
     *     15.6.1)
     * @param sharedSecret the shared DH secret. This typically is the x-coordinate of the secret
     *     point.
     * @param hmacAlgo the HMAC used (e.g. "HmacSha256")
     * @param hkdfInfo TODO(bleichen): determine what are good values for Info and salt and what are
     *     not good values. The ISO standard proposal http://eprint.iacr.org/2001/112.pdf does not
     *     allow additional values for the key derivation (see Section 15.6.2)
     * @param hkdfSalt
     * @param keySizeInBytes the size of the key material for the DEM key.
     * @throws GeneralSecurityException if hmacAlgo is not supported
     */
    public static byte[] computeEciesHkdfSymmetricKey(
            final byte[] ephemeralPublicKeyBytes,
            final byte[] sharedSecret,
            String hmacAlgo,
            final byte[] hkdfSalt,
            final byte[] hkdfInfo,
            int keySizeInBytes)
            throws GeneralSecurityException {
        byte[] hkdfInput = BytesUtils.concat(ephemeralPublicKeyBytes, sharedSecret);
        return Hkdf.computeHkdf(hmacAlgo, hkdfInput, hkdfSalt, hkdfInfo, keySizeInBytes);
    }
}