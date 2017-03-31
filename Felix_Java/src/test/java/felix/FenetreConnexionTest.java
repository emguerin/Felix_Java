package felix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.jemmy.JemmyProperties;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JFrameOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;

/**
 * Classe de tests de validation de la vue FenetreConnexion
 * @author Emeric G
 *
 */
public class FenetreConnexionTest {

	// ATTRIBUTES
	public static final String MESSAGE_CONNEXION_ECHOUEE = "Connexion au chat @%s:%s impossible.";
	
	private FenetreConnexion fenetreConnexion;
	
	// la fenetre de la vue
	private JFrameOperator fenetre;
	
	// le bouton de connexion
	private JButtonOperator boutonConnexion;

	// le champ d'informations, celui de saisie de l'adresse IP et celui de saisie du port
	private JTextFieldOperator tfInfos, tfIp, tfPort;
	
	@Before
	public void setUp() {
		
		// sert à fixer le moment où Jemmy 2 arrête de rechercher le widget au lieu de tourner en boucle
		// exemple tiré des tests sur monix
		final Integer timeout = 3000;
		JemmyProperties.setCurrentTimeout("FrameWaiter.WaitFrameTimeout", timeout);
				
		// instanciation de la fenetre qui s'affiche
		fenetreConnexion = new FenetreConnexion();
		fenetreConnexion.setVisible(true);
		
		// récupération de la vue dans les attributs liés à Jemmy2
		
		// récupération de la fenetre
		fenetre = new JFrameOperator(fenetreConnexion);
		boutonConnexion = new JButtonOperator(fenetreConnexion.getConnectButton());
		tfInfos = new JTextFieldOperator(fenetreConnexion.getInfoChamp());
		tfIp = new JTextFieldOperator(fenetreConnexion.getIpChamp());
		tfPort = new JTextFieldOperator(fenetreConnexion.getPortChamp());
		
		// Vérification de la réussite des accès
		assertNotNull("La vue FenetreConnexion n'est pas accessible.", fenetre);
		assertNotNull("Le bouton de connexion n'est pas accessible.", boutonConnexion);
		assertNotNull("Le champ d'informations n'est pas accessible.", tfInfos);
		assertNotNull("Le champ de saisie de l'IP n'est pas accessible.", tfIp);
		assertNotNull("Le champ de saisie du port n'est pas accessible.", tfPort);
	}
	
	/**
	 * Après chaque test on fait une pause de 3 secondes pour voir l'état de la vue
	 * @throws InterruptedException
	 */
	@After
	public void tearDown() throws InterruptedException {
		Thread.sleep(3000);
		
		// on vide les champs de saisie pour le prochain test
		tfIp.clearText();
		tfPort.clearText();
	}
	
	/**
	 * On a testé dans FenetreConnexionControllerTest le comportement du controller au
	 * clic sur le bouton connexion. Ici le but de voir l'affichage du champ d'information
	 * en temps réel.
	 * Cas où l'adresse IP est mauvaise
	 */
	@Test
	public void testConnexion1() {
		System.out.println("TESTCONNEXION - wrong IP");
		
		// écriture dans les champs de saisie comme le ferait un client
		String ip = "100.65.23.14";
		String port = "12345";
		tfIp.typeText(ip);
		tfPort.typeText(port);
		
		// clic sur le bouton de connexion
		boutonConnexion.clickMouse();
		
		// vérification du champ de saisie
		assertEquals(String.format(MESSAGE_CONNEXION_ECHOUEE, ip, port),
					fenetreConnexion.getInfoChamp().getText());
	}
	
	/**
	 * On a testé dans FenetreConnexionControllerTest le comportement du controller au
	 * clic sur le bouton connexion. Ici le but de voir l'affichage du champ d'information
	 * en temps réel.
	 * Cas où le port est mauvais
	 */
	@Test
	public void testConnexion2() {
		System.out.println("TESTCONNEXION - wrong port");
		
		// Test de structure identique à celui au dessus
		String ip = "127.0.0.1";
		String port = "10";
		tfIp.typeText(ip);
		tfPort.typeText(port);
		
		boutonConnexion.clickMouse();
		
		assertEquals(String.format(MESSAGE_CONNEXION_ECHOUEE, ip, port),
					fenetreConnexion.getInfoChamp().getText());
	}
	

}