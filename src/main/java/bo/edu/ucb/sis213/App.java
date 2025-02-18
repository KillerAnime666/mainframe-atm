package bo.edu.ucb.sis213;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class App {
    private static Scanner scanner = new Scanner(System.in);

    private static int usuarioId;
    private static double saldo;
    private static int pinActual;

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3306;
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String DATABASE = "atm";

    public static Connection getConnection() throws SQLException {
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found.", e);
        }

        return DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
    }

    public static void main(String[] args) {
        int intentos = 3;

        System.out.println("Bienvenido al Cajero Automático.");

        Connection connection = null;
        try {
            connection = getConnection();
        } catch (SQLException ex) {
            System.err.println("No se puede conectar a Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }
        
        while (intentos > 0) {
            System.out.print("Ingrese su PIN de 4 dígitos: ");
            int pinIngresado = scanner.nextInt();
            if (validarPIN(connection, pinIngresado)) {
                pinActual = pinIngresado;
                mostrarMenu(connection);
                break;
            } else {
                intentos--;
                if (intentos > 0) {
                    System.out.println("PIN incorrecto. Le quedan " + intentos + " intentos.");
                } else {
                    System.out.println("PIN incorrecto. Ha excedido el número de intentos.");
                    System.exit(0);
                }
            }
        }
    }

    public static boolean validarPIN(Connection connection, int pin) {
        String query = "SELECT id, saldo FROM usuarios WHERE pin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                usuarioId = resultSet.getInt("id");
                saldo = resultSet.getDouble("saldo");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void mostrarMenu(Connection connection) {
        while (true) {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Consultar saldo.");
            System.out.println("2. Realizar un depósito.");
            System.out.println("3. Realizar un retiro.");
            System.out.println("4. Cambiar PIN.");
            System.out.println("5. Salir.");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    consultarSaldo(connection);
                    break;
                case 2:
                    realizarDeposito(connection);
                    break;
                case 3:
                    realizarRetiro(connection);
                    break;
                case 4:
                    cambiarPIN(connection);
                    break;
                case 5:
                    System.out.println("Gracias por usar el cajero. ¡Hasta luego!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    public static void consultarSaldo(Connection connection) {
        String query = "SELECT saldo FROM usuarios WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, usuarioId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double saldo = resultSet.getDouble("saldo");
                System.out.println("Su saldo actual es: $" + saldo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void realizarDeposito(Connection connection) {
        System.out.print("Ingrese la cantidad a depositar: $");
        double cantidad = scanner.nextDouble();
    
        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
        } else {
            String updateQuery = "UPDATE usuarios SET saldo = saldo + ? WHERE id = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setDouble(1, cantidad);
                preparedStatement.setInt(2, usuarioId);
                int rowsAffected = preparedStatement.executeUpdate();
    
                if (rowsAffected > 0) {
                    System.out.println("Depósito realizado con éxito.");
                    registrarOperacionHistorico(connection, "deposito", cantidad);
    
                    consultarSaldo(connection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void realizarRetiro(Connection connection) {
        System.out.print("Ingrese la cantidad a retirar: $");
        double cantidad = scanner.nextDouble();
    
        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
        } else if (cantidad > saldo) {
            System.out.println("Saldo insuficiente.");
        } else {
            String updateQuery = "UPDATE usuarios SET saldo = saldo - ? WHERE id = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setDouble(1, cantidad);
                preparedStatement.setInt(2, usuarioId);
                int rowsAffected = preparedStatement.executeUpdate();
    
                if (rowsAffected > 0) {
                    System.out.println("Retiro realizado con éxito.");
                    registrarOperacionHistorico(connection, "retiro", cantidad);
    
                    consultarSaldo(connection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void cambiarPIN(Connection connection) {
        System.out.print("Ingrese su nuevo PIN: ");
        int nuevoPin = scanner.nextInt();
        System.out.print("Confirme su nuevo PIN: ");
        int confirmacionPin = scanner.nextInt();

        if (nuevoPin == confirmacionPin) {
            String updateQuery = "UPDATE usuarios SET pin = ? WHERE id = ? AND pin = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setInt(1, nuevoPin);
                preparedStatement.setInt(2, usuarioId);
                preparedStatement.setInt(3, pinActual);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    pinActual = nuevoPin;
                    System.out.println("PIN actualizado con éxito.");
                } else {
                    System.out.println("No se pudo actualizar el PIN.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Los PINs no coinciden.");
        }
    }

    public static void registrarOperacionHistorico(Connection connection, String tipoOperacion, double cantidad) {
        String insertQuery = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, usuarioId);
            preparedStatement.setString(2, tipoOperacion);
            preparedStatement.setDouble(3, cantidad);
            int rowsAffected = preparedStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("Operación registrada en histórico.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}