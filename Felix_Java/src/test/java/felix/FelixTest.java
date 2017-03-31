package felix;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.junit.Before;
import org.junit.Test;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;
import org.netbeans.jemmy.operators.JTextPaneOperator;

/**
 * Test de validation d'un scénario où :
 * deux clients Felix se connectent à Camix, 
 * puis changent leurs surnoms,
 * avant que l'un deux quitte le chat,
 * et que l'autre voit l'information dans le chat.
 * @author Emeric G
 *
 */
public class FelixTest {

	// ATTRIBUTES
	private static final String IP = "127.0.0.1";
	private static final String PORT = "12345";
	
	// les vues FenetreConnexion de chacun des deux clients
	private FenetreConnexion fenetreConnexion1, fenetreConnexion2;
	
	// les fenetres des vues FenetreConnexion
	private JFrameOperator fenetreConnexionOperator1, fenetreConnexionOperator2;
	
	// les boutons de connexion
	private JButtonOperator boutonConnexion1, boutonConnexion2;

	// les champ de saisie de l'adresse IP et ceux de saisie du port
	private JTextFieldOperator tfIp1, tfPort1, tfIp2, tfPort2;
	
	// les vues Fenetre des clients une fois connectés
	private Fenetre fenetreChat1, fenetreChat2;
	
	// les fenetres des vues Fenetre
	private JFrameOperator fenetreChatOperator1, fenetreChatOperator2;
	
	// les champs de saisie des messages dans le chat
	private JTextFieldOperator messages1, messages2;
	
	// les panneaux contenant les chat
	private JTextPaneOperator chat1, chat2;
	
	/**
	 * Connecte un premier client au chat
	 * @throws InterruptedException
	 */
	@Before
	public void connectFirstClient() throws InterruptedException {
		
		/*
		 *  Instanciation de la vue FenetreConnexion puis on charge ses éléments dans
		 *  des objets de Jemmy 2
		 */
		fenetreConnexion1 = new FenetreConnexion();
		fenetreConnexionOperator1 = new JFrameOperator(fenetreConnexion1);
		boutonConnexion1 = new JButtonOperator(fenetreConnexion1.getConnectButton());
		tfIp1 = new JTextFieldOperator(fenetreConnexion1.getIpChamp());
		tfPort1 = new JTextFieldOperator(fenetreConnexion1.getPortChamp());
		
		// Vérification de la réussite des accès
		assertNotNull("La vue FenetreConnexion n'est pas accessible.", fenetreConnexionOperator1);
		assertNotNull("Le bouton de connexion n'est pas accessible.", boutonConnexion1);
		assertNotNull("Le champ de saisie de l'IP n'est pas accessible.", tfIp1);
		assertNotNull("Le champ de saisie du port n'est pas accessible.", tfPort1);
		
		fenetreConnexion1.setVisible(true);
		
		// On connecte le client
		tfIp1.typeText(IP);
		tfPort1.typeText(PORT);
		
		Thread.sleep(2000);
		boutonConnexion1.clickMouse();
	}
	
	/**
	 * Connecte un deuxieme client au chat
	 * @throws InterruptedException
	 */
	@Before
	public void connectSecondClient() throws InterruptedException {
		
		// On procède exactement pareil qu'au-dessus mais pour le deuxième client
		fenetreConnexion2 = new FenetreConnexion();
		fenetreConnexionOperator2 = new JFrameOperator(fenetreConnexion2);
		boutonConnexion2 = new JButtonOperator(fenetreConnexion2.getConnectButton());
		tfIp2 = new JTextFieldOperator(fenetreConnexion2.getIpChamp());
		tfPort2 = new JTextFieldOperator(fenetreConnexion2.getPortChamp());
		
		// Vérification de la réussite des accès
		assertNotNull("La vue FenetreConnexion n'est pas accessible.", fenetreConnexionOperator2);
		assertNotNull("Le bouton de connexion n'est pas accessible.", boutonConnexion2);
		assertNotNull("Le champ de saisie de l'IP n'est pas accessible.", tfIp2);
		assertNotNull("Le champ de saisie du port n'est pas accessible.", tfPort2);
		
		fenetreConnexion2.setVisible(true);
		
		tfIp2.typeText(IP);
		tfPort2.typeText(PORT);
		
		Thread.sleep(2000);
		boutonConnexion2.clickMouse();
	}
	
	@Test
	public void testScenario() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException {
		
		// récupération et chargement des vues Fenetre
		this.getBackChat1();
		this.getBackChat2();
		
		// les deux clients changent de nom
		this.messages1.enterText("/n client1");
		this.messages2.enterText("/n client2");
		
		Thread.sleep(4000);
		
		// le premier client quitte le chat
		this.messages1.enterText("/q");
		
		Thread.sleep(10000);
		
		// On vérifie que le message comme quoi il a quitté apparaît bien
		assertTrue(this.chat2.getText().contains("* client1 quitte le chat."));
		
		// le deuxième client quitte à son tour le chat
		this.messages2.enterText("/q");
	}

	/**
	 * On récupère la fenetre de chat du client 1 dans des objets Jemmy2
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void getBackChat1() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		// on récupère le controleur de la vue FenetreConnexion
		FenetreConnexionController controller = this.fenetreConnexion1.getController();
		
		// On récupère la fenetre de chat
		Field fenetre = controller.getClass().getDeclaredField("fenetreChat");
		fenetre.setAccessible(true);
		this.fenetreChat1 = (Fenetre)fenetre.get(controller);
		
		// On la charge dans un objet Jemmy2
		this.fenetreChatOperator1 = new JFrameOperator(this.fenetreChat1);
		
		// On charge le champ de saisie et le panneau du chat dans des objets Jemmy2
		Field messages = fenetreChat1.getClass().getDeclaredField("texteMessage");
		messages.setAccessible(true);
		Field chat = fenetreChat1.getClass().getDeclaredField("texteChatMessages");
		chat.setAccessible(true);
		
		this.messages1 = new JTextFieldOperator((JTextField)messages.get(this.fenetreChat1));
		this.chat1 = new JTextPaneOperator((JTextPane)chat.get(this.fenetreChat1));
		
		// On vérifie que l'accès a fonctionné
		assertNotNull(fenetreChatOperator1);
		assertNotNull(messages1);
		assertNotNull(chat1);
	}

	/**
	 *  On récupère la fenetre de chat du client 2 dans des objets Jemmy2
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void getBackChat2() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		// Même structure que la méthode ci-dessus mais pour le client 2 cette fois
		FenetreConnexionController controller = this.fenetreConnexion2.getController();
		
		Field fenetre = controller.getClass().getDeclaredField("fenetreChat");
		fenetre.setAccessible(true);
		this.fenetreChat2 = (Fenetre)fenetre.get(controller);
		
		this.fenetreChatOperator2 = new JFrameOperator(this.fenetreChat2);
		
		Field messages = fenetreChat2.getClass().getDeclaredField("texteMessage");
		messages.setAccessible(true);
		Field chat = fenetreChat2.getClass().getDeclaredField("texteChatMessages");
		chat.setAccessible(true);
		
		this.messages2 = new JTextFieldOperator((JTextField)messages.get(this.fenetreChat2));
		this.chat2 = new JTextPaneOperator((JTextPane)chat.get(this.fenetreChat2));
		
		assertNotNull(fenetreChatOperator2);
		assertNotNull(messages2);
		assertNotNull(chat2);
	}
}
