package felix;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Classe implémentant le contrôleur destiné à être affecté au bouton de connexion 
 * de la vue FenetreConnexion
 * @author Emeric G
 *
 */
public class FenetreConnexionController implements ActionListener {

	// ATTRIBUTES
	private FenetreConnexion fenetre;
	private Connexion connexion;
	private Fenetre fenetreChat;
	
	// CONSTRUCTOR
	public FenetreConnexionController(FenetreConnexion fen) {
		fenetre = fen;
	}
	
	// PUBLIC METHODS
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// On met à jour le champ d'information pour indiquer qu'une tentative de connexion est effectuée
		fenetre.setInfoChamp("Connexion au chat @" + fenetre.getTextIpChamp() + ":" + fenetre.getTextPortChamp() + ".");
		
		try {
			
			// On essaie de se connecter avec l'adresse IP et le Port saisis
			connexion = new Connexion(
				fenetre.getTextIpChamp(),
				fenetre.getTextPortChamp()
			);

			// On instancie une fenêtre de chat si la connexion est réussie
			fenetreChat = new Fenetre(
				Integer.parseInt(Felix.CONFIGURATION.getString("FENETRE_LARGEUR")),
				Integer.parseInt(Felix.CONFIGURATION.getString("FENETRE_HAUTEUR")),
				Felix.CONFIGURATION.getString("FENETRE_TITRE"),
				connexion
			);

			// on rend visible cette dernière et on supprime la fenêtre de connexion
			fenetreChat.setVisible(true);	
			fenetre.dispose();
		} 
		catch (IOException ex) {
			System.err.println(ex.getMessage());
			
			// En cas d'échec de connexion, on l'indique dans le champ d'informations
			fenetre.setInfoChamp("Connexion au chat @" + fenetre.getTextIpChamp() + ":" + fenetre.getTextPortChamp() + " impossible.");
		}
	}

}
