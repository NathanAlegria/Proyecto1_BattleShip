/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battle_ship;

import java.util.ArrayList;
import java.util.Random;
/**
 *
 * @author Nathan
 */
public class Tablero_Logico {

    public static final int SIZE = 8;

    // ✅ Guarda ID único: "D#1", "D#2", "S#1"...
    public String[][] matriz = new String[SIZE][SIZE];

    public ArrayList<Barco> barcos = new ArrayList<>();

    private boolean[][] atacado = new boolean[SIZE][SIZE];

    public Tablero_Logico() {
        limpiar();
    }

    public void limpiar() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matriz[i][j] = null;
                atacado[i][j] = false;
            }
        }
    }

    // ✅ Devuelve el ID que le corresponde al barco por su posición en "barcos"
    // Ej: si es el 2do Destructor en la lista => "D#2"
    public String idPorIndice(int idx) {
        if (idx < 0 || idx >= barcos.size()) return null;

        Barco b = barcos.get(idx);
        int count = 0;

        for (int i = 0; i <= idx; i++) {
            if (barcos.get(i).prefijo.equals(b.prefijo)) count++;
        }
        return b.prefijo + "#" + count;
    }

    // ✅ Convierte "D#2" -> devuelve el 2do barco con prefijo D en la lista (sin maps)
    public Barco barcoDeId(String id) {
        if (id == null) return null;

        int pos = id.indexOf('#');
        if (pos == -1) return null;

        String prefijo = id.substring(0, pos);
        int num;
        try {
            num = Integer.parseInt(id.substring(pos + 1));
        } catch (NumberFormatException ex) {
            return null;
        }

        int count = 0;
        for (Barco b : barcos) {
            if (b.prefijo.equals(prefijo)) {
                count++;
                if (count == num) return b;
            }
        }
        return null;
    }

    public boolean yaAtacado(int f, int c) {
        return atacado[f][c];
    }

    // ✅ Regenera posiciones (mezcla) y reinicia ataques
    public void regenerarPosiciones() {
        generarRandom();
    }

    // ✅ Coloca todos los barcos VIVOS aleatoriamente dentro del tablero
    public void generarRandom() {
        limpiarMatrizSolo();

        Random r = new Random();

        // Colocar barcos por índice para usar su ID único
        for (int idx = 0; idx < barcos.size(); idx++) {
            Barco b = barcos.get(idx);
            if (b.estaHundido()) continue;

            String id = idPorIndice(idx);

            boolean colocado = false;
            while (!colocado) {
                int f = r.nextInt(SIZE);
                int c = r.nextInt(SIZE);
                boolean hor = r.nextBoolean();

                if (puedeColocarTam(b, f, c, hor)) {
                    for (int i = 0; i < b.tamaño; i++) {
                        int ff = f + (hor ? 0 : i);
                        int cc = c + (hor ? i : 0);
                        matriz[ff][cc] = id;   // ✅ ID único
                    }
                    colocado = true;
                }
            }
        }

        // ✅ reset ataques (porque el tablero cambió)
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                atacado[i][j] = false;
            }
        }
    }

    private void limpiarMatrizSolo() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                matriz[i][j] = null;
    }

    private boolean puedeColocarTam(Barco b, int f, int c, boolean h) {
        for (int i = 0; i < b.tamaño; i++) {
            int ff = f + (h ? 0 : i);
            int cc = c + (h ? i : 0);

            if (ff < 0 || ff >= SIZE || cc < 0 || cc >= SIZE) return false;
            if (matriz[ff][cc] != null) return false;
        }
        return true;
    }

    // ✅ devuelve ID si pegó, null si falló o si ya atacó ahí
    public String atacar(int f, int c) {
        if (yaAtacado(f, c)) return null;

        atacado[f][c] = true;

        if (matriz[f][c] == null) return null;

        String id = matriz[f][c];
        matriz[f][c] = null;

        // ✅ baja vida del barco EXACTO (por id)
        Barco b = barcoDeId(id);
        if (b != null) b.recibirImpacto();

        return id;
    }

    public boolean todosHundidos() {
        for (Barco b : barcos) {
            if (!b.estaHundido()) return false;
        }
        return true;
    }
}
