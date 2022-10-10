package modelo;

import controlador.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ProductoDAO {
    
    // Variables globales
    Conexion conexion = new Conexion(); // instancia
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    
    // Método para listar los datos desde la Base de Datos
    public List listar(){
        String sql = "SELECT * FROM productos";
        List<Producto> lista = new ArrayList<>(); 
        try {
           con = conexion.conexionBD();
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery(); // 'Query' para consulta. 'Update' para agregar/modif
           
           while(rs.next()){
               Producto producto = new Producto();
               producto.setCodigo(rs.getInt(1));
               producto.setNombre(rs.getString(2));
               producto.setPrecio(rs.getDouble(3));
               producto.setInventario(rs.getInt(4));
               lista.add(producto);
           }
        } catch (Exception e) {
            System.out.println("Error en la consulta sql al Listar(dao):" + e);
        }
        
        return lista;
    } // Fin método Listar
    
    // Método para agregar producto a la Base de Datos
    public void agregar(Producto producto){
        String sql = "insert into productos (nombre, precio, inventario) values (?, ?, ?)";
        try {
            con = conexion.conexionBD();
            ps = con.prepareStatement(sql);
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getInventario());
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "El producto ya existe en el inventario" , "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error al agregar el producto(dao): " + e);
        }
    } // Fin del método Agregar
    
    // Método para actualizar datos del producto a la Base de Datos
    public void actualizar(Producto producto){
        String sql = "update productos set nombre=?, precio=?, inventario=? where codigo=?";
        try {
            con = conexion.conexionBD();
            ps = con.prepareStatement(sql);
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getInventario());
            ps.setInt(4, producto.getCodigo());
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar los datos(dao): " + e);
        }
    } // fin del método Actualizar
    
    // Método para borrar producto de la Base de Datos
    public void borrar(int id){
        String sql = "delete from productos where codigo="+id;
        try {
            con = conexion.conexionBD();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al borrar el producto(dao): " + e);
        }
    } // fin del método Borrar
    
    
    
} // Fin clase ProductoDAO
