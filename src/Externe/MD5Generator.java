package Externe;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Cette classe permet de générer un hash MD5 à partir d'un mot de passe.
 */
public class MD5Generator {

  /**
   * Cette méthode permet de générer un hash MD5 à partir d'un mot de passe.
   * 
   * @param password le mot de passe à hasher
   * @return le hash MD5 du mot de passe
   */
  public static String hashPassword(String password) {
    byte[] digest = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(password.getBytes());
      digest = md.digest();
    } catch (NoSuchAlgorithmException e) {
      System.err.println(e.getMessage());
    }
    String hash = tabByteToString(digest);
    return hash;
  }

  /**
   * Cette méthode permet de convertir un tableau de byte en une chaîne de
   * caractères.
   * 
   * @param byteArr tableau de byte
   * @return une chaîne de caractères représentant le tableau de byte
   */
  private static String tabByteToString(byte[] byteArr) {
    StringBuilder sb = new StringBuilder();
    for (byte b : byteArr) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}
