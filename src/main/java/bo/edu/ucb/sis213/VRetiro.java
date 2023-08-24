package bo.edu.ucb.sis213;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class VRetiro extends JFrame {

	private JPanel contentPane;
	private JTextField tfRetiro;
	JLabel lblRMensaje = new JLabel("");

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VRetiro frame = new VRetiro();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VRetiro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblRetito = new JLabel("Retiro");
		lblRetito.setHorizontalAlignment(SwingConstants.CENTER);
		lblRetito.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblRetito.setBounds(167, 30, 100, 30);
		contentPane.add(lblRetito);
		
		JLabel lblRMonto = new JLabel("Ingrese la cantidad a depositar");
		lblRMonto.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblRMonto.setBounds(137, 70, 160, 20);
		contentPane.add(lblRMonto);
		
		tfRetiro = new JTextField();
		tfRetiro.setHorizontalAlignment(SwingConstants.CENTER);
		tfRetiro.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		tfRetiro.setColumns(10);
		tfRetiro.setBounds(120, 105, 120, 20);
		contentPane.add(tfRetiro);
		
		JButton btnRBorrar = new JButton("Borrar");
		btnRBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfRetiro.setText("");
			}
		});
		btnRBorrar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnRBorrar.setBounds(240, 105, 70, 20);
		contentPane.add(btnRBorrar);
		
		JButton btnRMenu = new JButton("Ir atras");
		btnRMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				VMenu menu = new VMenu();
				menu.setVisible(true);
			}
		});
		btnRMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnRMenu.setBounds(78, 160, 100, 30);
		contentPane.add(btnRMenu);
		
		JButton btnRAceptar = new JButton("Aceptar");
		btnRAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String mensaje="";
				try {
					mensaje = COperacionesATM.realizarRetiro(tfRetiro.getText());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				lblRMensaje.setText(mensaje);
			}
		});
		btnRAceptar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnRAceptar.setBounds(256, 160, 100, 30);
		contentPane.add(btnRAceptar);
		
		lblRMensaje.setHorizontalAlignment(SwingConstants.CENTER);
		lblRMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblRMensaje.setBounds(67, 215, 300, 20);
		contentPane.add(lblRMensaje);
	}

}
