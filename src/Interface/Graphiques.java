package Interface;

/**
 * Permet de dessiner des rectangles pour l'interface graphique du
 * terminal
 */
public class Graphiques {
  private static byte largeurTitre = 34;
  private static byte longeurEnTeteMenu = 35;

  /**
   * Construit une chaine de caractères reprétant un rectanglê encadrant le
   * titre de l'application
   * 
   * @param titre Le titre de l'application
   * @return Une chaine de caractère représentant un rectangle das lequel se
   *         trouve le titre spécifié
   */
  public static String construireRectangleTitre(final String titre) {
    // Haut et bas
    String partieHautBas = "";

    for (byte i = 0; i < largeurTitre; ++i) {
      partieHautBas += '#';
    }
    partieHautBas += '\n';

    // Centre
    String decorateurCotes = "";
    for (byte i = 0; i < 4; ++i) {
      decorateurCotes += '#';
    }

    int nombreEspacesCentre = (largeurTitre - (decorateurCotes.length() * 2) - titre.length()) / 2;
    String espacesCentres = "";

    for (byte i = 0; i < nombreEspacesCentre; ++i) {
      espacesCentres += ' ';
    }

    String centre = " ";

    if (largeurTitre - (titre.length() + nombreEspacesCentre * 2 + decorateurCotes.length() * 2) != 0) {
      centre = decorateurCotes + espacesCentres + titre + ' ' + espacesCentres + decorateurCotes + '\n';
    } else {
      centre = decorateurCotes + espacesCentres + titre + espacesCentres + decorateurCotes + '\n';
    }

    final String rectangle = partieHautBas + centre + partieHautBas;
    return rectangle;
  }

  /**
   * 
   * @param optionsMenu
   * @return
   */
  public static String construireRectangleMenu(String[] optionsMenu) {
    final String titreMenu = "Menu";

    String menuPartieGauche = ":-";
    String menuPartieDroite = "-:";

    String centreEnTete = menuPartieGauche + titreMenu + menuPartieDroite;
    int nombreInsertions = longeurEnTeteMenu - centreEnTete.length();

    String rembourage = "";
    for (byte i = 0; i < nombreInsertions / 2; ++i) {
      rembourage += '#';
    }

    String enTeteMenu = "";
    if (longeurEnTeteMenu - (centreEnTete.length() + rembourage.length() * 2) != 0) {
      enTeteMenu = rembourage + '#' + centreEnTete + rembourage;
    } else {
      enTeteMenu = rembourage + centreEnTete + rembourage;
    }

    byte nombreEspaces = 5;
    String espacesOptionsMenu = "";

    for (byte i = 0; i < nombreEspaces; ++i) {
      espacesOptionsMenu += ' ';
    }

    byte compteur = 1;
    String chaineOptionsMenu = "";

    for (String option : optionsMenu) {
      chaineOptionsMenu += compteur + "." + espacesOptionsMenu + option + '\n';
      compteur += 1;
    }

    final String chaineMenu = '\n' + enTeteMenu + '\n' + chaineOptionsMenu + '\n';
    return chaineMenu;
  }
}