package felix;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Classe implémentant la vue FenetreConnexion qui permet à un client Felix de se connecter
 * à un serveur Camix
 * @author Emeric G
 *
 */
public class FenetreConnexion extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// FINAL STATIC FIELDS
	public final static String INFO_INIT = "Saisir l'adresse et le port du serveur chat.";
	public final static String INFO_CONNEXION = "Connexion au chat ";
	
	// ATTRIBUTES
	private JPanel infoPanel;
	private JTextField infoChamp;
	private JPanel ipPanel;
	private JTextField ipChamp;
	private JLabel ipLabel;
	
	private JPanel portPanel;
	private JTextField portChamp;
	private JLabel portLabel;
	
	private JPanel connectPanel;
	private JButton connectButton;
	
	private FenetreConnexionController controller;
	
	// CONSTRUCTOR
	public FenetreConnexion() {
		super("Connexion au chat");
		
		this.initializeView();
		this.pack();
		this.setResizable(false);
	}

	// PUBLIC METHODS
	/**
	 * Met à jour le texte d'infos situé en haut de la fenêtre
	 * @param txt ce que l'on veut afficher dans ce texte
	 */
	public void setInfoChamp(String txt) {
		this.infoChamp.setText(txt);
	}
	
	/**
	 * Récupère le Port écrit dans la zone de saisie du port
	 * @return le port saisi, ou 0 si la saisie n'était pas un entier
	 */
	public Integer getTextPortChamp() {
		try {
			return Integer.parseInt(this.portChamp.getText());
		}
		catch (NumberFormatException exc) {
			System.err.println(exc.getMessage());
			return 0;
		}
	}
	
	/**
	 * Récupère l'adresse IP saisie dans la zone de saisie de l'IP
	 * @return l'adresse IP saisie
	 */
	public String getTextIpChamp() {
		return this.ipChamp.getText();
	}
	
	// PRIVATE METHODS
	private void initializeView() {

		// Instanciations
		infoPanel = new JPanel();
		infoChamp = new JTextField();
		ipPanel   = new JPanel();
		ipChamp   = new JTextField();
		ipLabel   = new JLabel("IP");
		portPanel = new JPanel();
		portChamp = new JTextField();
		portLabel = new JLabel("Port");
		connectPanel = new JPanel();
		connectButton = new JButton("Connexion au chat");
		
		// Affectation du contrôleur du bouton connexion
		this.controller = new FenetreConnexionController(this);
		connectButton.addActionListener(this.controller);
		
		// mise en forme du champ d'informations
		infoChamp.setText(FenetreConnexion.INFO_INIT);
		infoChamp.setEditable(false);
		infoChamp.setBorder(null);
		
		// fixe la taille des champs de saisie de l'IP et du Port
		ipChamp.setColumns(20);
		portChamp.setColumns(20);
		
		
		// Agencement de la fenêtre et de ses layout
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		ipPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		portPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		infoPanel.add(infoChamp);
		ipPanel.add(ipLabel);
		ipPanel.add(ipChamp);
		portPanel.add(portLabel);
		portPanel.add(portChamp);
		connectPanel.add(connectButton);

		this.getContentPane().add(infoPanel);
		this.getContentPane().add(ipPanel);
		this.getContentPane().add(portPanel);
		this.getContentPane().add(connectPanel);
	}

	
	// ACCESSORS
	public JTextField getInfoChamp() {
		return infoChamp;
	}

	public JTextField getIpChamp() {
		return ipChamp;
	}

	public JTextField getPortChamp() {
		return portChamp;
	}
	
	public JButton getConnectButton() {
		return this.connectButton;
	}
	
	public FenetreConnexionController getController() {
		return this.controller;
	}
}
