package controlador;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Producto;
import modelo.ProductoDAO;
import vista.Principal;

public class ControladorProducto implements ActionListener {

    // Instancias
    Producto producto = new Producto();
    ProductoDAO productoDAO = new ProductoDAO(); // para acceder al método "listar"
    Principal vista = new Principal();
    DefaultTableModel modelo = new DefaultTableModel();
    
    // Variables globales
    private int codigo = 0;
    private String nombre;
    private double precio;
    private int inventario;
    

    // Constructor
    public ControladorProducto(Principal vista) {
        this.vista = vista;
        vista.setVisible(true); 
        agregarEventos();
        listarTabla();
    }
    
    // le agregar un ActionListener a cada uno de los botones de la interfaz
    private void agregarEventos(){
        vista.getBtnAgregar().addActionListener(this);
        vista.getBtnActualizar().addActionListener(this);
        vista.getBtnBorrar().addActionListener(this);
        vista.getBtnLimpiar().addActionListener(this);
        vista.getTblTabla().addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                llenarCampos(e);
            }
        });
    }
    
    
    private void listarTabla(){
        String[] titulos = new String[]{"Código", "Nombre", "Precio", "Inventario"};
        modelo = new DefaultTableModel(titulos, 0);
        List<Producto> listaProductos = productoDAO.listar();
        for(Producto producto : listaProductos){
            modelo.addRow(new Object[]{producto.getCodigo(), producto.getNombre(), producto.getPrecio(), producto.getInventario()});
        }
        vista.getTblTabla().setModel(modelo);
        vista.getTblTabla().setPreferredSize(new Dimension(350, modelo.getRowCount() * 16 ));
    }
    
    private void llenarCampos(MouseEvent e){
        JTable target = (JTable) e.getSource();
        codigo = (int) vista.getTblTabla().getModel().getValueAt(target.getSelectedRow(), 0);
        vista.getTxtNombre().setText(vista.getTblTabla().getModel().getValueAt(target.getSelectedRow(),1).toString());
        vista.getTxtPrecio().setText(vista.getTblTabla().getModel().getValueAt(target.getSelectedRow(),2).toString());
        vista.getTxtInventario().setText(vista.getTblTabla().getModel().getValueAt(target.getSelectedRow(),3).toString());
    }
    
    //----------------------------- validar formulario vista -----------------------------//
    private boolean validarDatos(){
        if("".equals(vista.getTxtNombre().getText()) || "".equals(vista.getTxtPrecio().getText()) || "".equals(vista.getTxtInventario().getText())){
            JOptionPane.showMessageDialog(null, "Alguno de los campos está vacío" , "Error", JOptionPane.ERROR_MESSAGE);
            return false; // si falta algun dato, retorna falso
        }
        return true;
    }
    
    // Método para cargar los datos - 3 en 1
    private boolean cargarDatos(){
        try {
            nombre = vista.getTxtNombre().getText();
            precio = Double.parseDouble(vista.getTxtPrecio().getText());
            inventario = Integer.parseInt(vista.getTxtInventario().getText());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Los compos 'precio' e 'inventario' deben ser numéricos" , "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error de formato al cargar los datos: " + e);
            return false;
        }
    }
    
    private void limpiarCampos(){
        vista.getTxtNombre().setText("");
        vista.getTxtPrecio().setText("");
        vista.getTxtInventario().setText("");
        codigo = 0;
        nombre = "";
        precio = 0;
        inventario = 0;
    }
    
    //----------------------------- Método Agregar -----------------------------//
    private void agregarProducto(){
        try {
            if(validarDatos()){
                if(cargarDatos()){
                    Producto producto = new Producto(nombre, precio, inventario);
                    productoDAO.agregar(producto);
                    JOptionPane.showMessageDialog(null, "Registro exitoso");
                    limpiarCampos();
                }
            }
        } catch (HeadlessException e) {
            System.out.println("Error al agregar Producto (en controlador): " + e);
        } finally{
            listarTabla();
        }
    } // fin método agregar
    
    //----------------------------- Método Actualizar -----------------------------//
    private void actualizarProducto(){
        try {
            if(validarDatos()){
                if(cargarDatos()){
                    Producto producto = new Producto(codigo, nombre, precio, inventario);
                    productoDAO.actualizar(producto);
                    JOptionPane.showMessageDialog(null, "Actualización exitosa");
                    limpiarCampos();
                }
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el producto" , "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error al actualizar Producto (en controlador): " + e);
        } finally{
            listarTabla();
        }
    } // fin método actualizar
    
    //----------------------------- Método Borrar -----------------------------//
    private void borrarProducto(){
        try {
                if(codigo != 0){
                    productoDAO.borrar(codigo);
                    JOptionPane.showMessageDialog(null, "Registro borrado exitosamente");
                    limpiarCampos();
                } else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un producto de la tabla" , "Error", JOptionPane.ERROR_MESSAGE);
                }
        }catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un producto de la tabla" , "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error al borrar Producto (en controlador): " + e);
        } finally{
            listarTabla();
        }
}
    
    // método para darle las acciones a los botones (implementado mediante el Action Listener)
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == vista.getBtnAgregar()){
            agregarProducto();
        }
        if(e.getSource() == vista.getBtnActualizar()){
            actualizarProducto();
        }
        
        if(e.getSource() == vista.getBtnBorrar()){
            borrarProducto();
        }
        
        if(e.getSource() == vista.getBtnLimpiar()){
            limpiarCampos();
        }
    }
    
}
