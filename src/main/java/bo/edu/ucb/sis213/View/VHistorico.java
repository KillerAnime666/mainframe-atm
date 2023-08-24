package bo.edu.ucb.sis213.View;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import bo.edu.ucb.sis213.Bl.UsuariosBl;
import bo.edu.ucb.sis213.Bl.HistoricoBl;
import bo.edu.ucb.sis213.Util.UsuarioActivo;

public class VHistorico extends JFrame {

	private JPanel contentPane;
	UsuarioActivo id = UsuarioActivo.getInstance();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				VHistorico frame = new VHistorico();
				frame.setVisible(true);
			}
		});
	}

	public VHistorico() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblExtracto = new JLabel("Extracto");
		lblExtracto.setHorizontalAlignment(SwingConstants.CENTER);
		lblExtracto.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblExtracto.setBounds(167, 30, 100, 30);
		contentPane.add(lblExtracto);
		
		JLabel lblHMonto = new JLabel("Su saldo es");
		lblHMonto.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHMonto.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblHMonto.setBounds(110, 70, 100, 20);
		contentPane.add(lblHMonto);
		
		JLabel lblSaldo = new JLabel("");
		try {
			lblSaldo.setText(UsuariosBl.consultarSaldo());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		lblSaldo.setHorizontalAlignment(SwingConstants.LEFT);
		lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblSaldo.setBounds(220, 70, 100, 20);
		contentPane.add(lblSaldo);

		JTextArea taHistorico = new JTextArea();
		taHistorico.setEditable(false);
		taHistorico.setBackground(new Color(240, 240, 240));
		try {
			taHistorico.setText(HistoricoBl.consultarHistoricoCliente(id.getId()));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JScrollPane BarraDesplazamiento = new JScrollPane(taHistorico);
		BarraDesplazamiento.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		BarraDesplazamiento.setBounds(17, 100, 400, 110);
		contentPane.add(BarraDesplazamiento);
		
		JButton btnDAceptar = new JButton("Aceptar");
		btnDAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					lblSaldo.setText(UsuariosBl.consultarSaldo());
					taHistorico.setText(HistoricoBl.consultarHistoricoCliente(id.getId()));
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnDAceptar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnDAceptar.setBounds(256, 220, 100, 30);
		contentPane.add(btnDAceptar);
		
		JButton btnDMenu = new JButton("Ir atras");
		btnDMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				VMenu menu = new VMenu();
				menu.setVisible(true);
			}
		});
		btnDMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnDMenu.setBounds(78, 220, 100, 30);
		contentPane.add(btnDMenu);
	}
}
