package felix;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Classe qui teste les différents comportements de la méthode ActionPerformed selon
 * ce qui est écrit dans les champs de saisie de la vue fenetre de connexion
 * @author Emeric G
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class FenetreConnexionControllerTest {
	
	// ATTRIBUTES
	private static final String MESSAGE_CONNEXION_ECHOUEE = "Connexion au chat @%s:%s impossible.";
	private static final String MESSAGE_CONNEXION_EN_COURS = "Connexion au chat @%s:%s.";
	private static final String BAD_IP = "19.124.65.9999";
	private static final Integer BAD_PORT = 10;
	private static final String GOOD_IP = "127.0.0.1";
	private static final Integer GOOD_PORT = 12345;

	/*
	 * le mock de la fenetre de la connexion sur laquelle le controller va effectuer
	 * ses actions
	 */
	@Mock
	private FenetreConnexion fenetreMock;
	
	private FenetreConnexionController controller;
	
	/**
	 * On vérifie que le mock est bien instancié
	 */
	@Test
	public void testPreconditions() 
	{
		assertNotNull(this.fenetreMock);
	}
	
	/**
	 * On réinstancie le controller à chaque début de test
	 */
	@Before
	public void setUp() {
		controller = new FenetreConnexionController(fenetreMock);
	}
	
	/**
	 * Test du cas où un port valide et une ip invalide sont saisis
	 */
	@Test
	public void testActionPerformed1() {
		
		// On indique au mock de retourner telle IP et tel port aux appels des getter des saisies
		Mockito.when(fenetreMock.getTextIpChamp()).thenReturn(BAD_IP);
		Mockito.when(fenetreMock.getTextPortChamp()).thenReturn(GOOD_PORT);
		
		/*
		 *  En cas de mauvaise connexion, le champ d'informations dit d'abord que la connexion
		 *  est en cours puis qu'elle a échoué
		 */
		Mockito.doNothing().when(fenetreMock).
			setInfoChamp(String.format(MESSAGE_CONNEXION_EN_COURS, BAD_IP, GOOD_PORT));
		Mockito.doNothing().when(fenetreMock).
			setInfoChamp(String.format(MESSAGE_CONNEXION_ECHOUEE, BAD_IP, GOOD_PORT));
		
		// appel de la méthode à tester
		controller.actionPerformed(null);
		
		// On vérifie que chaque changement du champ d'informations ne se fait qu'une seule fois
		Mockito.verify(fenetreMock, Mockito.times(1)).
			setInfoChamp(String.format(MESSAGE_CONNEXION_EN_COURS, BAD_IP, GOOD_PORT));
		Mockito.verify(fenetreMock, Mockito.times(1)).
			setInfoChamp(String.format(MESSAGE_CONNEXION_ECHOUEE, BAD_IP, GOOD_PORT));
	}
	
	/**
	 * Test du cas où un port invalide et une ip valide sont saisis
	 */
	@Test
	public void testActionPerformed2() {
		
		// Meme structure que le test précédent
		Mockito.when(fenetreMock.getTextIpChamp()).thenReturn(GOOD_IP);
		Mockito.when(fenetreMock.getTextPortChamp()).thenReturn(BAD_PORT);
		
		Mockito.doNothing().when(fenetreMock).
			setInfoChamp(String.format(MESSAGE_CONNEXION_EN_COURS, GOOD_IP, BAD_PORT));
		Mockito.doNothing().when(fenetreMock).
			setInfoChamp(String.format(MESSAGE_CONNEXION_ECHOUEE, GOOD_IP, BAD_PORT));
		
		controller.actionPerformed(null);
		
		Mockito.verify(fenetreMock, Mockito.times(1)).
			setInfoChamp(String.format(MESSAGE_CONNEXION_EN_COURS, GOOD_IP, BAD_PORT));
		Mockito.verify(fenetreMock, Mockito.times(1)).
			setInfoChamp(String.format(MESSAGE_CONNEXION_ECHOUEE, GOOD_IP, BAD_PORT));
	}
	
	/**
	 * Test du cas où un port valide et une ip valide sont saisis
	 */
	@Test
	public void testActionPerformed3() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Mockito.when(fenetreMock.getTextIpChamp()).thenReturn(GOOD_IP);
		Mockito.when(fenetreMock.getTextPortChamp()).thenReturn(GOOD_PORT);
		
		// Ici, seul le texte de connexion en cours doit s'afficher dans le champ d'informations
		Mockito.doNothing().when(fenetreMock).
			setInfoChamp(String.format(MESSAGE_CONNEXION_EN_COURS, GOOD_IP, GOOD_PORT));
		
		controller.actionPerformed(null);
		
		// On vérifie que le texte n'est affiché qu'une seule fois
		Mockito.verify(fenetreMock, Mockito.times(1)).
			setInfoChamp(String.format(MESSAGE_CONNEXION_EN_COURS, GOOD_IP, GOOD_PORT));
		
		// Fermeture de la connexion pour que le test se termine
		Field connexion = FenetreConnexionController.class.getDeclaredField("connexion");
		connexion.setAccessible(true);
		((Connexion)connexion.get(controller)).ferme();
	}
}
