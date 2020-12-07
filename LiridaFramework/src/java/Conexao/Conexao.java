/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexao;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author maquina04
 */
public class Conexao {

    
    private static final String USERNAME = "postgres";
    private static final String DATABASE = "lirida_bd";    
    private static final String PASSWORD = "cross";
    //private static final String DATABASE_URL = "jdbc:postgresql://18.230.57.135:5432/";

    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/";

    private static Connection c = null;

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            if (c == null) {
                c = DriverManager.getConnection(DATABASE_URL + DATABASE, USERNAME, PASSWORD);
                //new Util().lerCaixaEntradaEmail();
                //System.out.println("Conexão Nova");
            } else {
                if (c.isClosed()) {
                    c = DriverManager.getConnection(DATABASE_URL + DATABASE, USERNAME, PASSWORD);
                }
                //System.out.println("Conexão Existente!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        getConnection();
    }

    
}