package Gen;

import Externe.MD5Generator;

/**
 * Représente une Passphrase, une phrase utilisée en tant que mot de 
 * passe composée de mots. 
 */
public class Passphrase {
  final String hash;
  final String passPhrase;
  final long dateDeCreation;

  /**
   * 
   * @param passPhrase Une passPhrase suivant le format suivant: <groupe_sujet>
   *                   <groupe_verbe> <groupe_lieu>
   */
  public Passphrase(final String passPhrase) {
    this.passPhrase = passPhrase;
    this.dateDeCreation = System.currentTimeMillis();
    this.hash = MD5Generator.hashPassword(passPhrase);
  }

  /**
   * Accesseur du Hash de la passphrase
   * 
   * @return Le Hash en question
   */
  public String getHash() {
    return hash;
  }

  /**
   * Accesseur de la date de création
   * 
   * @return La date de création
   */
  public long getDateDeCreation() {
    return dateDeCreation;
  }

  /**
   * Accesseur de la passPhrase
   * 
   * @return La passPhrase
   */
  public String getPassPhrase() {
    return passPhrase;
  }

  /**
   * Représente la passphrase sous format : <date> <passphrase> <hash>
   * 
   * @return Une chaîne de caractère formatée sous le format suivant:
   *         <date> <passphrase> <hash>
   */
  @Override
  public String toString() {
    return this.dateDeCreation + " " + this.passPhrase + " " + this.hash;
  }
}
