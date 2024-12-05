package Gen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class GenerateurPassphrase {
  /*
   * 1 tableau = (200 * taille de null (valeur par défault d'un non-initialisé
   * String)) + (taille d'en-tête) + (longeur tableau) + (rembourage)
   * 
   * (64 bits)
   * => 200 * 8 bytes + 8 bytes + 4 bytes + 8 bytes
   * => 1620 bytes
   * 
   * (32 bits)
   * => 200 * 4 bytes + 8 bytes + 4 bytes + 8 bytes
   * => 820 bytes
   * 
   * Il y a peut-être allocation de mémoire non nécessaire à l'instantiation
   * de la classe, puisque plusieurs méthodes peuvent échouer (dans la lecture).
   * Ainsi on fait l'allocation de manière dynamique, plus tard dans le code. De
   * plus, initialiser ces valeurs à l'instantiation de la classe nécessite une
   * allocation au heap, puisque les valeurs qu'on a sont des classes dérivés la
   * classe Object et sont stockées dans le heap. Conséquemment déclarer ces
   * valeurs nécessite à du temps pour l'allocateur qui pourrait être usé si
   * on instantiait ces valeurs à :
   * 
   * new String[200]
   */
  private String[] tableauLieux;
  private String[] tableauSujets;
  private String[] tableauVerbes;

  static Passphrase[] tableauPassphrases;

  public GenerateurPassphrase() {
    chargerMots();
  }

  /**
   * Charge les mots dans les fichiers précisés
   */
  private void chargerMots() {
    tableauLieux = lireListe("dictionnaire_lieux");
    tableauSujets = lireListe("dictionnaire_sujets");
    tableauVerbes = lireListe("dictionnaire_verbes");
  }

  /**
   * Lit le contenu d'une liste de mots situé à .src/Docs/...
   * 
   * @param nomFichier Le nom du fichier
   */
  private static String[] lireListe(final String nomFichier) {
    final String chemin = "./src/Docs/" + nomFichier + ".txt";
    String[] listeTableau;

    try {
      FileInputStream fichierListeDeMots = new FileInputStream(chemin);
      Scanner lecteur = new Scanner(fichierListeDeMots);

      /*
       * La situation assume que dans chaque fichier, nous avons moins que 32 767
       * lignes pour chaque fichier (nous avons 200 lignes)
       */
      short compteur = 0;
      listeTableau = new String[200];

      while (lecteur.hasNextLine()) {
        listeTableau[compteur] = lecteur.nextLine();
        ++compteur;
      }
      lecteur.close();
      return listeTableau;

    } catch (FileNotFoundException erreur) {
      System.out.println(String.format("Fichier au chemin \"%s\" indisponible", chemin));
      String[] vide = {};
      return vide;
    }
  }

  /**
   * Génère des mots de passe
   */
  public void genererPassphrase(final int nombreDePhrases) {
    tableauPassphrases = new Passphrase[nombreDePhrases];
    Random generateurNombreAleatoire = new Random();

    int i = 0;
    while (i != nombreDePhrases) {
      String mot = new String();

      /*
       * Ici, on a seulement 3 listes, donc on peut qualifier j en tant
       * qu'un byte puisque -128 < 3 < 128.
       * 
       * Je n'ai pas fait l'utilisation de tableau multi-dimensionel en
       * raison que cela occuperait plus de mémoire dans le programe, et
       * pourrait ralentir l'éxecution du code par allocation au heap.
       * En utilisant le code suivant par exemple :
       * 
       * String[][] tableauxListes = {
       * tableauSujets,
       * tableauVerbes,
       * tableauLieux,
       * }
       * 
       * on aurait un tableau de référence à des tableaux de châines de
       * caractères. Ainsi, la mémoire occupée par ce tableau est :
       * 
       * 3 * <taille référence>
       * 
       * (64 bits)
       * => 3 * 8 bytes
       * => 24 bytes
       * 
       * (32 bits)
       * => 3 * 4 bytes
       * => 12 bytes
       */
      for (byte j = 0; j < 3; ++j) {
        // Les listes ont 200 éléments et sont immuables
        int nombreAleatoire = generateurNombreAleatoire.nextInt(199);
        switch (j) {
          case 0: {
            mot += tableauSujets[nombreAleatoire] + " ";
          }
            break;

          case 1: {
            mot += tableauVerbes[nombreAleatoire] + " ";
          }
            break;

          case 2: {
            mot += tableauLieux[nombreAleatoire];
          }
            break;

          /*
           * la valeur de j qui nous importe ici sont les valeurs de 0 à 2
           * les autres valeurs, des intervalles [-128; -1] et [3-128] ne
           * nous intéressent pas.
           */
          default:
            break;
        }
      }

      Passphrase nouvellePhrase = new Passphrase(mot);
      boolean contientPhrase = false;

      /* Vérifier que la passphrase n'existe pas */
      if (i != 0) {
        for (Passphrase phrase : tableauPassphrases) {
          /**
           * Puisque notre tableau est initialisé à une longeur
           * spécifique, certaines valeurs sont null. Ainsi, lorsque
           * nous atteignons null, nous pouvons quitter la boucle
           * d'avance, puisque le reste des valeurs sont non-initialisées
           */
          if (phrase == null)
            break;

          if (phrase.getPassPhrase().equals(nouvellePhrase.getPassPhrase())) {
            System.out.println("Passphrase déjà existante");
            contientPhrase = true;
          }
        }
      }

      /**
       * Clause de garde: (on n'incrémente pas i) jusqu'à temps que l'on génère
       * une phrase qui n'existe pas déjà
       */
      if (!contientPhrase)
        continue;
      tableauPassphrases[i] = nouvellePhrase;
      ++i;
    }
    System.out.println("Génération de " + nombreDePhrases + " passphrases terminé");
  }

  /**
   * Crée un nouveau fichier de sauvegarde à ./src/Docs/sortie.txt
   * et enregistre les passphares générées. 
   */
  public void sauverPassphrase() {
    final String chemin = "./src/Docs/sortie.txt";
    File fichierSauvegarde = new File(chemin);

    try {
      fichierSauvegarde.createNewFile();
    } catch (IOException erreur) {
      System.err.println("Le fichier n'a pas pu été créé");
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
    }
  }

  /**
   * Lit le fichier de sortie (sortie.txt)
   */
  public void lireFichier() {
    final String chemin = "./src/Docs/sortie.txt";
    System.out.println("Contenu du fichier sortie.txt : ");

    try {
      FileInputStream fichierSauvegarde = new FileInputStream(chemin);
      Scanner lecteur = new Scanner(fichierSauvegarde);

      while (lecteur.hasNextLine()) {
        String ligne = lecteur.nextLine();
        System.out.println(ligne);
      }
      lecteur.close();
    } catch (FileNotFoundException erreur) {
      System.out.println(String.format("Fichier au chemin \"%s\" indisponible", chemin));
    }
  }

  /**
   * Tente de déchiffrer un mot de passe en utilisant une force brute
   * 
   * @param hash Le paramètre hash à déchiffrer
   * @return La phrase trouvée, (une chaîne de caractères vide si elle n'est pas
   *         trouvée)
   */
  public String forceBrute(String hash) {
    int compteur = 1;
    boolean trouve = false;

    String phraseTrouve = new String();
    hash = hash.trim();

    /*
     * On utilise une étiquette nommée principale pour identifier la boucle
     * principale mais aussi pour quitter d'avance si l'on retrouve la phrase
     * en question.
     */
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
      /**
       * Puisqu'une grande majorité des phrases à trouver par force brute prennent
       * plus de 1 000 voir 1,000 000 essais, la plupart du temps, le formattage sert
       * à lire le nombre d'essais plus facilement.
       */
      String message = String.format("Phrase trouvée après %,3d essais", compteur);
      System.out.println(message);
    }
    return phraseTrouve;
  }
}
