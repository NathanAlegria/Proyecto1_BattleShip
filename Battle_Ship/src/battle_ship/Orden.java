/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java
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

    private final JComboBox<String> selectorBarco;
    private final JLabel infoBarco;
    private final JLabel lblDificultad;
    private final JLabel lblRestantes;

    private final LinkedHashMap<String, Barco> opciones = new LinkedHashMap<>();
    private final Set<String> colocados = new HashSet<>();
    private final HashMap<String, String> itemToId = new HashMap<>();

    public Orden(Tablero_Logico t) {
        this(t, "NORMAL");
    }

    public Orden(Tablero_Logico t, String dificultad) {

        this.tablero = t;

        String dif = (dificultad == null) ? "NORMAL" : dificultad;

        setLayout(new BorderLayout());

        configurarBarcosSegunDificultad(dif);
        limpiarMatriz();

        selectorBarco = new JComboBox<>();
        cargarComboDesdeOpciones();

        infoBarco = new JLabel("Selecciona un barco");
        lblDificultad = new JLabel("Dificultad: " + dif);
        lblRestantes = new JLabel("");

        selectorBarco.addActionListener(e -> {
            actualizarInfoBarco();
            actualizarRestantes();
        });

        JPanel grid = new JPanel(new GridLayout(SIZE, SIZE));
        grid.setPreferredSize(new Dimension(SIZE * CELL_SIZE, SIZE * CELL_SIZE));

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {

                JButton b = new JButton("~");
                b.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                b.setFocusPainted(false);
                b.setBackground(new Color(180, 210, 230));
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

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(lblDificultad);
        top.add(new JLabel(" | Barco: "));
        top.add(selectorBarco);
        top.add(infoBarco);
        top.add(lblRestantes);
        top.add(random);

        JPanel bottom = new JPanel();
        bottom.add(rotar);

        add(top, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        actualizarInfoBarco();
        actualizarRestantes();
    }

    private void configurarBarcosSegunDificultad(String dif) {

        Barco PA = new Barco("PA", "P", 5);
        Barco AZ = new Barco("AZ", "A", 4);
        Barco SM = new Barco("SM", "S", 3);
        Barco DT = new Barco("DT", "D", 2);

        ArrayList<Barco> lista = new ArrayList<>();

        switch (dif) {
            case "EASY": {
                lista.add(PA);
                lista.add(AZ);
                lista.add(SM);
                lista.add(DT);
                lista.add(new Barco("DT", "D", 2)); // EASY: repetido fijo 1 Destructor
                break;
            }
            case "EXPERT": {
                ArrayList<Barco> pool = new ArrayList<>(Arrays.asList(PA, AZ, SM, DT));
                Collections.shuffle(pool);
                lista.add(pool.get(0));
                lista.add(pool.get(1));
                break;
            }
            case "GENIUS": {
                Barco[] base = {PA, AZ, SM, DT};
                lista.add(base[new Random().nextInt(base.length)]);
                break;
            }
            default: {
                lista.add(PA);
                lista.add(AZ);
                lista.add(SM);
                lista.add(DT);
                break;
            }
        }

        tablero.barcos.clear();
        tablero.barcos.addAll(lista);

        opciones.clear();
        itemToId.clear();

        HashMap<String, Integer> contador = new HashMap<>();

        for (Barco b : lista) {
            int n = contador.getOrDefault(b.prefijo, 0) + 1;
            contador.put(b.prefijo, n);

            String item = (n == 1) ? b.codigo : (b.codigo + " (" + n + ")");
            opciones.put(item, b);

            String id = b.prefijo + "#" + n;
            itemToId.put(item, id);
        }
    }

    private void cargarComboDesdeOpciones() {
        selectorBarco.removeAllItems();
        for (String key : opciones.keySet()) selectorBarco.addItem(key);
    }

    private void actualizarInfoBarco() {
        String item = (String) selectorBarco.getSelectedItem();
        if (item == null) {
            infoBarco.setText("Todos los barcos colocados");
            return;
        }
        Barco b = opciones.get(item);
        if (b != null) infoBarco.setText("Barco " + b.codigo + " - Largo: " + b.tamaño);
    }

    private void actualizarRestantes() {
        lblRestantes.setText(" | Faltan: " + selectorBarco.getItemCount());
    }

    private void colocar(int f, int c) {

        String item = (String) selectorBarco.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Ya colocaste todos los barcos");
            return;
        }

        if (colocados.contains(item)) {
            JOptionPane.showMessageDialog(this, "Ese barco ya fue colocado");
            return;
        }

        Barco barco = opciones.get(item);
        String id = itemToId.get(item);
        if (barco == null || id == null) return;

        if (!puedeColocarLocal(barco, f, c, horizontal)) {
            JOptionPane.showMessageDialog(this, "No se puede colocar aquí");
            return;
        }

        for (int i = 0; i < barco.tamaño; i++) {
            int ff = f + (horizontal ? 0 : i);
            int cc = c + (horizontal ? i : 0);

            tablero.matriz[ff][cc] = id;

            botones[ff][cc].setIcon(escalarImagen(barco.prefijo, i + 1, horizontal));
            botones[ff][cc].setText("");
        }

        colocados.add(item);
        selectorBarco.removeItem(item);

        actualizarInfoBarco();
        actualizarRestantes();

        if (selectorBarco.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Todos los barcos colocados");
        }
    }

    public void colocarRandom() {
        limpiarMatriz();
        colocados.clear();

        colocarTodosRandomLocal();
        redibujarDesdeLogica();

        selectorBarco.removeAllItems();
        infoBarco.setText("Todos los barcos colocados");
        actualizarRestantes();
    }

    private void colocarTodosRandomLocal() {
        Random rand = new Random();
        HashMap<String, Integer> contador = new HashMap<>();

        for (Barco b : tablero.barcos) {
            int n = contador.getOrDefault(b.prefijo, 0) + 1;
            contador.put(b.prefijo, n);
            String id = b.prefijo + "#" + n;

            boolean colocadoOK = false;
            while (!colocadoOK) {
                int f = rand.nextInt(SIZE);
                int c = rand.nextInt(SIZE);
                boolean horiz = rand.nextBoolean();

                if (puedeColocarLocal(b, f, c, horiz)) {
                    for (int i = 0; i < b.tamaño; i++) {
                        int ff = f + (horiz ? 0 : i);
                        int cc = c + (horiz ? i : 0);
                        tablero.matriz[ff][cc] = id;
                    }
                    colocadoOK = true;
                }
            }
        }
    }

    private void redibujarDesdeLogica() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                botones[i][j].setIcon(null);
                botones[i][j].setText("~");
            }
        }

        HashMap<String, Integer> contador = new HashMap<>();

        for (Barco b : tablero.barcos) {
            int n = contador.getOrDefault(b.prefijo, 0) + 1;
            contador.put(b.prefijo, n);

            String id = b.prefijo + "#" + n;
            int parte = 1;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (id.equals(tablero.matriz[i][j])) {
                        botones[i][j].setIcon(escalarImagen(b.prefijo, parte++, true));
                        botones[i][j].setText("");
                    }
                }
            }
        }
    }

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

    private ImageIcon escalarImagen(String prefijo, int parte, boolean horizontal) {
        String ruta = "/Imagenes/" + prefijo + parte + ".png";
        java.net.URL url = getClass().getResource(ruta);

        if (url == null) return null;

        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH);

        if (!horizontal) img = rotarImagen(img);
        return new ImageIcon(img);
    }

    private Image rotarImagen(Image img) {
        BufferedImage original = new BufferedImage(CELL_SIZE, CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = original.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        BufferedImage rotated = new BufferedImage(CELL_SIZE, CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = rotated.createGraphics();
        g2.rotate(Math.toRadians(90), CELL_SIZE / 2.0, CELL_SIZE / 2.0);
        g2.drawImage(original, 0, 0, null);
        g2.dispose();

        return rotated;
    }
}

