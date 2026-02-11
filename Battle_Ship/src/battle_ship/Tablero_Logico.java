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

    public String[][] matriz = new String[SIZE][SIZE];
    public boolean[][] atacado = new boolean[SIZE][SIZE];
    public ArrayList<Barco> barcos = new ArrayList<>();

    public Tablero_Logico() {
        limpiar();
    }

    /* ================= LIMPIAR ================= */

    public void limpiar() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matriz[i][j] = null;
                atacado[i][j] = false;
            }
        }
    }

    /* ================= COLOCACIÓN ================= */

    public void generarRandom() {
        limpiar();
        Random r = new Random();

        for (Barco b : barcos) {
            boolean colocado = false;

            while (!colocado) {
                int f = r.nextInt(SIZE);
                int c = r.nextInt(SIZE);
                boolean hor = r.nextBoolean();

                if (puedeColocar(b, f, c, hor)) {
                    for (int i = 0; i < b.tamaño; i++) {
                        int ff = f + (hor ? 0 : i);
                        int cc = c + (hor ? i : 0);
                        matriz[ff][cc] = b.prefijo;
                    }
                    colocado = true;
                }
            }
        }
    }

    public boolean puedeColocar(Barco b, int f, int c, boolean h) {
        for (int i = 0; i < b.tamaño; i++) {
            int ff = f + (h ? 0 : i);
            int cc = c + (h ? i : 0);

            if (ff < 0 || ff >= SIZE || cc < 0 || cc >= SIZE) {
                return false;
            }

            if (matriz[ff][cc] != null) {
                return false;
            }
        }
        return true;
    }

    /* ================= ATAQUES ================= */

    public boolean yaAtacado(int f, int c) {
        return atacado[f][c];
    }

    /**
     * @return prefijo del barco si impacta, null si falla
     */
    public String atacar(int f, int c) {
        if (atacado[f][c]) {
            return null;
        }

        atacado[f][c] = true;

        if (matriz[f][c] == null) {
            return null;
        }

        String hit = matriz[f][c];
        matriz[f][c] = null;

        for (Barco b : barcos) {
            if (b.prefijo.equals(hit)) {
                b.vidas--;
                break;
            }
        }

        return hit;
    }

    /* ================= FIN DE JUEGO ================= */

    public boolean todosHundidos() {
        for (Barco b : barcos) {
            if (!b.estaHundido()) {
                return false;
            }
        }
        return true;
    }
}

