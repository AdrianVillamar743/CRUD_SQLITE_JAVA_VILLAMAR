# sistema
 JAVA-SQLITE CRUD
En esta ocasión realizaremos un CRUD con java netbeans y SQLite Admin para la base de datos

1.- Crear nuestro proyecto Java en mi caso un java maven application
2.- Añadir paquete empleadosDAL (Estructura de los datos)
3.- Añadir paquete empleadosBL  (Conexión a la base de datos)
4.- Añadir paquete empleadosGUI (Interfaz gráfica)
5.- En el paquete empleadosGUI, añadimos un JFrame form
6.- En el jframe añadimos labels, botones, una tabla y text fields
7.- En el caso de los text fields les damos click mientras estamos en la vista de diseño, luego en las propiedades de este elemento seleccionamos la pestaña code, y modificamos variable name con el nombre de txt_id o txt_nombre dependiendo de que deseemos hacer.
8.- Por consiguiente hacemos lo mismo para todos los elementos existentes.
9.- Para la base de datos utilizaremos SQLite admin, crearemos una nueva base de datos y una tablas llamada empleado con un campo id autoincremental primary key, nombre y correo.
10.- Insertamos dos registros al menos
11.- Guardamos el registro de nueva base de datos en el archivo resultante, y tenemos almacenada nuestra ruta para próximo uso.
12.- En nuestro caso debemos añadir una dependencia a nuestro archivo pom.xml para poder conectarnos al sqlite admin
La dependencia nos permitirá usar la librería de conexión añadiendo las lineas de código que referenciaran a dicho archivo.

<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.36.0.3</version>
</dependency>


13.- En el paquete empleadosDAL crearemos un archivo de conexion, especificamente la clase conexion


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sistema.empleadosDAL;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author adrian
 */
public class conexion {
    String strConexiondb = "jdbc:sqlite:C:/Users/adrian/Documents/SQLLITE/sistema.s3db";
    Connection con = null;
    
    public conexion (){
        try {
            Class.forName("org.sqlite.JDBC");
            con=DriverManager.getConnection(strConexiondb);
            System.out.println("Conectada correctamente");
        } catch (Exception e) {
            System.out.println("Error de conexion fatal"+e);
        }
    }
}


Donde la variable strConexiondb guarda la ruta del archivo de la base de datos SQLite Admin que creamos.

14.- Ahora debemos probar esa conexión para ello vamos al frm que creamos, y le asignaremos esa función al botón agregar, luego le damos doble click y nos dirigimos a la vista de source dentro de una función de evento. Donde dentro de ella referenciaremos al objeto conexión

  private void btn_agregarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
        
     conexion objconexion=new conexion();
       objconexion.ejecutarSentenciaSQL("insert into empleado (nombre,correo) values ('nuevo','valor@gmail.com')");
      
        
    }  

Esto nos permitirá verificar que se conecte cada vez que realice un click en la salida por consola de nuestro frm
Por consiguiente no olvides referenciar a conexion en ese código

import com.mycompany.sistema.empleadosDAL.conexion;

Tambien se le añade una sentencia fija de datos para verificar la correcta inserción de los mismos.

15.- En nuestro archivo conexion.java añadiremos una funció para consultar información, obteniendo los datos solicitados a través de un parametro de consulta.

public ResultSet consultarRegistros(String strSentenciaSQL){
        try {
             PreparedStatement pstm=con.prepareStatement(strSentenciaSQL);
             ResultSet respuesta=pstm.executeQuery();
             return respuesta;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
          
    }

16.- Añadimos la consulta al la función asignada al botón agregar y verificamos la salida de los datos consultados en la consola

 private void btn_agregarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
     conexion objconexion=new conexion();
     objconexion.ejecutarSentenciaSQL("insert into empleado (nombre,correo) values ('nuevo','valor@gmail.com')");
        try {
            ResultSet resultado= objconexion.consultarRegistros("select *from empleado");
           while (resultado.next()){
               System.out.println(resultado.getString("id"));
               System.out.println(resultado.getString("nombre"));
               System.out.println(resultado.getString("correo"));
               
               
           }
                 
        } catch (Exception e){
            System.out.println(e);
        }
        
        
    }                               


17.- Ahora recuperaremos los datos de la interfaz de usuario para poder insertarlos en la base de datos por lo que nos dirigimos en la pestaña de código del formulario empleados y añadimos la siguiente función.

    public empleadosBL recuperarDatosGUI(){
        empleadosBL oEmpleado= new empleadosBL();
        int ID= (txt_id.getText().isEmpty()?0:Integer.parseInt(txt_id.getText())) ;   
        oEmpleado.setId(ID);
        oEmpleado.setNombre(txt_nombre.getText());
        oEmpleado.setCorreo(txt_correo.getText());
        return oEmpleado;
    }

18.- Modificamos la función insertar para poder recuperar los datos y obtener datos dinámicos para nuestra base de datos.

    // TODO add your handling code here:
     conexion objconexion=new conexion();
     empleadosBL Oempleado= recuperarDatosGUI();
     objconexion.ejecutarSentenciaSQL("insert into empleado (nombre,correo) values ('"+Oempleado.getNombre()+"','"+Oempleado.getCorreo()+"')");
        try {
            ResultSet resultado= objconexion.consultarRegistros("select *from empleado");
           while (resultado.next()){
               System.out.println(resultado.getString("id"));
               System.out.println(resultado.getString("nombre"));
               System.out.println(resultado.getString("correo"));
               
               
           }
                 
        } catch (Exception e){
            System.out.println(e);
        }
        
        
    }                        

19.- Asignaremos un modelo a nuestra tabla en el source del frmEmpleados empezaremos definiendo un modelo por defecto declarandolo de la siguiente manera.

 DefaultTableModel modelo;

Y en la clase inicial crearemos un arreglo String que almacenará los títulos que asignaremos a la tabla, asignaremos una variable de modelo y lo setearemos a la table

 public frmEmpleados() {
        initComponents();
        String [] titulos={"ID","Nombre","Correo"};
        modelo=new DefaultTableModel(null,titulos);
        tbl_empleados.setModel(modelo);
        mostrarDatos();
    }

20.- Dentro del código del frmEmpleados declararemos la función mostrarDatos lo que nos ayudará a obtener la información de la base de datos, añadirla al modelo y posteriormente cargarla al modelo que está asignado a la tabla.

  public void mostrarDatos(){
        conexion objconexion=new conexion();
        try {
            ResultSet resultado= objconexion.consultarRegistros("select *from empleado");
           while (resultado.next()){
           Object[] oid={    resultado.getString("id"),resultado.getString("nombre"),resultado.getString("correo")};
           modelo.addRow(oid);
           }
                 
        } catch (Exception e){
            System.out.println(e);
        }
    }
    

21.- Ahora añadiremos un evento a la tabla para que recoja los datos de la fila en la que el usuario haga click, recoger y mostrar esos datos dentro de la propia interfaz en caso de desear modificarlos o eliminarlos.

Para poder lograrlo vamos a la pestaña de diseño del formulario, damos click a la tabla, escogemos la pestaña events, y seleccionamos en nuestro caso tbl_empleadosMouseClicked

Primero contamos el número de clicks y luego asignamos los valores de la fila de acuerdo al orden de las columnas en los cuadros de texto correspondientes.

22.- Ahora utilizaremos al función del botón borrar, basandonos en el botón agregar, modificando la función SQL de inserción para eliminar nuestro registro quedando la función de la siguiente manera.
Claro previamente haciendo click en el botón borrar.

    private void btn_borrarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
          conexion objconexion=new conexion();
     empleadosBL Oempleado= recuperarDatosGUI();
     objconexion.ejecutarSentenciaSQL("delete from empleado where id="+Oempleado.getId());
    
 this.mostrarDatos();
        this.limpiar();
    }                                          

23.- Ahora añadiremos una pequeña modificación a nuestra función mostrarDatos. Preguntaremos antes de llamarlo si el modelo está lleno procederemos a vaciarlo, caso contrario cargará los datos de la consulta con estas simples líneas de código.
  public void mostrarDatos(){
          while (modelo.getRowCount()>0){
        modelo.removeRow(0);
   }
        conexion objconexion=new conexion();
        try {
            ResultSet resultado= objconexion.consultarRegistros("select *from empleado");
           while (resultado.next()){
           Object[] oid={    resultado.getString("id"),resultado.getString("nombre"),resultado.getString("correo")};
           modelo.addRow(oid);
           }
                 
        } catch (Exception e){
            System.out.println(e);
        }
    }

24.- Ahora realizaremos lo mismo solo que con la función editar, basandonos en la función borrar.

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
            conexion objconexion=new conexion();
     empleadosBL Oempleado= recuperarDatosGUI();
     objconexion.ejecutarSentenciaSQL("UPDATE empleado SET nombre='"+Oempleado.getNombre()+"' ,correo='"+Oempleado.getCorreo()+"' where id='"+Oempleado.getId()+"'");
        this.mostrarDatos();
    btn_agregar.setEnabled(false);
        btn_editar.setEnabled(true);
        btn_borrar.setEnabled(true);
    }                                          

25.- Finalmente agregamos una función limpiar que nos ayudará a vaciar los cuadros de texto para futuras operaciones

    public void limpiar(){
        txt_id.setText("");
        txt_nombre.setText("");
        txt_correo.setText("");
        btn_agregar.setEnabled(true);
        btn_editar.setEnabled(false);
        btn_borrar.setEnabled(false);

    }