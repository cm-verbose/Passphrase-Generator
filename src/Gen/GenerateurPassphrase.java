package Gen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class GenerateurPassphrase {
  /* 
   * On suppose que les tableaux ne muent pas : 0 
   */
  private String[] tableauLieux = new String[200];
  private String[] tableauSujets = new String[200];
  private String[] tableauVerbes = new String[200];
  static Passphrase[] tableauPassphrases;

  public GenerateurPassphrase() {
    chargerMots();
  }

  /**
   * Charge les mots dans les fichiers précisés
   */
  private void chargerMots() {
    chargerFichier("dictionnaire_lieux", this.tableauLieux);
    chargerFichier("dictionnaire_sujets", this.tableauSujets);
    chargerFichier("dictionnaire_verbes", this.tableauVerbes);
  }

  /**
   * Charge un fichier en question
   * 
   * @param nomFichier  Le nom du fichier
   * @param dansTableau Le tableau dans lequel les lignes seront inserées
   */
  private static void chargerFichier(final String nomFichier, final String[] dansTableau) {
    File ficherLieux = new File("./src/Docs/" + nomFichier + ".txt");

    try {
      Scanner lecteur = new Scanner(ficherLieux);

      int i = 0;
      while (lecteur.hasNextLine()) {
        String ligne = lecteur.nextLine();
        dansTableau[i] = ligne;
        i += 1;
      }
      
      lecteur.close();
    } catch (FileNotFoundException erreur) {
      System.out.println("Le fichier n'a pas été trouvé !");
    }
  }

  /**
   * Génère des mots de passe
   */
  public void genererPassphrase(final int nombreDePhrases) {
    tableauPassphrases = new Passphrase[nombreDePhrases];

    String[][] tableauxMots = {
        tableauSujets,
        tableauVerbes,
        tableauLieux,
    };

    Random generateurNombreAleatoire = new Random();

    int i = 0;
    while (i != nombreDePhrases) {
      String mot = new String();
      int j = 0;
      for (String[] tableau : tableauxMots) {
        mot += tableau[generateurNombreAleatoire.nextInt(tableau.length - 1)];
        if (j != tableauxMots.length - 1) {
          mot += " ";
        }
        j += 1;
      }

      Passphrase nouvellePhrase = new Passphrase(mot);
      boolean contientPhrase = false;

      if (i != 0) {
        for (Passphrase phrase : tableauPassphrases) {
          if (phrase == null)
            continue;
          if (phrase.getPassPhrase().equals(nouvellePhrase.getPassPhrase())) {
            System.out.println("Passphrase déjà existante");
            contientPhrase = true;
          }
        }
      }

      if (!contientPhrase) {
        tableauPassphrases[i] = nouvellePhrase;
        i += 1;
      }
    }
    System.out.println("Génération de " + nombreDePhrases + " passphrases terminé");
  }

  /**
   * Crée un nouveau fichier
   */
  public void sauverPassphrase() {
    final String chemin = "./src/Docs/sortie.txt";
    File fichierSauvegarde = new File(chemin);
    try {
      fichierSauvegarde.createNewFile();
    } catch (IOException erreur) {
      System.err.println("Le fichier n'a pas pu été créé");
      erreur.printStackTrace();
    }

    try {
      FileWriter redacteur = new FileWriter(chemin);
      if (tableauPassphrases.length == 0) {
        System.out.println("Vous devez générer au moins une phrase");
        redacteur.close();
        return;
      }

      for (Passphrase phrase : tableauPassphrases) {
        redacteur.write(phrase.toString() + "\n");
      }
      redacteur.close();
    } catch (IOException erreur) {
      System.err.println("On a pas pu écrire à ce fichier");
      erreur.printStackTrace();
    }
  }

  /**
   * Lit le fichier de sortie (sortie.txt)
   */
  public void lireFichier() {
    final String chemin = "./src/Docs/sortie.txt";
    System.out.println("Contenu du fichier sortie.txt : ");
    try {
      File fichierSauvegarde = new File(chemin);
      Scanner lecteur = new Scanner(fichierSauvegarde);

      while (lecteur.hasNextLine()) {
        String ligne = lecteur.nextLine();
        System.out.println(ligne);
      }
      lecteur.close();
    } catch (FileNotFoundException erreur) {
      System.out.println("Le fichier n'a pas été trouvé !");
    }
  }

  /**
   * Tente de déchiffrer un mot de passe en utilisant une force brute
   * 
   * @param hash Le paramètre hash à déchiffrer
   * @return La phrase trouvée
   */

  public String forceBrute(String hash) {
    int compteur = 1;
    boolean trouve = false;
    String phraseTrouve = new String();

    hash = hash.trim();

    principale: for (String sujet : tableauSujets) {
      for (String verbe : tableauVerbes) {
        for (String lieux : tableauLieux) {
          String message = sujet + " " + verbe + " " + lieux;
          Passphrase phrase = new Passphrase(message);

          if (hash.equals(phrase.getHash())) {
            phraseTrouve = message;
            trouve = true;
            break principale;
          }
          compteur += 1;
        }
      }
    }

    if (!trouve) {
      System.out.println("La passphrase ne peut pas être trouvée");
    } else {
      String message = String.format("Phrase trouvée après %,3d essais", compteur);
      System.out.println(message);
    }
    return phraseTrouve;
  }
}