/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battle_ship;

/**
 *
 * @author Nathan
 */

public class Barco {
    public String codigo;   // PA, AZ, SM, DT
    public String prefijo;  // P, A, S, D
    public int tamaño;
    public int vidas;

    public Barco(String c, String p, int t) {
        codigo = c;
        prefijo = p;
        tamaño = t;
        vidas = t;
    }

    public boolean estaHundido() {
        return vidas <= 0;
    }
}

