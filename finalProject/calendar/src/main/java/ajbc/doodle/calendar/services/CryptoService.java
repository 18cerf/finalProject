package ajbc.doodle.calendar.services;

import ajbc.doodle.calendar.Application;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;


@Component
public class CryptoService {

    private final SecureRandom SECURE_RANDOM = new SecureRandom();

    private KeyPairGenerator keyPairGenerator;

    private KeyFactory keyFactory;

    public CryptoService() {
        try {
            this.keyPairGenerator = KeyPairGenerator.getInstance("EC");
            this.keyPairGenerator.initialize(new ECGenParameterSpec("secp256r1"));

            this.keyFactory = KeyFactory.getInstance("EC");
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            Application.logger.error("init crypto", e);
        }
    }

    public KeyPairGenerator getKeyPairGenerator() {
        return this.keyPairGenerator;
    }

    public PublicKey convertX509ToECPublicKey(byte[] encodedPublicKey)
            throws InvalidKeySpecException {
        X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(encodedPublicKey);
        return this.keyFactory.generatePublic(pubX509);
    }

    public PrivateKey convertPKCS8ToECPrivateKey(byte[] encodedPrivateKey)
            throws InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8spec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        return this.keyFactory.generatePrivate(pkcs8spec);
    }

    private static byte[] P256_HEAD = Base64.getDecoder()
            .decode("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgA");


    public ECPublicKey fromUncompressedECPublicKey(String encodedPublicKey)
            throws InvalidKeySpecException {

        byte[] w = Base64.getUrlDecoder().decode(encodedPublicKey);
        byte[] encodedKey = new byte[P256_HEAD.length + w.length];
        System.arraycopy(P256_HEAD, 0, encodedKey, 0, P256_HEAD.length);
        System.arraycopy(w, 0, encodedKey, P256_HEAD.length, w.length);

        X509EncodedKeySpec ecpks = new X509EncodedKeySpec(encodedKey);
        return (ECPublicKey) this.keyFactory.generatePublic(ecpks);
    }

    public static byte[] toUncompressedECPublicKey(ECPublicKey publicKey) {
        byte[] result = new byte[65];
        byte[] encoded = publicKey.getEncoded();
        System.arraycopy(encoded, P256_HEAD.length, result, 0,
                encoded.length - P256_HEAD.length);
        return result;
    }

    byte[] concat(byte[]... arrays) {

        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }

        byte[] result = new byte[totalLength];

        int currentIndex = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, currentIndex, array.length);
            currentIndex += array.length;
        }

        return result;
    }


    public byte[] encrypt(String plainTextString, String uaPublicKeyString,
                          String authSecret, int paddingSize)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {

        KeyPair asKeyPair = this.keyPairGenerator.genKeyPair();
        ECPublicKey asPublicKey = (ECPublicKey) asKeyPair.getPublic();
        byte[] uncompressedASPublicKey = toUncompressedECPublicKey(asPublicKey);

        ECPublicKey uaPublicKey = fromUncompressedECPublicKey(uaPublicKeyString);

        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");
        keyAgreement.init(asKeyPair.getPrivate());
        keyAgreement.doPhase(uaPublicKey, true);

        byte[] ecdhSecret = keyAgreement.generateSecret();

        byte[] salt = new byte[16];
        this.SECURE_RANDOM.nextBytes(salt);
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        hmacSHA256
                .init(new SecretKeySpec(Base64.getUrlDecoder().decode(authSecret), "HmacSHA256"));
        byte[] prkKey = hmacSHA256.doFinal(ecdhSecret);
        byte[] keyInfo = concat("WebPush: info\0".getBytes(StandardCharsets.UTF_8),
                toUncompressedECPublicKey(uaPublicKey), uncompressedASPublicKey);
        hmacSHA256.init(new SecretKeySpec(prkKey, "HmacSHA256"));
        hmacSHA256.update(keyInfo);
        hmacSHA256.update((byte) 1);
        byte[] ikm = hmacSHA256.doFinal();
        hmacSHA256.init(new SecretKeySpec(salt, "HmacSHA256"));
        byte[] prk = hmacSHA256.doFinal(ikm);

        byte[] cekInfo = "Content-Encoding: aes128gcm\0".getBytes(StandardCharsets.UTF_8);

        hmacSHA256.init(new SecretKeySpec(prk, "HmacSHA256"));
        hmacSHA256.update(cekInfo);
        hmacSHA256.update((byte) 1);
        byte[] cek = hmacSHA256.doFinal();
        cek = Arrays.copyOfRange(cek, 0, 16);


        byte[] nonceInfo = "Content-Encoding: nonce\0".getBytes(StandardCharsets.UTF_8);
        hmacSHA256.init(new SecretKeySpec(prk, "HmacSHA256"));
        hmacSHA256.update(nonceInfo);
        hmacSHA256.update((byte) 1);
        byte[] nonce = hmacSHA256.doFinal();
        nonce = Arrays.copyOfRange(nonce, 0, 12);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(cek, "AES"),
                new GCMParameterSpec(128, nonce));

        List<byte[]> inputs = new ArrayList<>();
        byte[] plainTextBytes = plainTextString.getBytes(StandardCharsets.UTF_8);
        inputs.add(plainTextBytes);
        inputs.add(new byte[]{2});

        int padSize = Math.max(0, paddingSize - plainTextBytes.length);
        if (padSize > 0) {
            inputs.add(new byte[padSize]);
        }

        byte[] encrypted = cipher.doFinal(concat(inputs.toArray(new byte[0][])));

        ByteBuffer encryptedArrayLength = ByteBuffer.allocate(4);
        encryptedArrayLength.putInt(encrypted.length);

        byte[] header = concat(salt, encryptedArrayLength.array(),
                new byte[]{(byte) uncompressedASPublicKey.length}, uncompressedASPublicKey);

        return concat(header, encrypted);
    }

}