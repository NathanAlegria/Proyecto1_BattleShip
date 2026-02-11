/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battle_ship;

import javax.swing.*;
import java.awt.*;
import java.util.*;
/**
 *
 * @author Nathan
 */
public class Orden extends JPanel {

    private JButton[][] botones = new JButton[8][8];
    private Tablero_Logico tablero;
    private boolean horizontal = true;

    private Barco[] barcos;
    private int index = 0;

    public Orden(Tablero_Logico t) {
        this.tablero = t;
        setLayout(new BorderLayout());

        barcos = new Barco[]{
                new Barco("PA", "P", 5),
                new Barco("AZ", "A", 4),
                new Barco("SM", "S", 3),
                new Barco("DT", "D", 2)
        };

        tablero.barcos.clear();
        tablero.barcos.addAll(Arrays.asList(barcos));

        JPanel grid = new JPanel(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton b = new JButton("~");
                b.setFocusPainted(false);
                b.setBackground(new Color(180, 210, 230));
                final int r = i, c = j;
                b.addActionListener(e -> colocar(r, c));
                botones[i][j] = b;
                grid.add(b);
            }
        }

        JButton rotar = new JButton("Rotar");
        rotar.addActionListener(e -> horizontal = !horizontal);

        JButton random = new JButton("Random");
        random.addActionListener(e -> colocarRandom());

        JPanel top = new JPanel();
        top.add(random);

        JPanel bottom = new JPanel();
        bottom.add(rotar);

        add(top, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void colocar(int f, int c) {

        if (index >= barcos.length) {
            JOptionPane.showMessageDialog(this,
                    "Ya no hay barcos que colocar");
            return;
        }

        Barco b = barcos[index];

        if (!tablero.puedeColocar(b, f, c, horizontal)) {
            JOptionPane.showMessageDialog(this,
                    "No se puede colocar aquí");
            return;
        }

        for (int i = 0; i < b.tamaño; i++) {
            int ff = f + (horizontal ? 0 : i);
            int cc = c + (horizontal ? i : 0);

            tablero.matriz[ff][cc] = b.prefijo;
            botones[ff][cc].setIcon(
                    escalarImagen(b.prefijo, i + 1, botones[ff][cc])
            );
            botones[ff][cc].setText("");
        }

        index++;

        if (index >= barcos.length) {
            JOptionPane.showMessageDialog(this,
                    "Todos los barcos colocados. Presiona JUGAR.");
        }
    }

    public void colocarRandom() {
        tablero.generarRandom();
        redibujarDesdeLogica();
        index = barcos.length;
    }

    private void redibujarDesdeLogica() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                botones[i][j].setIcon(null);
                botones[i][j].setText("~");
            }

        for (Barco b : tablero.barcos) {
            int parte = 1;
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                    if (b.prefijo.equals(tablero.matriz[i][j])) {
                        botones[i][j].setIcon(
                                escalarImagen(b.prefijo, parte++, botones[i][j])
                        );
                        botones[i][j].setText("");
                    }
        }
    }

    private ImageIcon escalarImagen(String prefijo, int parte, JButton btn) {
        ImageIcon icon = new ImageIcon(
                getClass().getResource(
                        "/Imagenes/" + prefijo + parte + ".png"
                )
        );

        Image img = icon.getImage().getScaledInstance(
                btn.getWidth(),
                btn.getHeight(),
                Image.SCALE_SMOOTH
        );

        return new ImageIcon(img);
    }
}

