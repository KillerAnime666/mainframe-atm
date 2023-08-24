package bo.edu.ucb.sis213;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MValidacion {

    public int validar(String usuario, String pin) throws IOException, SQLException {
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
}
