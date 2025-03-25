/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bibliotech;


import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Tiago
 */
public class BiblioTech {

    
    public static void main(String[] args) {
        
        
        try (Connection conexao = DBConnection.conectar()) {
            
            conexao.close();
            System.out.println("Conexão bem-sucedida!");

        } catch (SQLException e) {
            System.err.println("Erro ao conectar no main: " + e.getMessage());
        }
        
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        
        
    }
    
}
