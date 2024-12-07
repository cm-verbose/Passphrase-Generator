import java.util.InputMismatchException;
import java.util.Scanner;

import Gen.GenerateurPassphrase;
import Interface.Graphiques;

public class Generateur {
  private static String NOM_APPLICATION = "Passphrase Gen";
  private static int DA = 2474808;

  public static void main(String[] args) {
    afficherTitre();
    gererInterractionsMenu();
  }

  /**
   * Gère les interractions avec le menu
   */
  private static void gererInterractionsMenu() {
    GenerateurPassphrase generateur = new GenerateurPassphrase();
    int choixUtilisateur = 0;
    Scanner lecteur = new Scanner(System.in);

    do {
      afficherMenu();
      System.out.print("Votre choix: ");

      try {
        choixUtilisateur = lecteur.nextInt();
      } catch (InputMismatchException erreur) {

        // Sauter la ligne invalide
        lecteur.nextLine();
        System.out.println("Saisie invalide : Veuillez saisir un nombre entre 1 et 5");
        continue;
      }

      switch (choixUtilisateur) {
        case 1: {
          System.out.println("Combien de phrases voulez-vous générer ?");
          try {
            int nombreDePhrases = lecteur.nextInt();
            generateur.genererPassphrase(nombreDePhrases);
          } catch (InputMismatchException erreur) {
            lecteur.nextLine();
            System.out.println("Saisie invalide : Veuillez saisir un nombre valide");
            continue;
          }
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

        default: {
          System.out.println("Saisie invalide : Veuillez saisir un nombre entre 1 et 5");
          continue;
        }
      }
    } while (choixUtilisateur != 5);
    lecteur.close();
  }

  /**
   * Affiche le titre de l'application
   */
  private static void afficherTitre() {
    String titre = NOM_APPLICATION + " " + DA;
    String rect = Graphiques.construireRectangleTitre(titre);
    System.out.println(rect);
  }

  /**
   * Affiche le menu de l'application
   */
  private static void afficherMenu() {

    // Le contenu littéral du rectangle
    String[] optionsMenu = {
        "Générer des passphrases aléatoires",
        "Enregistrer les passphrases dans le fichier de sortie",
        "Lire et afficher le fichier de passphrases",
        "Craquer une passphrase par force brute",
        "Quitter",
    };

    String menu = Graphiques.construireRectangleMenu(optionsMenu); 
    System.out.println(menu);
  }
}
