package bo.edu.ucb.sis213.View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

import bo.edu.ucb.sis213.Bl.UsuariosBl;

public class VDeposito extends JFrame {

	private JPanel contentPane;
	private JTextField tfDeposito;
	JLabel lblDMensaje = new JLabel("");

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VDeposito frame = new VDeposito();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VDeposito() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblDeposito = new JLabel("Depósito");
		lblDeposito.setHorizontalAlignment(SwingConstants.CENTER);
		lblDeposito.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblDeposito.setBounds(167, 30, 100, 30);
		contentPane.add(lblDeposito);
		
		JLabel lblDMonto = new JLabel("Ingrese la cantidad a depositar");
		lblDMonto.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblDMonto.setBounds(137, 70, 160, 20);
		contentPane.add(lblDMonto);
		
		tfDeposito = new JTextField();
		tfDeposito.setHorizontalAlignment(SwingConstants.CENTER);
		tfDeposito.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		tfDeposito.setBounds(120, 105, 120, 20);
		contentPane.add(tfDeposito);
		tfDeposito.setColumns(10);

		JButton btnDAceptar = new JButton("Aceptar");
		btnDAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String mensaje="";
				try {
					mensaje = UsuariosBl.realizarDeposito(tfDeposito.getText());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				lblDMensaje.setText(mensaje);
			}
		});
		btnDAceptar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnDAceptar.setBounds(256, 160, 100, 30);
		contentPane.add(btnDAceptar);
		
		JButton btnDBorrar = new JButton("Borrar");
		btnDBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfDeposito.setText("");
			}
		});
		btnDBorrar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		btnDBorrar.setBounds(240, 105, 70, 20);
		contentPane.add(btnDBorrar);
		
		JButton btnDMenu = new JButton("Ir atras");
		btnDMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				VMenu menu = new VMenu();
				menu.setVisible(true);
			}
		});
		btnDMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnDMenu.setBounds(78, 160, 100, 30);
		contentPane.add(btnDMenu);

		lblDMensaje.setHorizontalAlignment(SwingConstants.CENTER);
		lblDMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblDMensaje.setBounds(67, 215, 300, 20);
		contentPane.add(lblDMensaje);
	}
}
