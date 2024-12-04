import java.util.Scanner;

import Gen.GenerateurPassphrase;
import Interface.Graphiques;

public class Generateur {
  private static String NOM_APPLICATION = "Générateur de mot de passes";
  private static int DA = 2474808;

  public static void main(String[] args) {
    ini();
  }

  /**
   * Est responsable d'initialiser le programme
   */
  private static void ini() {
    afficherTitre();
    Scanner lecteur = new Scanner(System.in);
    GenerateurPassphrase generateur = new GenerateurPassphrase();
    int choixUtilisateur = 0;
    do {
      afficherMenu();
      System.out.print("Votre choix: ");
      choixUtilisateur = lecteur.nextInt();

      switch (choixUtilisateur) {
        case 1: {
          System.out.println("Combien de phrases voulez-vous générer ?");
          int nombreDePhrases = lecteur.nextInt();
          lecteur.nextLine();
          generateur.genererPassphrase(nombreDePhrases);
        }
          break;

        case 2: {
          generateur.sauverPassphrase();
        }
          break;

        case 3: {
          generateur.lireFichier();
        }
          break;

        case 4: {
          System.out.print("Saisir MD5 à cracker : ");
          lecteur.nextLine(); // skip
          String hash = lecteur.nextLine();
          String phrase = generateur.forceBrute(hash);

          if (!phrase.isEmpty()) {
            System.out.println("La phrase est : \n" + phrase);
          }
        }
          break;

        default:
          continue;
      }
    } while (choixUtilisateur != 5);
    lecteur.close();
  }

  /**
   * Affiche le titre de l'application
   */
  private static void afficherTitre() {
    String titre = NOM_APPLICATION + " [" + DA + "]";
    String rect = Graphiques.construireRectangleSimple(titre, 80, true);
    System.out.println(rect);
  }

  /**
   * Affiche le menu de l'application
   */
  private static void afficherMenu() {
    String[] optionsMenu = {
        "",
        "     Menu",
        "",
        "     1.     Générer des passphrases aléatoires",
        "     2.     Enregistrer les passphrases dans le fichier de sortie",
        "     3.     Lire et afficher le fichier de passphrases",
        "     4.     Craquer une passphrase par force brute",
        "     5.     Quitter",
        ""
    };
    String menu = Graphiques.construireRectangleMultiplesLignes(optionsMenu, 80);
    System.out.println(menu + '\n');
  }
}
