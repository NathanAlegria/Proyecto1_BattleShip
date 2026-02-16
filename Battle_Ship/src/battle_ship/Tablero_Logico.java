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

    public String[][] matriz = new String[SIZE][SIZE];
    public ArrayList<Barco> barcos = new ArrayList<>();

    // 0 = no atacado, 1 = fallo, 2 = acierto
    private byte[][] estado = new byte[SIZE][SIZE];

    public Tablero_Logico() {
        limpiar();
    }

    public void limpiar() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matriz[i][j] = null;
                estado[i][j] = 0;
            }
        }
    }

    public byte getEstado(int f, int c) {
        return estado[f][c];
    }

    public boolean yaAtacado(int f, int c) {
        return estado[f][c] != 0;
    }

    //Lipiar Fallos (F) para siguiente turno
    public void limpiarFallosTurno() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (estado[i][j] == 1) estado[i][j] = 0;
            }
        }
    }

    public String idPorIndice(int idx) {
        if (idx < 0 || idx >= barcos.size()) return null;

        Barco b = barcos.get(idx);
        int count = 0;
        for (int i = 0; i <= idx; i++) {
            if (barcos.get(i).prefijo.equals(b.prefijo)) count++;
        }
        return b.prefijo + "#" + count;
    }

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

    public String atacar(int f, int c) {

        if (yaAtacado(f, c)) return null;

        String id = matriz[f][c];

        if (id == null) {
            estado[f][c] = 1; // fallo
            return null;
        }

        estado[f][c] = 2; // acierto

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

    //Limpiar Fallos
    private void limpiarFallos() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (estado[i][j] == 1) estado[i][j] = 0;
            }
        }
    }

    
    //Limpiar aciertos
    private void limpiarHits() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (estado[i][j] == 2) estado[i][j] = 0;
            }
        }
    }
    
    //Regenerar Tablero despues de acierto
    public boolean regenerarPosiciones() {

        String[][] backup = copiarMatriz();

        limpiarFallos();
        limpiarHits();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matriz[i][j] = null;
            }
        }

        Random r = new Random();

        for (int idx = 0; idx < barcos.size(); idx++) {

            Barco b = barcos.get(idx);
            if (b.estaHundido()) continue;

            String id = idPorIndice(idx);

            boolean colocado = false;
            int intentos = 0;

            while (!colocado && intentos < 4000) {
                intentos++;

                int f = r.nextInt(SIZE);
                int c = r.nextInt(SIZE);
                boolean hor = r.nextBoolean();

                if (puedeColocar(b, f, c, hor)) {
                    for (int i = 0; i < b.tamaño; i++) {
                        int ff = f + (hor ? 0 : i);
                        int cc = c + (hor ? i : 0);
                        matriz[ff][cc] = id;
                    }
                    colocado = true;
                }
            }

            if (!colocado) {
                matriz = backup;
                return false;
            }
        }

        return true;
    }

    private boolean puedeColocar(Barco b, int f, int c, boolean h) {
        for (int i = 0; i < b.tamaño; i++) {
            int ff = f + (h ? 0 : i);
            int cc = c + (h ? i : 0);

            if (ff < 0 || ff >= SIZE || cc < 0 || cc >= SIZE) return false;

            if (estado[ff][cc] != 0) return false; // no encima de atacadas
            if (matriz[ff][cc] != null) return false;
        }
        return true;
    }

    private String[][] copiarMatriz() {
        String[][] copia = new String[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(matriz[i], 0, copia[i], 0, SIZE);
        }
        return copia;
    }
}


