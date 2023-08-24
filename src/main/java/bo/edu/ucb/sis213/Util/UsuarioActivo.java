package bo.edu.ucb.sis213.Util;

public class UsuarioActivo {

    private static UsuarioActivo instance;
    
    private int id;
    
    private String usuario;
    
    private int intentos = 3;

    private UsuarioActivo() { }

    public static UsuarioActivo getInstance() {
        if (instance == null) {
            instance = new UsuarioActivo();
        }
        return instance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }
    
    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public int getIntentos() {
        return intentos;
    }
}