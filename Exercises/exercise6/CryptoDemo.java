// 文件路径：Exercises/exercise6/CryptoDemo.java
package Exercises.exercise6;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Base64;

public class CryptoDemo {
    public static void main(String[] args) throws Exception {
        System.out.println("=== AES-256 (Symmetric) Encryption ===");
        SecretKey aesKey = generateAESKey();
        byte[] iv = generateIV();

        String originalMessage = "Hello Bob, this is Alice.";
        byte[] encrypted = encryptAES(originalMessage, aesKey, iv);
        String decrypted = decryptAES(encrypted, aesKey, iv);

        System.out.println("Original: " + originalMessage);
        System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));
        System.out.println("Decrypted: " + decrypted);

        System.out.println("\n=== RSA-2048 (Asymmetric) Encryption ===");
        KeyPair aliceKeyPair = generateRSAKeyPair();
        KeyPair bobKeyPair = generateRSAKeyPair();

        String messageForBob = "Hi Bob, encrypted with your public key.";
        byte[] encryptedRSA = encryptRSA(messageForBob, bobKeyPair.getPublic());
        String decryptedRSA = decryptRSA(encryptedRSA, bobKeyPair.getPrivate());

        System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encryptedRSA));
        System.out.println("Decrypted: " + decryptedRSA);

        System.out.println("\n=== RSA Signature Verification ===");
        String signedMessage = "This is a signed message from Alice.";
        byte[] signature = signMessage(signedMessage, aliceKeyPair.getPrivate());

        boolean isVerified = verifySignature(signedMessage, signature, aliceKeyPair.getPublic());
        System.out.println("Signature: " + Base64.getEncoder().encodeToString(signature));
        System.out.println("Verified: " + isVerified);
    }

    // === AES ===
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256);
        return generator.generateKey();
    }

    public static byte[] generateIV() {
        byte[] iv = new byte[12]; // GCM 推荐 12 字节 IV
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public static byte[] encryptAES(String message, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        return cipher.doFinal(message.getBytes());
    }

    public static String decryptAES(byte[] encrypted, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted);
    }

    // === RSA ===
    public static KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    public static byte[] encryptRSA(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes());
    }

    public static String decryptRSA(byte[] encrypted, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(encrypted));
    }

    // === Digital Signature ===
    public static byte[] signMessage(String message, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        return signature.sign();
    }

    public static boolean verifySignature(String message, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(message.getBytes());
        return signature.verify(signatureBytes);
    }
}
