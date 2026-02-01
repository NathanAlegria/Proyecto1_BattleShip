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
    private int rows = 10, cols = 10;

    public Orden() {
        setLayout(new GridLayout(rows, cols));
        gridButtons = new JButton[rows][cols];
        shipTypes = new String[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JButton b = new JButton();
                b.setBackground(Color.CYAN);
                final int r = i, c = j;
                b.addActionListener(e -> colocarBarcoManual(r, c));
                gridButtons[i][j] = b;
                add(b);
            }
        }
    }

    // Para colocar barco manual (ejemplo, solo Acorazado)
    private void colocarBarcoManual(int row, int col) {
        String type = "A"; // ejemplo: Acorazado
        int size = 4;
        boolean horizontal = true; // aquí puedes agregar selector de orientación

        if (!puedeColocar(row, col, size, horizontal)) return;

        for (int i = 0; i < size; i++) {
            int r = row + (horizontal ? 0 : i);
            int c = col + (horizontal ? i : 0);
            shipTypes[r][c] = String.valueOf(type);
            ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/Barcos/A" + (i + 1) + ".png"));
            gridButtons[r][c].setIcon(icon);
        }
    }

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

    public void colocarRandom() {
        clearBoard();
        Random rand = new Random();

        int[][] barcos = {
            {4, 'A'}, // Acorazado
            {5, 'P'}, // Portaaviones
            {3, 'S'}, // Submarino
            {2, 'D'}  // Destructor
        };

        for (int[] b : barcos) {
            int size = b[0];
            char type = (char) b[1];
            boolean colocado = false;

            while (!colocado) {
                int r = rand.nextInt(rows);
                int c = rand.nextInt(cols);
                boolean horizontal = rand.nextBoolean();
                if (puedeColocar(r, c, size, horizontal)) {
                    for (int i = 0; i < size; i++) {
                        int row = r + (horizontal ? 0 : i);
                        int col = c + (horizontal ? i : 0);
                        shipTypes[row][col] = String.valueOf(type);
                        ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/Barcos/" + type + (i + 1) + ".png"));
                        gridButtons[row][col].setIcon(icon);
                    }
                    colocado = true;
                }
            }
        }
    }

    private void clearBoard() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                shipTypes[i][j] = null;
                gridButtons[i][j].setIcon(null);
            }
    }

    public String[][] getShipTypes() {
        return shipTypes;
    }
}


