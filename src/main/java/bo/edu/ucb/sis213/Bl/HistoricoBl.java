package bo.edu.ucb.sis213.Bl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

import bo.edu.ucb.sis213.Dao.MConexion;
import bo.edu.ucb.sis213.Util.UsuarioActivo;

public class HistoricoBl {

    private static int usuarioId;

    public static void registrarOperacionHistorico(String tipoOperacion, BigDecimal cantidad) throws SQLException {
        java.sql.Connection llamar = MConexion.getConnection();
        UsuarioActivo id = UsuarioActivo.getInstance();
        usuarioId = id.getId();

        String insertQuery = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = llamar.prepareStatement(insertQuery);
            preparedStatement.setInt(1, usuarioId);
            preparedStatement.setString(2, tipoOperacion);
            preparedStatement.setBigDecimal(3, cantidad);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String consultarHistoricoCliente(int idCliente) throws SQLException {
        java.sql.Connection llamar = MConexion.getConnection();

        String consultaHistorico = "SELECT tipo_operacion, cantidad FROM historico WHERE usuario_id = ?";
        try {
            PreparedStatement preparedStatement = llamar.prepareStatement(consultaHistorico);
            preparedStatement.setInt(1, idCliente);
            ResultSet resultSet = preparedStatement.executeQuery();

            StringBuilder historico = new StringBuilder();
            historico.append("Monto | Tipo Operacion\n");

            while (resultSet.next()) {
                String tipoOperacion = resultSet.getString("tipo_operacion");
                BigDecimal cantidad = resultSet.getBigDecimal("cantidad");

                String signoMonto = tipoOperacion.equalsIgnoreCase("retiro") ? "-" : "+";
                historico.append(String.format("%s%s  %s\n", signoMonto, cantidad.toPlainString(), tipoOperacion));
            }

            return historico.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error al consultar el historial del cliente.";
    }
}