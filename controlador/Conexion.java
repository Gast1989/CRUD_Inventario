package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion {

    // variables constantes
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost:3306/";
    private final String DB = "inventario";
    private final String USER = "root";
    private final String PASSWORD = "admin";
    
    public Connection con;
    
    public Connection conexionBD(){
        try {
            Class.forName(DRIVER);
            this.con=DriverManager.getConnection(URL+DB,USER,PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(0);
        }
        return this.con;
    }
}

    class Prueba{
        public static void main(String[] args) {
            Connection con;
            Conexion conexion = new Conexion();
            con = conexion.conexionBD();
            
        }
    }
