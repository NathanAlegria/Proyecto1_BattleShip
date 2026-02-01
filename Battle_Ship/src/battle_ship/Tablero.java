/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battle_ship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;
/**
 *
 * @author Nathan
 */

public class Tablero extends JPanel {

    private static final int SIZE = 8;

    private JButton[][] celdas;
    private String[][] matrizLogica;
    private Image fondoTablero;

    private boolean horizontal = true;
    private Barco barcoActual = null;

    /* ===================== BARCO ===================== */
    private static class Barco {
        String codigo;
        int tamaño;
        String prefijo;

        Barco(String c, int t, String p) {
            codigo = c;
            tamaño = t;
            prefijo = p;
        }
    }

    private Barco[] barcos = {
        new Barco("PA", 5, "P"), // P1-P5
        new Barco("AZ", 4, "A"), // A1-A4
        new Barco("SM", 3, "S"), // S1-S3
        new Barco("DT", 2, "D")  // D1-D2
    };

    /* ===================== CONSTRUCTOR ===================== */
    public Tablero() {
        setLayout(new GridLayout(SIZE, SIZE, 2, 2));
        setOpaque(false);

        celdas = new JButton[SIZE][SIZE];
        matrizLogica = new String[SIZE][SIZE];

        try {
            fondoTablero = ImageIO.read(
                getClass().getResource("/Imagenes/fjuego.png"));
        } catch (Exception e) {
            System.out.println("No se pudo cargar fondo fjuego");
        }

        inicializarTablero();
        limpiarMatrizLogica();

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    horizontal = !horizontal;
                }
            }
        });
    }

    /* ===================== TABLERO ===================== */
    private void inicializarTablero() {
        removeAll();

        for (int f = 0; f < SIZE; f++) {
            for (int c = 0; c < SIZE; c++) {

                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 16));
                btn.setForeground(Color.YELLOW);
                btn.setBackground(new Color(40, 40, 40));
                btn.setFocusPainted(false);

                // 🔒 ELIMINA EFECTO HOVER
                btn.setRolloverEnabled(false);
                btn.setContentAreaFilled(false);
                btn.setOpaque(true);

                btn.setText("~");

                int fila = f;
                int col = c;
                btn.addActionListener(e -> manejarClick(fila, col));

                celdas[f][c] = btn;
                add(btn);
            }
        }

        revalidate();
        repaint();
    }

    private void limpiarMatrizLogica() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                matrizLogica[i][j] = null;
    }

    /* ===================== CLICK ===================== */
    private void manejarClick(int fila, int col) {

        if (!celdas[fila][col].getText().equals("~"))
            return;

        if (matrizLogica[fila][col] != null) {
            mostrarImagenBarco(fila, col);
        } else {
            celdas[fila][col].setText("F");
            celdas[fila][col].setForeground(Color.YELLOW);
        }
    }

    /* ===================== IMÁGENES ===================== */
    private void mostrarImagenBarco(int fila, int col) {
        try {
            String pieza = matrizLogica[fila][col]; // P1, A3, S2, D1
            ImageIcon icono = new ImageIcon(
                getClass().getResource("/Imagenes/barcos/" + pieza + ".png")
            );

            Image img = icono.getImage().getScaledInstance(
                celdas[fila][col].getWidth(),
                celdas[fila][col].getHeight(),
                Image.SCALE_SMOOTH
            );

            celdas[fila][col].setIcon(new ImageIcon(img));
            celdas[fila][col].setText("");

        } catch (Exception e) {
            System.out.println("No se encontró imagen del barco");
        }
    }

    /* ===================== RANDOM ===================== */
    public void colocarBarcosRandom() {
        limpiarMatrizLogica();
        inicializarTablero();

        for (Barco b : barcos) {
            boolean colocado = false;

            while (!colocado) {
                boolean hor = Math.random() < 0.5;
                int fila = (int) (Math.random() * SIZE);
                int col = (int) (Math.random() * SIZE);

                if (puedeColocar(b, fila, col, hor)) {
                    colocarBarco(b, fila, col, hor);
                    colocado = true;
                }
            }
        }
    }

    private boolean puedeColocar(Barco b, int fila, int col, boolean horizontal) {
        for (int i = 0; i < b.tamaño; i++) {
            int f = fila + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);

            if (f < 0 || f >= SIZE || c < 0 || c >= SIZE)
                return false;

            if (matrizLogica[f][c] != null)
                return false;
        }
        return true;
    }

    private void colocarBarco(Barco b, int fila, int col, boolean horizontal) {
        for (int i = 0; i < b.tamaño; i++) {
            int f = fila + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);

            matrizLogica[f][c] = b.prefijo + (i + 1);
        }
    }

    /* ===================== FONDO ===================== */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (fondoTablero != null) {
            g.drawImage(fondoTablero, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

