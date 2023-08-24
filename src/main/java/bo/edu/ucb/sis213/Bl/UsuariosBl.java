package bo.edu.ucb.sis213.Bl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bo.edu.ucb.sis213.Dao.MConexion;
import bo.edu.ucb.sis213.Util.UsuarioActivo;

public class UsuariosBl {

    private static int usuarioId;
    private static BigDecimal saldo = BigDecimal.ZERO;

    public static int validar(String usuario, String pin) throws IOException, SQLException {
        UsuarioActivo almacenar = UsuarioActivo.getInstance();

        java.sql.Connection llamar = MConexion.getConnection();

        String consulta = "SELECT * FROM usuarios WHERE usuario = ? AND pin = ?";

        try (PreparedStatement statement = llamar.prepareStatement(consulta)) {
            statement.setString(1, usuario);
            statement.setString(2, pin);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                almacenar.setId(rs.getInt("id"));
                almacenar.setUsuario(rs.getString("usuario"));
                almacenar.setIntentos(3); 
                return -1; 
            }
        }

        almacenar.setIntentos(almacenar.getIntentos() - 1);
        return 0; 
    }

    public static String consultarSaldo() throws SQLException {
        java.sql.Connection llamar = MConexion.getConnection();

        UsuarioActivo id = UsuarioActivo.getInstance();
        usuarioId = id.getId();

        String consulta = "SELECT saldo FROM usuarios WHERE id = ?";
        try {
            PreparedStatement preparedStatement = llamar.prepareStatement(consulta);
            preparedStatement.setInt(1, usuarioId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                saldo = resultSet.getBigDecimal("saldo");
                return saldo.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return saldo.toString();
    }

    public static String realizarDeposito(String deposito) throws SQLException {
        if (deposito.isEmpty()) {
            return "Ingrese un valor mayor a 0";
        }
        try {
            BigDecimal cantidad = new BigDecimal(deposito);

            if (cantidad.compareTo(BigDecimal.ZERO) > 0) {
                java.sql.Connection llamar = MConexion.getConnection();
                UsuarioActivo id = UsuarioActivo.getInstance();
                usuarioId = id.getId();

                String consultaActualizar = "UPDATE usuarios SET saldo = saldo + ? WHERE id = ?";
                PreparedStatement preparedStatement = llamar.prepareStatement(consultaActualizar);
                preparedStatement.setBigDecimal(1, cantidad);
                preparedStatement.setInt(2, usuarioId);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    HistoricoBl.registrarOperacionHistorico("Deposito", cantidad);
                    return "Depósito realizado con éxito. Su saldo es " + consultarSaldo();
                }
            }

            return "Ingrese un valor mayor a 0";
        } catch (NumberFormatException e) {
            return "Por favor ingrese un numero";
        }
    }

    public static String realizarRetiro(String retiro) throws SQLException {
        if (retiro.isEmpty()) {
            return "Ingrese un valor mayor a 0";
        }
        try {
            BigDecimal cantidad = new BigDecimal(retiro);

            if (cantidad.compareTo(new BigDecimal(consultarSaldo())) > 0) {
                return "Saldo insuficiente";
            }
            if (cantidad.compareTo(BigDecimal.ZERO) > 0) {
                java.sql.Connection llamar = MConexion.getConnection();
                UsuarioActivo id = UsuarioActivo.getInstance();
                usuarioId = id.getId();

                String consultaActualizar = "UPDATE usuarios SET saldo = saldo - ? WHERE id = ?";
                PreparedStatement preparedStatement = llamar.prepareStatement(consultaActualizar);
                preparedStatement.setBigDecimal(1, cantidad);
                preparedStatement.setInt(2, usuarioId);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    HistoricoBl.registrarOperacionHistorico("Retiro", cantidad);
                    return "Retiro realizado con éxito. Su saldo es " + consultarSaldo();
                }
            }

            return "Ingrese un valor mayor a 0";
        } catch (NumberFormatException e) {
            return "Por favor ingrese un numero";
        }
    }

    public static String cambiarPIN(String nuevoPin, String pinAnterior) throws SQLException {
        if (nuevoPin.isEmpty() || pinAnterior.isEmpty()) {
            return "No puede dejar los campos vacios";
        }
        try {
            Integer.parseInt(nuevoPin);
            Integer.parseInt(pinAnterior);
        } catch (NumberFormatException e) {
            return "Ingrese solo numeros";
        }

        java.sql.Connection llamar = MConexion.getConnection();
        UsuarioActivo id = UsuarioActivo.getInstance();
        usuarioId = id.getId();

        String consultaPinActual = "SELECT pin FROM usuarios WHERE id = ?";
        try {
            PreparedStatement pinActualStatement = llamar.prepareStatement(consultaPinActual);
            pinActualStatement.setInt(1, usuarioId);
            ResultSet resultSet = pinActualStatement.executeQuery();

            if (resultSet.next()) {
                int pinActual = resultSet.getInt("pin");

                if (pinActual == Integer.parseInt(pinAnterior)) {
                    String updateQuery = "UPDATE usuarios SET pin = ? WHERE id = ?";
                    try {
                        PreparedStatement preparedStatement = llamar.prepareStatement(updateQuery);
                        preparedStatement.setInt(1, Integer.parseInt(nuevoPin));
                        preparedStatement.setInt(2, usuarioId);
                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            return "PIN actualizado con éxito.";
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return "Error al cambiar el PIN.";
                    }
                } else {
                    return "Pin incorrecto";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al consultar el PIN actual.";
        }
        return "Error al cambiar el PIN.";
    }
}