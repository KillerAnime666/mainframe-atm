package bo.edu.ucb.sis213;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VLogin extends JFrame {

	private JPanel contentPane;
	private JTextField tfPin;
	private JTextField tfUsuario;
	JLabel lblIntentos = new JLabel("");
	MValidacion validar = new MValidacion();
	UsuarioActivo datos = UsuarioActivo.getInstance();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VLogin frame = new VLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblATM = new JLabel("ATM V2");
		lblATM.setHorizontalAlignment(SwingConstants.CENTER);
		lblATM.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblATM.setBounds(167, 40, 100, 40);
		contentPane.add(lblATM);
		
		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblUsuario.setBounds(130, 100, 50, 20);
		contentPane.add(lblUsuario);
		
		JLabel lblPin = new JLabel("Pin");
		lblPin.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblPin.setBounds(160, 140, 20, 20);
		contentPane.add(lblPin);
		
		tfPin = new JPasswordField();
		tfPin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		tfPin.setBounds(195, 140, 110, 20);
		contentPane.add(tfPin);
		
		tfUsuario = new JTextField();
		tfUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		tfUsuario.setBounds(195, 100, 110, 20);
		contentPane.add(tfUsuario);
		tfUsuario.setColumns(10);
		
		JButton btnSesion = new JButton("Iniciar Sesion");
		btnSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(validar.validar(tfUsuario.getText(), tfPin.getText())==-1) {
						dispose();
						VMenu menu = new VMenu();
						menu.setVisible(true);
					}
					if(datos.getIntentos()==0) {
						System.exit(0);
					}
					lblIntentos.setText("Le quedan "+datos.getIntentos()+" intentos");
					tfUsuario.setText("");
					tfPin.setText("");
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Error : "+ e1);
				}
			}
		});
		btnSesion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnSesion.setBounds(162, 190, 110, 20);
		contentPane.add(btnSesion);
		
		lblIntentos.setHorizontalAlignment(SwingConstants.CENTER);
		lblIntentos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblIntentos.setBounds(117, 230, 200, 20);
		contentPane.add(lblIntentos);
	}
}
