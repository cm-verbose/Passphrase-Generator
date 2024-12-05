package Interface;

/**
 * Permet de dessiner des rectangles pour l'interface graphique du
 * terminal
 */
public class Graphiques {
  /*
   * Symboles spéciaux
   */
  private static char COIN_SUPERIEUR_GAUCHE = '\u250c';
  private static char COIN_SUPERIEUR_DROIT = '\u2510';
  private static char COIN_INFERIEUR_GAUCHE = '\u2514';
  private static char COIN_INFERIEUR_DROIT = '\u2518';
  private static char LIGNE_HORIZONTALE = '\u2500';
  private static char LIGNE_VERTICALE = '\u2502';

  /**
   * Construit un rectangle simple (comportant une seule ligne)
   * 
   * @param texte     Le texte a inclure dans un rectangle
   * @param largeur   La largeur du rectangle
   * @param estCentre Si le texte doit être centré
   * @return Le texte encadré dans un rectangle
   */
  public static String construireRectangleSimple(final String texte, final int largeur, final boolean estCentre) {
    final StringBuilder rectangle = new StringBuilder();
    final StringBuilder constructeurLigneHorizontale = new StringBuilder();

    for (int i = 0; i < largeur - 2; i++) {
      constructeurLigneHorizontale.append(LIGNE_HORIZONTALE);
    }
    final String ligneHorizontale = constructeurLigneHorizontale.toString();

    // Haut
    rectangle.append(COIN_SUPERIEUR_GAUCHE + ligneHorizontale + COIN_SUPERIEUR_DROIT + '\n');

    // Centre
    if (estCentre) {
      final int nombreInsertions = ((largeur - texte.length()) - 2) / 2;
      StringBuilder constructeurEspaces = new StringBuilder();

      for (int i = 0; i < nombreInsertions; i++) {
        constructeurEspaces.append(' ');
      }
      String espaces = constructeurEspaces.toString();

      // Nombre d'espaces insuffisants dans certains cas
      if (nombreInsertions % 2 == 0) {
        rectangle.append(LIGNE_VERTICALE + espaces + texte + espaces + ' ' + LIGNE_VERTICALE + '\n');
      } else {
        rectangle.append(LIGNE_VERTICALE + espaces + texte + espaces + LIGNE_VERTICALE + '\n');
      }
    } else {
      String commencementCentre = (LIGNE_VERTICALE + " " + texte);
      final int nombreInsertions = largeur - commencementCentre.length() - 1;
      for (int i = 0; i < nombreInsertions; i++) {
        commencementCentre += " ";
      }
      rectangle.append(commencementCentre + LIGNE_VERTICALE + '\n');
    }
    // Bas
    rectangle.append(COIN_INFERIEUR_GAUCHE + ligneHorizontale + COIN_INFERIEUR_DROIT);
    return rectangle.toString();
  }

  /**
   * Contruit un rectangle avec plusieurs lignes
   * 
   * @param lignes  Les lignes du rectangle
   * @param largeur La largeur du rectangle
   * @return Un rectangle à plusieurs lignes enclosant du texte
   */
  public static String construireRectangleMultiplesLignes(final String[] lignes, final int largeur) {
    final StringBuilder rectangle = new StringBuilder();
    final StringBuilder constructeurLigneHorizontale = new StringBuilder();

    for (int i = 0; i < largeur - 2; i++) {
      constructeurLigneHorizontale.append(LIGNE_HORIZONTALE);
    }
    final String ligneHorizontale = constructeurLigneHorizontale.toString();

    // Haut
    rectangle.append(COIN_SUPERIEUR_GAUCHE + ligneHorizontale + COIN_SUPERIEUR_DROIT + '\n');

    // Centre
    for (String ligne : lignes) {
      String commencementCentre = (LIGNE_VERTICALE + " " + ligne);
      final int nombreInsertions = largeur - commencementCentre.length() - 1;
      for (int i = 0; i < nombreInsertions; i++) {
        commencementCentre += " ";
      }
      rectangle.append(commencementCentre + LIGNE_VERTICALE + '\n');
    }

    // Bas
    rectangle.append(COIN_INFERIEUR_GAUCHE + ligneHorizontale + COIN_INFERIEUR_DROIT);
    return rectangle.toString();
  }
}