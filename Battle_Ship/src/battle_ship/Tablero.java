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
import java.util.Random;

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
    private Barco[] barcos;
    private int barcoIndex = 0;
    private Barco barcoActual = null;

    // Clase interna Barco
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

    public Tablero() {
        setLayout(new GridLayout(SIZE, SIZE, 2, 2));
        setOpaque(false);

        celdas = new JButton[SIZE][SIZE];
        matrizLogica = new String[SIZE][SIZE];

        // Inicializar barcos
        barcos = new Barco[]{
            new Barco("PA", 5, "P"),
            new Barco("AZ", 4, "A"),
            new Barco("SM", 3, "S"),
            new Barco("DT", 2, "D")
        };
        barcoActual = barcos[barcoIndex];

        // Cargar fondo
        try {
            fondoTablero = ImageIO.read(getClass().getResource("/Imagenes/fjuego.png"));
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
                    JOptionPane.showMessageDialog(null,
                            "Orientación: " + (horizontal ? "Horizontal" : "Vertical"));
                }
            }
        });

        mostrarMensajeBarco();
    }

    /* ===================== INICIALIZAR TABLERO ===================== */
    private void inicializarTablero() {
        removeAll();
        for (int f = 0; f < SIZE; f++) {
            for (int c = 0; c < SIZE; c++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 16));
                btn.setForeground(Color.YELLOW);
                btn.setBackground(new Color(40, 40, 40));
                btn.setFocusPainted(false);
                btn.setRolloverEnabled(false);
                btn.setContentAreaFilled(false);
                btn.setOpaque(true);
                btn.setText("~");

                final int fila = f;
                final int col = c;
                btn.addActionListener(e -> colocarBarcoManual(fila, col));

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

    /* ===================== COLOCAR BARCO MANUAL ===================== */
    private void colocarBarcoManual(int fila, int col) {
        if (barcoActual == null) return;

        if (!puedeColocar(barcoActual, fila, col, horizontal)) {
            JOptionPane.showMessageDialog(this, "No se puede colocar aquí");
            return;
        }

        for (int i = 0; i < barcoActual.tamaño; i++) {
            int f = fila + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            matrizLogica[f][c] = barcoActual.prefijo + (i + 1);

            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/barcos/"
                        + barcoActual.prefijo + (i + 1) + ".png"));
                Image img = icon.getImage().getScaledInstance(
                        celdas[f][c].getWidth(),
                        celdas[f][c].getHeight(),
                        Image.SCALE_SMOOTH
                );
                celdas[f][c].setIcon(new ImageIcon(img));
                celdas[f][c].setText("");
            } catch (Exception e) {
                celdas[f][c].setText(barcoActual.prefijo + (i + 1));
            }
        }

        barcoIndex++;
        if (barcoIndex < barcos.length) {
            barcoActual = barcos[barcoIndex];
        } else {
            barcoActual = null;
            JOptionPane.showMessageDialog(this, "Todos los barcos colocados!");
        }
        mostrarMensajeBarco();
    }

    private boolean puedeColocar(Barco b, int fila, int col, boolean horizontal) {
        for (int i = 0; i < b.tamaño; i++) {
            int f = fila + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            if (f < 0 || f >= SIZE || c < 0 || c >= SIZE) return false;
            if (matrizLogica[f][c] != null) return false;
        }
        return true;
    }

    /* ===================== RANDOM ===================== */
    public void colocarBarcosRandom() {
        limpiarMatrizLogica();
        inicializarTablero();

        Random rand = new Random();

        for (Barco b : barcos) {
            boolean colocado = false;
            while (!colocado) {
                int fila = rand.nextInt(SIZE);
                int col = rand.nextInt(SIZE);
                boolean hor = rand.nextBoolean();
                if (puedeColocar(b, fila, col, hor)) {
                    for (int i = 0; i < b.tamaño; i++) {
                        int f = fila + (hor ? 0 : i);
                        int c = col + (hor ? i : 0);
                        matrizLogica[f][c] = b.prefijo + (i + 1);
                        try {
                            ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/barcos/"
                                    + b.prefijo + (i + 1) + ".png"));
                            Image img = icon.getImage().getScaledInstance(
                                    celdas[f][c].getWidth(),
                                    celdas[f][c].getHeight(),
                                    Image.SCALE_SMOOTH
                            );
                            celdas[f][c].setIcon(new ImageIcon(img));
                            celdas[f][c].setText("");
                        } catch (Exception e) {
                            celdas[f][c].setText(b.prefijo + (i + 1));
                        }
                    }
                    colocado = true;
                }
            }
        }
        barcoIndex = barcos.length;
        barcoActual = null;
        mostrarMensajeBarco();
    }

    /* ===================== MENSAJE BARCO ===================== */
    private void mostrarMensajeBarco() {
        if (barcoActual != null) {
            String msg = "Coloca tu barco: " + barcoActual.codigo +
                    " (" + barcoActual.tamaño + " casillas)";
            JOptionPane.showMessageDialog(this, msg);
        }
    }

    public String[][] getMatrizLogica() {
        return matrizLogica;
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
