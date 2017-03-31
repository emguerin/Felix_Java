package felix;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Classe de la fenêtre de Felix. 
 * 
 * @version 2.0.etu
 * @author Matthias Brun
 * 
 */
public final class Fenetre extends JFrame implements Runnable, ActionListener
{
	/**
	 * UID auto-généré.
	 */
	private static final long serialVersionUID = 5550164472948135142L;

	// kk
	
	/**
	 * Connexion au chat.
	 */
	private transient Connexion connexion;

	/**
	 * Le conteneur de la fenêtre.
	 */
	private Container contenu;

	/**
	 * Le panneau de la fenêtre pour saisir les messages.
	 */
	private JPanel panMessage;

	/**
	 * Le panneau de la fenêtre pour afficher les messages du chat.
	 */
	private JPanel panChatMessages;
	
	/**
	 * Le champs de saisi des messages.
	 */
	private JTextField texteMessage;

	/**
	 * Le panneau texte des messages du chat.
	 */
	private JTextPane texteChatMessages;

	/**
	 * Le panneau de défilement des messages du chat.
	 */
	private JScrollPane defilementChatMessages;

	/**
	 * Le style de document des messages du chat.
	 */
	private transient StyledDocument documentChatMessages;

	/**
	 * Le style des messages du chat.
	 */
	private transient Style documentChatMessagesStyle;
	
	/**
	 * Le nom du style des messages du chat.
	 */
	private static final String STYLENAME = "documentChatMessagesStyle";
	
	/**
	 * Constructeur de la fenêtre.
	 *
	 * @param largeur largeur de la fenêtre. 
	 * @param hauteur hauteur de la fenêtre.
	 * @param titre libellé de la fenêtre affiché dans la barre de titre.
	 * @param connexion la connexion au chat.
	 *
	 */
	public Fenetre(Integer largeur, Integer hauteur, String titre, Connexion connexion)
	{
		// Construction de la fenêtre.
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setSize(largeur, hauteur);

		setTitle(titre);
		setResizable(true);

		// Construction du contenu de la fenêtre.
		this.construirePanneaux();
		this.construireControles(largeur, hauteur);

		// Récupération de la connexion au chat.
		this.connexion = connexion;
	
		// Lancement d'un thread de réception des messages du chat.
		new Thread(this).start();
	}

	/**
	 * Construire les panneaux de la fenêtre.
	 *
	 */
	private void construirePanneaux()
	{
		this.contenu = this.getContentPane();
		this.contenu.setLayout(new FlowLayout());

		this.panMessage = new JPanel();
		this.contenu.add(this.panMessage);

		this.panChatMessages = new JPanel();
		this.contenu.add(this.panChatMessages);
	}

	/**
	 * Construire les widgets de contrôle de la fenêtre.
	 *
	 * @param largeur la largeur de la fenêtre.
	 * @param hauteur la hauteur de la fenêtre.
	 *
	 */
	private void construireControles(Integer largeur, Integer hauteur)
	{
		final Integer mLargeur = Integer.parseInt(Felix.CONFIGURATION.getString("FENETRE_MARGE_LARGEUR"));
		final Integer mHauteur = Integer.parseInt(Felix.CONFIGURATION.getString("FENETRE_MARGE_HAUTEUR"));
		final Integer hMessage = Integer.parseInt(Felix.CONFIGURATION.getString("FENETRE_HAUTEUR_MESSAGE"));

		/* message */
		this.texteMessage = new JTextField();
		this.texteMessage.setPreferredSize(new Dimension(largeur - mLargeur, hMessage));
		this.texteMessage.setHorizontalAlignment(JTextField.LEFT);
		this.texteMessage.setEditable(true);
		this.texteMessage.setFocusable(true);
		this.texteMessage.requestFocus();
		this.texteMessage.addActionListener(this);
		this.panMessage.add(this.texteMessage);
	
		/* chat messages */
		this.texteChatMessages = new JTextPane();
		this.texteChatMessages.setEditable(false);

		this.documentChatMessages = this.texteChatMessages.getStyledDocument();

		this.documentChatMessagesStyle = this.documentChatMessages.addStyle(STYLENAME, null);
		StyleConstants.setFontFamily(this.documentChatMessagesStyle, "Monospaced");

		this.defilementChatMessages = new JScrollPane(this.texteChatMessages);
		this.defilementChatMessages.setPreferredSize(new Dimension(largeur - mLargeur, hauteur - mHauteur));
        	this.defilementChatMessages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		this.panChatMessages.add(this.defilementChatMessages);
	}

	/**
	 * Envoi d'un message au chat.
	 *
	 * @param ev un évènement d'action.
	 *
	 * @see java.awt.event.ActionListener
	 */
	public void actionPerformed(ActionEvent ev)
	{
		final String message = ev.getActionCommand();

		try {
			// Envoi du message au chat.
			this.connexion.ecrire(message);

			// Efface le champs de saisi du message.
			this.texteMessage.setText("");
			
			if (message.startsWith("/q")) {
				FenetreConnexion fenetreCo = new FenetreConnexion();
				fenetreCo.setVisible(true);
				this.dispose();
			}
		} 
		catch (IOException ex) {
			System.err.println("Écriture du message impossible.");
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * Point d'entrée du thread de réception des messages du chat.
	 */
	public void run() 
	{
		try {
			while (true) {
				// Reception d'un message.
				final String message = this.connexion.lire();

				// Ecriture du message dans la zone des messages du chat (à la suite).
				this.documentChatMessages.insertString(
					this.documentChatMessages.getLength(),
					message + System.getProperty("line.separator"), 
					this.documentChatMessages.getStyle(STYLENAME)
				);

				// Force l'affichage du dernier message reçu.
				this.texteChatMessages.setCaretPosition(this.documentChatMessages.getLength());
			}
		}
		catch (BadLocationException ex) {
			System.err.println("Affichage des messages du chat impossible.");
			System.err.println(ex.getMessage());
		}
		catch (EOFException ex) {
			System.err.println("Connexion interrompue avec le serveur du chat.");
		}
		catch (IOException ex) {
			System.err.println("Lecture des messages du chat impossible.");
			System.err.println(ex.getMessage());
		}
		finally	{
			this.connexion.ferme();
		}
	}
}
