/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sistema.empleadosDAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    
    
    
    public int ejecutarSentenciaSQL(String strSentenciaSQL){
        try {
            PreparedStatement pstm=con.prepareStatement(strSentenciaSQL);
            pstm.execute();
            return 1; 
                    } catch (SQLException e) {
            System.out.println(e);
            return 0;
                    }
    }
    
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
    
}
