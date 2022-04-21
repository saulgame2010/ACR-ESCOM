import javax.swing.*;
import javax.swing.text.html.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GUI extends JFrame implements ActionListener {

	private static JEditorPane areaMsj;
	private JScrollPane scrollMsj;
	private JPanel listaUsuariosP;
	private JTextArea txtArea;
	private JButton enviar, salir, btnMostrarU, btnTheme;
	private static volatile String aux;
	private JLabel onlineLabel;
	private JList<String> listaUsuarios;
	private static DefaultListModel<String> usuarios = new DefaultListModel<>();
	static String username;
	private static ChesterChatos chat;
	static boolean active = true;
	Thread mensajes;
	private String priv = "";
	private HTMLEditorKit kit = new HTMLEditorKit();
	StyleSheet style;
	boolean usu = false;
	Random ran;

	public GUI() {
		// Construimos la interfaz
		areaMsj = new JEditorPane();
		areaMsj.setBounds(15, 15, 750, 600);
		areaMsj.setEditable(false);
		areaMsj.setContentType("text/html");
		areaMsj.setEditorKit(kit);
		areaMsj.setBorder(BorderFactory.createEmptyBorder());

		style = kit.getStyleSheet();
		style.addRule(
				"body{background-color:rgb(33,33,33);color:white;font-family:Helvetica,sans-serif;font-size:10px}");
		style.addRule(".msg{color:rgb(100, 181, 246)}");
		style.addRule(".join{color:rgb(76, 175, 80)}");
		style.addRule(".lft{color:rgb(213, 0, 0)}");
		style.addRule(".priv{color:rgb(124, 77, 255)}");

		scrollMsj = new JScrollPane(areaMsj);
		// 205, 750
		// Original (15, 15, 950, 600)
		scrollMsj.setBounds(15, 15, 600, 300);
		scrollMsj.setBorder(BorderFactory.createEmptyBorder());

		listaUsuariosP = new JPanel();
		// Original (15, 15, 165, 600)
		listaUsuariosP.setBounds(15, 15, 100, 300);
		listaUsuariosP.setBackground(new Color(30, 136, 229));
		listaUsuariosP.setLayout(null);

		onlineLabel = new JLabel("En linea", SwingConstants.CENTER);
		onlineLabel.setBounds(0, 0, 100, 40);
		onlineLabel.setForeground(new Color(255, 255, 255));
		onlineLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 12));

		listaUsuarios = new JList(usuarios);
		listaUsuarios.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				enviarPriv(event);
			}
		});
		// Original (0, 40, 165, 570)
		listaUsuarios.setBounds(0, 40, 100, 270);
		listaUsuarios.setFont(new Font("Consolas", Font.BOLD, 12));
		listaUsuarios.setBackground(new Color(33, 33, 33));
		listaUsuarios.setForeground(new Color(255, 255, 255));

		listaUsuariosP.add(onlineLabel);
		listaUsuariosP.add(listaUsuarios);

		txtArea = new JTextArea();
		// Original(211, 630, 562, 105)
		txtArea.setBounds(135, 330, 362, 70);
		txtArea.setBackground(new Color(66, 66, 66));
		txtArea.setFont(new Font("Arial", Font.BOLD, 12));
		txtArea.setForeground(new Color(255, 255, 255));

		enviar = new JButton("Enviar");
		// Originales (793, 635, 160, 45)
		enviar.setBounds(517, 330, 100, 30);
		enviar.setBackground(new Color(79, 195, 247));
		enviar.setForeground(new Color(0, 0, 0));
		enviar.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		enviar.setBorder(BorderFactory.createEmptyBorder());
		enviar.addActionListener(this);

		btnTheme = new JButton("Cambiar tema");
		// Originales(793, 700, 160, 45)
		btnTheme.setBounds(15, 330, 100, 30);
		btnTheme.setBackground(new Color(77, 208, 225));
		btnTheme.setForeground(new Color(0, 0, 0));
		btnTheme.setBorder(BorderFactory.createEmptyBorder());
		btnTheme.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		btnTheme.addActionListener(this);

		salir = new JButton("Salir");
		// Originales(15, 635, 160, 45)
		salir.setBounds(517, 370, 100, 30);
		salir.setBackground(new Color(77, 208, 225));
		salir.setBorder(BorderFactory.createEmptyBorder());
		salir.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		salir.addActionListener(this);

		btnMostrarU = new JButton("Usuarios");
		// Originales(15, 700, 160, 45)
		btnMostrarU.setBounds(15, 370, 100, 30);
		btnMostrarU.setBackground(new Color(79, 195, 247));
		btnMostrarU.setForeground(new Color(0, 0, 0));
		btnMostrarU.setBorder(BorderFactory.createEmptyBorder());
		btnMostrarU.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		btnMostrarU.addActionListener(this);

		aux = "";

		mensajes = new Thread(new leerMensajes());
		mensajes.start();
		// Propiedades de la ventana
		this.add(scrollMsj);
		// this.add(pnlUsers);
		this.add(txtArea);
		this.add(enviar);
		this.add(salir);
		this.add(btnMostrarU);
		this.add(btnTheme);
		// Originales (1000, 800)
		this.setSize(650, 450);
		this.setTitle("Chat");
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setResizable(false);
		this.getContentPane().setBackground(new Color(33, 33, 33));
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cerrar();
			}
		});
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == salir) {
			cerrar();
		}
		if (e.getSource() == btnMostrarU) {
			if (usu == false) {
				scrollMsj.setBounds(135, 15, 480, 300);
				this.add(listaUsuariosP);
				btnMostrarU.setText("Ocultar usuarios");
				usu = true;
			} else {
				scrollMsj.setBounds(15, 15, 600, 300);
				this.remove(listaUsuariosP);
				btnMostrarU.setText("Mostrar usuarios");
				usu = false;
			}
		}
		if (e.getSource() == btnTheme) {
			ran = new Random();
			salir.setBackground(new Color(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
			btnMostrarU.setBackground(new Color(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
			enviar.setBackground(new Color(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
			btnTheme.setBackground(new Color(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
			listaUsuariosP.setBackground(new Color(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
		}
		if (e.getSource() == enviar) {
			System.out.println(priv);
			if (!priv.equals("")) { // Si hay un usuario destino
				chat.enviar(priv + txtArea.getText());
				priv = "";
				listaUsuarios.clearSelection();
			} else {
				chat.enviar("<msj><" + username + ">" + txtArea.getText());
			}
			txtArea.setText("");
		}
	}

	private void enviarPriv(ListSelectionEvent event) {
		if (listaUsuarios.getSelectedValue() != null) {
			String userDest = (String) listaUsuarios.getSelectedValue();
			priv = "<privado><" + username + "><" + userDest + ">";
		}
	}

	public static String leerMsj() {
		return chat.recibir();
	}

	public static void agregarMsj(String message) {
		aux = aux + message + "<br>";
		areaMsj.setText(aux);
	}

	public static void nombre() {
		chat.enviar("<online><" + username + ">");
	}

	public static void agregarUsuarioEnLinea(String userName) {
		if (!usuarios.contains(userName)) {
			usuarios.addElement(userName);
		}
	}

	public static void quitarUsuarioEnLinea(String userName) {
		usuarios.removeElement(userName);
	}

	private void cerrar() {
		active = false;
		chat.salir(username);
		System.exit(0);
	}

	public static void main(String[] args) {
		username = JOptionPane.showInputDialog("Ingresa tu nombre de usuario");
		if (username.equals("")) {
			JOptionPane.showMessageDialog(null, "Debes ingresar un nombre de usuario", "Introduce tu nombre",
					JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		} else {
			chat = new ChesterChatos(username);
			new GUI();
		}
	}
}

class leerMensajes implements Runnable {

	String msj;

	leerMensajes() {
	}

	@Override
	public void run() {
		while (GUI.active) {
			msj = GUI.leerMsj();
			if (!msj.equalsIgnoreCase(" ")) {
				msj = msj.replace("<", "");
				String[] s = msj.split(">");
				switch (s[0]) {
					case "inicio":
						GUI.agregarMsj("<b class='join'> " + s[1] + "</b> se unio");
						if (!s[1].equals(GUI.username)) {
							GUI.nombre();
							GUI.agregarUsuarioEnLinea(s[1]);
						}
						break;
					case "msj":
						s[2] = s[2].replace(":)", "<img src=file:///C:/Users/sadaga/Pictures/chat/risa.png>");
						s[2] = s[2].replace(":)", "<img src=file:///C:/Users/sadaga/Pictures/chat/risa.png>");
						s[2] = s[2].replace("_amor_", "<img src=file:///C:/Users/sadaga/Pictures/chat/amor.png>");
						s[2] = s[2].replace(":s", "<img src=file:///C:/Users/sadaga/Pictures/chat/enojo.png>");
						s[2] = s[2].replace("_loco_", "<img src=file:///C:/Users/sadaga/Pictures/chat/loco.gif>");
						s[2] = s[2].replace("_homero_",
								"<img src=http://tusimagenesde.com/wp-content/uploads/2015/01/gifs-animados-5.gif");
						s[2] = s[2].replace(":o", "<img src=file:///C:/Users/sadaga/Pictures/chat/sorpresa.png>");
						GUI.agregarMsj("<b class='msg'> " + s[1] + ":</b> " + s[2]);
						break;
					case "privado":
						if (s[2].equals(GUI.username)) {
							s[2] = s[2].replace(":)", "<img src=file:///C:/Users/sadaga/Pictures/chat/risa.png>");
							s[2] = s[2].replace(":)", "<img src=file:///C:/Users/sadaga/Pictures/chat/risa.png>");
							s[2] = s[2].replace("_amor_", "<img src=file:///C:/Users/sadaga/Pictures/chat/amor.png>");
							s[2] = s[2].replace(":s", "<img src=file:///C:/Users/sadaga/Pictures/chat/enojo.png>");
							s[2] = s[2].replace("_loco_", "<img src=file:///C:/Users/sadaga/Pictures/chat/loco.gif>");
							s[2] = s[2].replace("_homero_",
									"<img src=http://tusimagenesde.com/wp-content/uploads/2015/01/gifs-animados-5.gif");
							s[2] = s[2].replace(":o", "<img src=file:///C:/Users/sadaga/Pictures/chat/sorpresa.png>");
							GUI.agregarMsj("<b class='priv'>(Privado)" + s[1] + ":</b> " + s[3]);
						} else if (s[1].equals(GUI.username)) {
							s[2] = s[2].replace(":)", "<img src=file:///C:/Users/sadaga/Pictures/chat/risa.png>");
							s[2] = s[2].replace(":)", "<img src=file:///C:/Users/sadaga/Pictures/chat/risa.png>");
							s[2] = s[2].replace("_amor_", "<img src=file:///C:/Users/sadaga/Pictures/chat/amor.png>");
							s[2] = s[2].replace(":s", "<img src=file:///C:/Users/sadaga/Pictures/chat/enojo.png>");
							s[2] = s[2].replace("_loco_", "<img src=file:///C:/Users/sadaga/Pictures/chat/loco.gif>");
							s[2] = s[2].replace("_homero_",
									"<img src=http://tusimagenesde.com/wp-content/uploads/2015/01/gifs-animados-5.gif");
							s[2] = s[2].replace(":o", "<img src=file:///C:/Users/sadaga/Pictures/chat/sorpresa.png>");
							GUI.agregarMsj("<b class='priv'>(Privado)" + s[2] + ":</b> " + s[3]);
						}
						break;
					case "fin":
						GUI.agregarMsj("<b class='lft'> " + s[1] + "</b> ha salido del grupo");
						GUI.quitarUsuarioEnLinea(s[1]);
						break;
					case "online": // Me estan enviando los usuarios que hay en la sala
						if (!s[1].equals(GUI.username)) { // No puedo mandarme mensajes privados
							GUI.agregarUsuarioEnLinea(s[1]);
						}
						break;
				}
			}
		}
	}
}