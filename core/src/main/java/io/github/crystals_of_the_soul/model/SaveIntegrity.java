package io.github.crystals_of_the_soul.model;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class SaveIntegrity {

    private static final String ALGORITHM = "SHA-256";

    /**
     * Genera un hash SHA-256 del contenuto del file di salvataggio.
     * Usato per rilevare manomissioni o corruzione del file.
     */
    public static String generateHash(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hashBytes = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verifica che il contenuto del file corrisponda all'hash salvato.
     * Restituisce false se il file è stato manomesso o è corrotto.
     */
    public static boolean verify(String content, String expectedHash) {
        if (content == null || expectedHash == null) return false;
        String actualHash = generateHash(content);
        return expectedHash.equals(actualHash);
    }
}
