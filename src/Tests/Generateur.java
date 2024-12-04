package Tests;

import static org.junit.Assert.assertTrue;
import java.util.Random;
import org.junit.Test;
import Externe.MD5Generator;
import Gen.GenerateurPassphrase;
import Gen.Passphrase;

public class Generateur {
  /**
   * Vérifie que le hash crée par le générateur et la passphrase est la
   * même. Cela assure ainsi que la class passphrase crée notre hash
   * correctement
   */
  @Test
  public void verifierHash() {
    final String sequence = this.genererSequence(100);

    final String hashMD5 = MD5Generator.hashPassword(sequence);
    final String hashPhrase = new Passphrase(sequence).getHash();

    assertTrue(hashMD5.equals(hashPhrase));
  }

  /**
   * Vérifie que la fonction de décryption du génerateur fonctionne
   */
  @Test
  public void verifierDecryption() {
    GenerateurPassphrase generateur = new GenerateurPassphrase();
    final String sequence = "Les distillateurs s'initient à la photographie à Lisbonne";

    Passphrase phrase = new Passphrase(sequence);
    String hashPhrase = phrase.getHash();
    String phraseForceBrute = generateur.forceBrute(hashPhrase);

    System.out.println(phraseForceBrute);
    assertTrue(sequence.equals(phraseForceBrute));
  }

  /**
   * Génère une chaine de caractère incluant des caractères aléatoires
   * 
   * @param longeur la longeur de la chaine de caractère à générer
   */
  public String genererSequence(int longeur) {
    StringBuilder constructeurSequence = new StringBuilder();

    for (int i = 0; i < longeur; i++) {
      int nombreAleatoire = new Random().nextInt(2000) + 65;
      constructeurSequence.append(Character.toChars(nombreAleatoire));
    }
    return constructeurSequence.toString();
  }
}
