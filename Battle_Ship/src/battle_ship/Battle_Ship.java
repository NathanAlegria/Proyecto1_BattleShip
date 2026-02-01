/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package battle_ship;

import javax.swing.SwingUtilities;

/**
 *
 * @author Nathan
 */
public class Battle_Ship {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Menu(); 
        }); 
    }
    
}
 