/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battle_ship;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 *
 * @author Nathan
 */

public class Orden extends JPanel {

    private static final int SIZE = 8;
    private static final int CELL_SIZE = 60;

    private final JButton[][] botones = new JButton[SIZE][SIZE];
    private final Tablero_Logico tablero;

    private boolean horizontal = true;

    // Barcos disponibles (NO se eliminan de tablero.barcos; solo se marcan como colocados)
    private final Barco[] barcosBase;

    private final JComboBox<String> selectorBarco;
    private final JLabel infoBarco;

    // Guardamos qué barcos ya fueron colocados (por código: PA, AZ, SM, DT)
    private final Set<String> colocados = new HashSet<>();

    public Orden(Tablero_Logico t) {

        this.tablero = t;
        setLayout(new BorderLayout());

        // ======= Definición de barcos =======
        // codigo = lo que ve el usuario (PA, AZ...)
        // prefijo = lo que se guarda en matriz para identificar imagen (P, A...)
        // tamaño = largo
        barcosBase = new Barco[]{
                new Barco("PA", "P", 5),
                new Barco("AZ", "A", 4),
                new Barco("SM", "S", 3),
                new Barco("DT", "D", 2)
        };

        // Asegurar que la lógica tenga estos barcos (y sus vidas correctas)
        // OJO: No los borres luego, el juego los necesita para vidas/hundidos/regeneración.
        tablero.barcos.clear();
        tablero.barcos.addAll(Arrays.asList(barcosBase));

        // Limpia matriz lógica al iniciar orden
        limpiarMatriz();

        // ======= UI: selector =======
        selectorBarco = new JComboBox<>();
        for (Barco b : barcosBase) selectorBarco.addItem(b.codigo);

        infoBarco = new JLabel("Selecciona un barco");

        selectorBarco.addActionListener(e -> actualizarInfoBarco());

        // ======= UI: grid =======
        JPanel grid = new JPanel(new GridLayout(SIZE, SIZE));
        grid.setPreferredSize(new Dimension(SIZE * CELL_SIZE, SIZE * CELL_SIZE));

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

                JButton b = new JButton("~");
                b.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                b.setFocusPainted(false);
                b.setBackground(new Color(180, 210, 230));
                b.setHorizontalAlignment(SwingConstants.CENTER);
                b.setVerticalAlignment(SwingConstants.CENTER);
                b.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                final int r = i, c = j;
                b.addActionListener(ev -> colocar(r, c));

                botones[i][j] = b;
                grid.add(b);
            }
        }

        JButton rotar = new JButton("Rotar");
        rotar.addActionListener(e -> horizontal = !horizontal);

        JButton random = new JButton("Random");
        random.addActionListener(e -> colocarRandom());

        JPanel top = new JPanel();
        top.add(new JLabel("Barco: "));
        top.add(selectorBarco);
        top.add(infoBarco);
        top.add(random);

        JPanel bottom = new JPanel();
        bottom.add(rotar);

        add(top, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        actualizarInfoBarco();
    }

    private void actualizarInfoBarco() {
        String codigo = (String) selectorBarco.getSelectedItem();
        if (codigo == null) {
            infoBarco.setText("Todos los barcos colocados");
            return;
        }
        Barco b = buscarBarcoPorCodigo(codigo);
        if (b != null) infoBarco.setText("Barco " + b.codigo + " - Largo: " + b.tamaño);
    }

    // =========================
    // COLOCAR BARCO
    // =========================
    private void colocar(int f, int c) {

        String codigo = (String) selectorBarco.getSelectedItem();
        if (codigo == null) {
            JOptionPane.showMessageDialog(this, "Ya colocaste todos los barcos");
            return;
        }

        if (colocados.contains(codigo)) {
            JOptionPane.showMessageDialog(this, "Ese barco ya fue colocado");
            return;
        }

        Barco barco = buscarBarcoPorCodigo(codigo);
        if (barco == null) return;

        if (!puedeColocarLocal(barco, f, c, horizontal)) {
            JOptionPane.showMessageDialog(this, "No se puede colocar aquí");
            return;
        }

        // Colocar en matriz lógica + iconos
        for (int i = 0; i < barco.tamaño; i++) {

            int ff = f + (horizontal ? 0 : i);
            int cc = c + (horizontal ? i : 0);

            tablero.matriz[ff][cc] = barco.prefijo; // guardamos prefijo para dibujo

            botones[ff][cc].setIcon(
                    escalarImagen(barco.prefijo, i + 1, horizontal)
            );
            botones[ff][cc].setText("");
        }

        // Marcar como colocado y quitarlo del selector (PERO NO de tablero.barcos)
        colocados.add(codigo);
        selectorBarco.removeItem(codigo);

        if (selectorBarco.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Todos los barcos colocados");
        } else {
            actualizarInfoBarco();
        }
    }

    // =========================
    // COLOCACIÓN RANDOM
    // =========================
    public void colocarRandom() {

        // Reset visual + lógica
        limpiarMatriz();
        colocados.clear();

        // Si tu Tablero_Logico.generarRandom() coloca usando b.codigo en matriz,
        // aquí NO conviene. Para Orden necesitamos prefijo para pintar imágenes.
        // Entonces hacemos random acá mismo (con prefijo) para que siempre pinte.
        colocarTodosRandomLocal();

        redibujarDesdeLogica();
        selectorBarco.removeAllItems(); // ya están todos colocados
        infoBarco.setText("Todos los barcos colocados");
    }

    private void colocarTodosRandomLocal() {
        Random rand = new Random();

        for (Barco b : tablero.barcos) {

            boolean colocadoOK = false;
            while (!colocadoOK) {
                int f = rand.nextInt(SIZE);
                int c = rand.nextInt(SIZE);
                boolean horiz = rand.nextBoolean();

                if (puedeColocarLocal(b, f, c, horiz)) {
                    for (int i = 0; i < b.tamaño; i++) {
                        int ff = f + (horiz ? 0 : i);
                        int cc = c + (horiz ? i : 0);
                        tablero.matriz[ff][cc] = b.prefijo;
                    }
                    colocadoOK = true;
                }
            }
        }
    }

    // =========================
    // REDIBUJAR DESDE LÓGICA
    // =========================
    private void redibujarDesdeLogica() {

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                botones[i][j].setIcon(null);
                botones[i][j].setText("~");
                botones[i][j].revalidate();
                botones[i][j].repaint();
            }
        }

        // Pintar por barco, buscando sus partes en la matriz
        for (Barco b : tablero.barcos) {

            int parte = 1;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {

                    if (b.prefijo.equals(tablero.matriz[i][j])) {
                        botones[i][j].setIcon(escalarImagen(b.prefijo, parte++, true));
                        botones[i][j].setText("");
                    }
                }
            }
        }
    }

    // =========================
    // VALIDACIÓN LOCAL DE COLOCACIÓN
    // (No depende de Tablero_Logico.puedeColocar)
    // =========================
    private boolean puedeColocarLocal(Barco b, int f, int c, boolean horizontal) {

        for (int i = 0; i < b.tamaño; i++) {

            int ff = f + (horizontal ? 0 : i);
            int cc = c + (horizontal ? i : 0);

            if (ff < 0 || cc < 0 || ff >= SIZE || cc >= SIZE) return false;
            if (tablero.matriz[ff][cc] != null) return false;
        }

        return true;
    }

    private void limpiarMatriz() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                tablero.matriz[i][j] = null;
    }

    private Barco buscarBarcoPorCodigo(String codigo) {
        for (Barco b : tablero.barcos) {
            if (b.codigo.equals(codigo)) return b;
        }
        return null;
    }

    // =========================
    // ESCALAR + ROTAR
    // =========================
    private ImageIcon escalarImagen(String prefijo, int parte, boolean horizontal) {

        String ruta = "/Imagenes/" + prefijo + parte + ".png";
        java.net.URL url = getClass().getResource(ruta);

        if (url == null) {
            System.out.println("No se encontró imagen: " + ruta);
            return null;
        }

        ImageIcon icon = new ImageIcon(url);

        Image img = icon.getImage().getScaledInstance(
                CELL_SIZE,
                CELL_SIZE,
                Image.SCALE_SMOOTH
        );

        if (!horizontal) img = rotarImagen(img);

        return new ImageIcon(img);
    }

    private Image rotarImagen(Image img) {

        BufferedImage original = new BufferedImage(
                CELL_SIZE,
                CELL_SIZE,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = original.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        BufferedImage rotated = new BufferedImage(
                CELL_SIZE,
                CELL_SIZE,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = rotated.createGraphics();
        g2.rotate(Math.toRadians(90), CELL_SIZE / 2.0, CELL_SIZE / 2.0);
        g2.drawImage(original, 0, 0, null);
        g2.dispose();

        return rotated;
    }
}
