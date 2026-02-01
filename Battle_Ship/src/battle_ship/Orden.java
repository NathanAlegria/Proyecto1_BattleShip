/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battle_ship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
/**
 *
 * @author Nathan
 */
public class Orden extends JPanel {

    private JButton[][] gridButtons;
    private String[][] shipTypes;
    private int rows = 8, cols = 8;

    private boolean horizontal = true;
    private Barco[] barcos;
    private int barcoIndex = 0; // Para ir colocando barcos en orden
    private Barco barcoActual;

    // Clase interna para definir barcos
    public static class Barco {
        String codigo;
        int tamaño;
        String prefijo;
        Barco(String c, int t, String p) {
            codigo = c;
            tamaño = t;
            prefijo = p;
        }
    }

    public Orden() {
        setLayout(new BorderLayout());

        // Inicializar barcos
        barcos = new Barco[]{
                new Barco("PA", 5, "P"), // Portaaviones
                new Barco("AZ", 4, "A"), // Acorazado
                new Barco("SM", 3, "S"), // Submarino
                new Barco("DT", 2, "D")  // Destructor
        };
        barcoActual = barcos[barcoIndex];

        // Panel del tablero
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
        gridButtons = new JButton[rows][cols];
        shipTypes = new String[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton b = new JButton();
                b.setBackground(Color.CYAN);
                final int r = i, c = j;
                b.addActionListener(e -> colocarBarcoManual(r, c));
                gridButtons[i][j] = b;
                gridPanel.add(b);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        // Panel de opciones
        JPanel panelOpciones = new JPanel();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));

        JButton btnOrientacion = new JButton("Cambiar orientación");
        btnOrientacion.addActionListener(e -> {
            horizontal = !horizontal;
            JOptionPane.showMessageDialog(this,
                    "Orientación: " + (horizontal ? "Horizontal" : "Vertical"));
        });

        JButton btnRandom = new JButton("Colocar Random");
        btnRandom.addActionListener(e -> colocarRandom());

        panelOpciones.add(btnOrientacion);
        panelOpciones.add(Box.createVerticalStrut(10));
        panelOpciones.add(btnRandom);

        add(panelOpciones, BorderLayout.EAST);

        actualizarMensajeBarco();
    }

    /* ===================== COLOCACIÓN MANUAL ===================== */
    private void colocarBarcoManual(int row, int col) {
        if (barcoActual == null) return;

        if (!puedeColocar(row, col, barcoActual.tamaño, horizontal)) {
            JOptionPane.showMessageDialog(this, "No se puede colocar aquí");
            return;
        }

        // Colocar barco en la matriz y mostrar imágenes
        for (int i = 0; i < barcoActual.tamaño; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            shipTypes[r][c] = barcoActual.prefijo + (i + 1);
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/barcos/"
                        + barcoActual.prefijo + (i + 1) + ".png"));
                Image img = icon.getImage().getScaledInstance(
                        gridButtons[r][c].getWidth(),
                        gridButtons[r][c].getHeight(),
                        Image.SCALE_SMOOTH
                );
                gridButtons[r][c].setIcon(new ImageIcon(img));
                gridButtons[r][c].setText("");
            } catch (Exception e) {
                gridButtons[r][c].setText(barcoActual.prefijo + (i + 1));
            }
        }

        // Pasar al siguiente barco
        barcoIndex++;
        if (barcoIndex < barcos.length) {
            barcoActual = barcos[barcoIndex];
        } else {
            barcoActual = null;
            JOptionPane.showMessageDialog(this, "Todos los barcos colocados!");
        }
        actualizarMensajeBarco();
    }

    /* ===================== VERIFICAR COLOCACIÓN ===================== */
    private boolean puedeColocar(int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > cols) return false;
            for (int i = 0; i < size; i++)
                if (shipTypes[row][col + i] != null) return false;
        } else {
            if (row + size > rows) return false;
            for (int i = 0; i < size; i++)
                if (shipTypes[row + i][col] != null) return false;
        }
        return true;
    }

    /* ===================== RANDOM ===================== */
    public void colocarRandom() {
        limpiarTablero();
        Random rand = new Random();

        for (Barco b : barcos) {
            boolean colocado = false;
            while (!colocado) {
                int r = rand.nextInt(rows);
                int c = rand.nextInt(cols);
                boolean hor = rand.nextBoolean();
                if (puedeColocar(r, c, b.tamaño, hor)) {
                    for (int i = 0; i < b.tamaño; i++) {
                        int row = r + (hor ? 0 : i);
                        int col = c + (hor ? i : 0);
                        shipTypes[row][col] = b.prefijo + (i + 1);
                        try {
                            ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/barcos/"
                                    + b.prefijo + (i + 1) + ".png"));
                            Image img = icon.getImage().getScaledInstance(
                                    gridButtons[row][col].getWidth(),
                                    gridButtons[row][col].getHeight(),
                                    Image.SCALE_SMOOTH
                            );
                            gridButtons[row][col].setIcon(new ImageIcon(img));
                            gridButtons[row][col].setText("");
                        } catch (Exception e) {
                            gridButtons[row][col].setText(b.prefijo + (i + 1));
                        }
                    }
                    colocado = true;
                }
            }
        }

        barcoIndex = barcos.length;
        barcoActual = null;
        actualizarMensajeBarco();
    }

    /* ===================== LIMPIAR ===================== */
    private void limpiarTablero() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                shipTypes[i][j] = null;
                gridButtons[i][j].setIcon(null);
                gridButtons[i][j].setText("");
            }
        barcoIndex = 0;
        barcoActual = barcos[0];
    }

    /* ===================== MENSAJE BARCO ===================== */
    private void actualizarMensajeBarco() {
        if (barcoActual != null) {
            String mensaje = "Coloca tu barco: " + barcoActual.codigo +
                    " (" + barcoActual.tamaño + " casillas)";
            JOptionPane.showMessageDialog(this, mensaje);
        }
    }

    public String[][] getShipTypes() {
        return shipTypes;
    }
}


