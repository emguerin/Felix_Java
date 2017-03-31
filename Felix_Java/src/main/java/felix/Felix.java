package felix;

import java.util.ResourceBundle;

/**
 * Classe principale de Felix. 
 * 
 * @version 2.0.etu
 * @author Matthias Brun
 *
 */
public final class Felix
{
	/**
	 * Fichier de configuration de Felix.
	 */
	public static final ResourceBundle CONFIGURATION = ResourceBundle.getBundle("Configuration");

	/**
	 * Constructeur privé de Felix.
	 * 
	 * Ce constructeur privé assure la non-instanciation de Felix dans un programme.
	 * (Felix est la classe principale du programme Felix)
	 */
	private Felix() 
	{
		// Constructeur privé pour assurer la non-instanciation de Felix.
	}

	/**
	 * Main du programme.
	 *
	 * <p>
	 * Cette fonction main lance le programme Felix qui consiste à :
	 * <ul>
	 * <li> Ouvrir une connexion avec le chat.
	 * <li> Ouvrir une fenêtre de communication via cette connexion.
	 * </ul>
	 * </p>
	 *
	 * @param args aucun argument attendu.
	 */
	public static void main(String[] args)
	{
		System.out.println("Felix v.2.0.etu");

//		try {
//			final Connexion connexion = new Connexion(
//				Felix.CONFIGURATION.getString("ADRESSE_CHAT"),
//				Integer.parseInt(Felix.CONFIGURATION.getString("PORT_CHAT"))
//			);
//
//			final Fenetre fenetre = new Fenetre(
//				Integer.parseInt(Felix.CONFIGURATION.getString("FENETRE_LARGEUR")),
//				Integer.parseInt(Felix.CONFIGURATION.getString("FENETRE_HAUTEUR")),
//				Felix.CONFIGURATION.getString("FENETRE_TITRE"),
//				connexion
//			);
//
//			fenetre.setVisible(true);			
//		} 
//		catch (IOException ex) {
//			System.err.println(ex.getMessage());
//		}
		
		// le controleur du bouton de connexion de la vue FenetreConnexion s'occupera dorénavant
		// de ce qu'il y a de commenté juste au-dessus
		FenetreConnexion fen = new FenetreConnexion();
		fen.setVisible(true);
	}
}

