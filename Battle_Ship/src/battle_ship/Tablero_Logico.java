/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battle_ship;

import java.util.*;

/**
 *
 * @author Nathan
 */
public class Tablero_Logico {

    public static final int SIZE = 8;

    // En esta matriz se guardan PREFIJOS: "P","A","S","D" (o null)
    public String[][] matriz = new String[SIZE][SIZE];
    public List<Barco> barcos = new ArrayList<>();

    private final Random rand = new Random();

    public Tablero_Logico() {
        // Si tú lo construyes desde afuera (con Orden), no necesitas auto-crear aquí.
        // Pero igual dejamos matriz limpia.
        limpiarMatriz();
    }

    public void limpiarMatriz() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                matriz[i][j] = null;
    }

    // Recoloca SOLO barcos vivos (NO reinicia vidas)
    public void regenerarPosiciones() {
        limpiarMatriz();

        for (Barco b : barcos) {
            if (b.estaHundido()) continue;
            colocarBarcoRandom(b);
        }
    }

    private void colocarBarcoRandom(Barco b) {

        boolean colocado = false;

        while (!colocado) {

            int f = rand.nextInt(SIZE);
            int c = rand.nextInt(SIZE);
            boolean horizontal = rand.nextBoolean();

            if (puedeColocar(b, f, c, horizontal)) {

                for (int i = 0; i < b.tamaño; i++) {
                    int ff = f + (horizontal ? 0 : i);
                    int cc = c + (horizontal ? i : 0);
                    matriz[ff][cc] = b.prefijo;
                }

                colocado = true;
            }
        }
    }

    // público por si lo usa alguna otra parte
    public boolean puedeColocar(Barco b, int f, int c, boolean horizontal) {

        for (int i = 0; i < b.tamaño; i++) {

            int ff = f + (horizontal ? 0 : i);
            int cc = c + (horizontal ? i : 0);

            if (ff < 0 || cc < 0 || ff >= SIZE || cc >= SIZE) return false;
            if (matriz[ff][cc] != null) return false;
        }

        return true;
    }

    public Barco buscarBarcoPorPrefijo(String prefijo) {
        for (Barco b : barcos) {
            if (b.prefijo.equals(prefijo)) return b;
        }
        return null;
    }

    public boolean todosHundidos() {
        for (Barco b : barcos) {
            if (!b.estaHundido()) return false;
        }
        return true;
    }

    // útil si quieres mostrar "cuántos barcos quedan"
    public int contarBarcosVivos() {
        int vivos = 0;
        for (Barco b : barcos) if (!b.estaHundido()) vivos++;
        return vivos;
    }
}
