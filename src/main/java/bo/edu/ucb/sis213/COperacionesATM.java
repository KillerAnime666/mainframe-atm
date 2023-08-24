
package bo.edu.ucb.sis213;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class COperacionesATM {

    private static int usuarioId;
    private static double saldo;

    private static int revision;

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
                double saldo = resultSet.getDouble("saldo");
                return String.valueOf(saldo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(saldo);
    }

    public static String realizarDeposito(String deposito) throws SQLException {
        if (deposito.isEmpty()) {
            return "Ingrese un valor mayor a 0";
        }
        try {
            double cantidad = Double.parseDouble(deposito);

            if (cantidad > 0) {
                java.sql.Connection llamar = MConexion.getConnection();
                UsuarioActivo id = UsuarioActivo.getInstance();
                usuarioId = id.getId();

                String consultaActualizar = "UPDATE usuarios SET saldo = saldo + ? WHERE id = ?";
                PreparedStatement preparedStatement = llamar.prepareStatement(consultaActualizar);
                preparedStatement.setDouble(1, cantidad);
                preparedStatement.setInt(2, usuarioId);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    registrarOperacionHistorico("Deposito", cantidad);
                    return "Deposito realizado con exito. Su saldo es " + consultarSaldo();
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
            double cantidad = Double.parseDouble(retiro);

            if (cantidad > Double.parseDouble(consultarSaldo())) {
                return "Saldo insuficiente";
            }
            if (cantidad > 0) {
                java.sql.Connection llamar = MConexion.getConnection();
                UsuarioActivo id = UsuarioActivo.getInstance();
                usuarioId = id.getId();

                String consultaActualizar = "UPDATE usuarios SET saldo = saldo - ? WHERE id = ?";
                PreparedStatement preparedStatement = llamar.prepareStatement(consultaActualizar);
                preparedStatement.setDouble(1, cantidad);
                preparedStatement.setInt(2, usuarioId);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    registrarOperacionHistorico("Retiro", cantidad);
                    return "Retiro realizado con exito. Su saldo es " + consultarSaldo();
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
            revision = Integer.parseInt(nuevoPin);
            revision = Integer.parseInt(pinAnterior);
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
                            return "PIN actualizado con exito.";
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

    public static void registrarOperacionHistorico(String tipoOperacion, double cantidad) throws SQLException {
        java.sql.Connection llamar = MConexion.getConnection();
        UsuarioActivo id = UsuarioActivo.getInstance();
        usuarioId = id.getId();

        String insertQuery = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = llamar.prepareStatement(insertQuery);
            preparedStatement.setInt(1, usuarioId);
            preparedStatement.setString(2, tipoOperacion);
            preparedStatement.setDouble(3, cantidad);
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
                double cantidad = resultSet.getDouble("cantidad");

                String signoMonto = tipoOperacion.equalsIgnoreCase("retiro") ? "-" : "+";
                historico.append(String.format("%s%.2f  %s\n", signoMonto, cantidad, tipoOperacion));
            }

            return historico.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error al consultar el historial del cliente.";
    }
}