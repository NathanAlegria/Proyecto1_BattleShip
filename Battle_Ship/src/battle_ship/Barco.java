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
    public String prefijo;  // P, A, S, D  (para matriz/imágenes)
    public int tamaño;      // 5,4,3,2
    public int vidas;       // inicia = tamaño

    public Barco(String codigo, String prefijo, int tamaño) {
        this.codigo = codigo;
        this.prefijo = prefijo;
        this.tamaño = tamaño;
        this.vidas = tamaño;
    }

    public void recibirImpacto() {
        if (vidas > 0) vidas--;
    }

    public boolean estaHundido() {
        return vidas <= 0;
    }

    public int getVida() {
        return vidas;
    }
}

