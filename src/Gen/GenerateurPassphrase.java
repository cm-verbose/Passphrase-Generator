package Gen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

/**
 * Une generateur de passhphrases utilisé afin de gèrer des actions
 * liées aux options de menu.
 */
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

  /*
   * La situation assume que nous avons 200 lignes dans nos tableaux.
   */
  public static int LONGEUR_TABLEAU = 200;

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
   * Lit le contenu d'une liste de mots situé à .src/Docs/
   * 
   * @param nomFichier Le nom du fichier de liste de mots à lire
   * @return Un tableau de taile 200 (la situation assume que nous avons 200
   *         lignes) contenant les expressions de la liste spécifiée par le nom de
   *         fichier
   */
  private static String[] lireListe(final String nomFichier) {
    final String chemin = "./src/Docs/" + nomFichier + ".txt";
    String[] listeTableau;

    try {
      FileInputStream fichierListeDeMots = new FileInputStream(chemin);
      Scanner lecteur = new Scanner(fichierListeDeMots);

      /*
       * La situation assume qu'il a 200 lignes dans nos fichiers. Utiliser le type
       * short consomme ainsi le moins de mémoire tout en ayant assez d'espace pour
       * stocker 200 lignes (nous avons moins de 32 767 lignes)
       */
      short compteur = 0;
      listeTableau = new String[LONGEUR_TABLEAU];

      while (lecteur.hasNextLine()) {
        listeTableau[compteur] = lecteur.nextLine();
        ++compteur;
      }
      lecteur.close();
      return listeTableau;

      /*
       * Les erreurs FileNotFoundException et SecurityException sont les erreurs
       * présentes dans le constructeur de la classe FileInputStream sous sa forme de
       * surchage avec un paramètre de type File, qui est utilisé dans ce cas. Pour la
       * syntaxe par rapport au catch à multiples exceptions, cette fonctionalité est
       * disponible depuis Java SE7.
       * Voir :
       * https://docs.oracle.com/javase/7/docs/technotes/guides/language/catch-multiple.html
       */
    } catch (FileNotFoundException | SecurityException erreur) {
      System.out.println(String.format("Fichier au chemin \"%s\" indisponible ou non-existant", chemin));
      String[] vide = {};
      return vide;
    }
  }

  /**
   * Génère des mots de passe
   * 
   * @param nombreDePhrases Le nombre de phrases à générer
   */
  public void genererPassphrase(final int nombreDePhrases) {
    /*
     * On ne devrait pas générer un nombre négatif ou nul de phrases
     */
    if (nombreDePhrases <= 0) {
      System.out.println("Saisie invalide : Veuillez saisir un nombre supérieur à 0");
      return;
    }

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
        /*
         * Les listes ont 200 éléments et la situation assume qu'ils sont immuables.
         * Ainsi on peut générer un nombre entre 0 et 199 (ou 200 -1) afin d'avoir
         * un index aléatoire pour la liste
         */
        int nombreAleatoire = generateurNombreAleatoire.nextInt(LONGEUR_TABLEAU - 1);
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

      /**
       * Clause de garde (ou garde): (on n'incrémente pas i) jusqu'à temps que l'on
       * génère une phrase qui n'existe pas déjà
       */
      if (contientPhrase)
        continue;
      tableauPassphrases[i] = nouvellePhrase;
      ++i;
    }
    System.out.println("Génération de " + nombreDePhrases + " passphrases terminée");
  }

  /**
   * Crée un nouveau fichier de sauvegarde à ./src/Docs/sortie.txt
   * et enregistre les passphares générées.
   */
  public void sauverPassphrase() {
    final String chemin = "./src/Docs/sortie.txt";
    if (tableauPassphrases == null || tableauPassphrases.length == 0) {
      System.out
          .println("Il faut que vous génériez au moins une passphrase avant de pouvoir enregistrer vos passphrases");
      return;
    }

    try {
      FileOutputStream fichier = new FileOutputStream(chemin);
      PrintStream redacteur = new PrintStream(fichier);

      for (Passphrase phrase : tableauPassphrases) {
        redacteur.println(phrase.toString());
      }
      redacteur.close();
      System.out.println("Enregistrement des passphrases dans le fichier sortie.txt");
    } catch (IOException erreur) {
      System.out.println("Nous ne pouvons pas créer le fichier au chemin " + chemin);
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
    } catch (FileNotFoundException | SecurityException erreur) {
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
    String hashSansEspaces = hash.trim();
    /*
     * La longeur du hash M5D générée par la class MD5Generator est toujours de 32
     * caractères. Ainsi toute chaine qui a plus ou moins de caractères est invalide
     * automatiquement, puisque le générateur ne peut jamais générer un hash qui n'a
     * pas une longeur de 32 caractères.
     */
    if (hashSansEspaces.length() != 32) {
      System.out.println(
          "Saisie invalide : Le hash MD5 à cracker n'est pas d'une longueur de 32 caractères");
      return "";
    }

    int compteur = 1;
    boolean trouve = false;

    String phraseTrouve = new String();
    hash = hashSansEspaces;

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

            /**
             * Ici, on utilise break pour éviter de continuer d'itérer. Bien qu'on pourrait
             * aussi utiliser une boule do-while ou while avec avec la comparaison du
             * booléen nommé trouvé, le code suivant est plus simple à lire.
             */
            break principale;
          }
          ++compteur;
        }
      }
    }

    if (trouve) {
      /**
       * Puisqu'une grande majorité des phrases à trouver par force brute prennent
       * plus de 1 000 voir 1,000 000 essais, la plupart du temps, le formattage sert
       * à lire le nombre d'essais plus facilement.
       */
      String message = String.format("Phrase trouvée après %,3d essais", compteur);
      System.out.println(message);
    } else {
      System.out.println("La passphrase ne peut être trouvée");
    }
    return phraseTrouve;
  }
}