package com.demo.datasecurity.utils;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;

/**
 * A factory that returns JCE engines, using pre-specified j.security.Providers.
 *
 * <p>This class contains a lot of static factories and static functions returning factories: these
 * allow customization and hide the typing complexity in this class. To use this class, import it,
 * and replace your <code>Cipher.getInstance(...)</code> with <code>
 * EngineFactory.CIPHER.getInstance(...)</code>.
 *
 * @since 1.0.0
 */
public final class EngineFactory<T_WRAPPER extends EngineWrapper<T_ENGINE>, T_ENGINE> {
    private static final Logger logger = Logger.getLogger(EngineFactory.class.getName());
    private static final List<Provider> defaultPolicy;
    private static final boolean DEFAULT_LET_FALLBACK = true;

    // Warning: keep this above the initialization of static providers below. or you'll get null
    // pointer errors (due to this policy not being initialized).
    static {
            defaultPolicy = new ArrayList<Provider>();
    }

    public static final EngineFactory<EngineWrapper.TCipher, Cipher> CIPHER =
            new EngineFactory<>(new EngineWrapper.TCipher());

    public static final EngineFactory<EngineWrapper.TCipher, Cipher> getCustomCipherProvider(
            boolean letFallbackToDefault, String... providerNames) {
        return new EngineFactory<EngineWrapper.TCipher, Cipher>(
                new EngineWrapper.TCipher(), toProviderList(providerNames), letFallbackToDefault);
    }

    public static final EngineFactory<EngineWrapper.TMac, Mac> MAC =
            new EngineFactory<>(new EngineWrapper.TMac());

    public static final EngineFactory<EngineWrapper.TMac, Mac> getCustomMacProvider(
            boolean letFallbackToDefault, String... providerNames) {
        return new EngineFactory<EngineWrapper.TMac, Mac>(
                new EngineWrapper.TMac(), toProviderList(providerNames), letFallbackToDefault);
    }

    public static final EngineFactory<EngineWrapper.TSignature, Signature> SIGNATURE =
            new EngineFactory<>(new EngineWrapper.TSignature());

    public static final EngineFactory<EngineWrapper.TSignature, Signature> getCustomSignatureProvider(
            boolean letFallbackToDefault, String... providerNames) {
        return new EngineFactory<EngineWrapper.TSignature, Signature>(
                new EngineWrapper.TSignature(), toProviderList(providerNames), letFallbackToDefault);
    }

    public static final EngineFactory<EngineWrapper.TMessageDigest, MessageDigest> MESSAGE_DIGEST =
            new EngineFactory<>(new EngineWrapper.TMessageDigest());

    public static final EngineFactory<EngineWrapper.TMessageDigest, MessageDigest>
    getCustomMessageDigestProvider(boolean letFallbackToDefault, String... providerNames) {
        return new EngineFactory<EngineWrapper.TMessageDigest, MessageDigest>(
                new EngineWrapper.TMessageDigest(), toProviderList(providerNames), letFallbackToDefault);
    }

    public static final EngineFactory<EngineWrapper.TKeyAgreement, KeyAgreement> KEY_AGREEMENT =
            new EngineFactory<>(new EngineWrapper.TKeyAgreement());

    public static final EngineFactory<EngineWrapper.TKeyAgreement, KeyAgreement>
    getCustomKeyAgreementProvider(boolean letFallbackToDefault, String... providerNames) {
        return new EngineFactory<EngineWrapper.TKeyAgreement, KeyAgreement>(
                new EngineWrapper.TKeyAgreement(), toProviderList(providerNames), letFallbackToDefault);
    }

    public static final EngineFactory<EngineWrapper.TKeyPairGenerator, KeyPairGenerator>
            KEY_PAIR_GENERATOR = new EngineFactory<>(new EngineWrapper.TKeyPairGenerator());

    public static final EngineFactory<EngineWrapper.TKeyPairGenerator, KeyPairGenerator>
    getCustomKeyPairGeneratorProvider(boolean letFallbackToDefault, String... providerNames) {
        return new EngineFactory<EngineWrapper.TKeyPairGenerator, KeyPairGenerator>(
                new EngineWrapper.TKeyPairGenerator(), toProviderList(providerNames), letFallbackToDefault);
    }

    public static final EngineFactory<EngineWrapper.TKeyFactory, KeyFactory> KEY_FACTORY =
            new EngineFactory<>(new EngineWrapper.TKeyFactory());

    public static final EngineFactory<EngineWrapper.TKeyFactory, KeyFactory>
    getCustomKeyFactoryProvider(boolean letFallbackToDefault, String... providerNames) {
        return new EngineFactory<EngineWrapper.TKeyFactory, KeyFactory>(
                new EngineWrapper.TKeyFactory(), toProviderList(providerNames), letFallbackToDefault);
    }

    /** Helper function to get a list of Providers from names. */
    public static List<Provider> toProviderList(String... providerNames) {
        List<Provider> providers = new ArrayList<Provider>();
        for (String s : providerNames) {
            Provider p = Security.getProvider(s);
            if (p != null) {
                providers.add(p);
            } else {
                logger.info(String.format("Provider %s not available", s));
            }
        }
        return providers;
    }

    public EngineFactory(T_WRAPPER instanceBuilder) {
        this.instanceBuilder = instanceBuilder;
        this.policy = defaultPolicy;
        this.letFallback = DEFAULT_LET_FALLBACK;
    }

    public EngineFactory(T_WRAPPER instanceBuilder, List<Provider> policy) {
        this.instanceBuilder = instanceBuilder;
        this.policy = policy;
        this.letFallback = DEFAULT_LET_FALLBACK;
    }

    public EngineFactory(T_WRAPPER instanceBuilder, List<Provider> policy, boolean letFallback) {
        this.instanceBuilder = instanceBuilder;
        this.policy = policy;
        this.letFallback = letFallback;
    }

    public T_ENGINE getInstance(String algorithm) throws GeneralSecurityException {
        Exception cause = null;
        for (Provider provider : this.policy) {
            try {
                return this.instanceBuilder.getInstance(algorithm, provider);
            } catch (Exception e) {
                if (cause == null) {
                    cause = e;
                }
            }
        }
        if (letFallback) {
            return this.instanceBuilder.getInstance(algorithm, null);
        }
        throw new GeneralSecurityException("No good Provider found.", cause);
    }

    private T_WRAPPER instanceBuilder;
    private List<Provider> policy;
    private boolean letFallback;
}