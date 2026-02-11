/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battle_ship;

import Logica.Battleship;
import Logica.Player;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 *
 * @author Nathan
 */
public class Tablero extends JPanel {

    private static final int SIZE = 8;

    private JButton[][] tableroJugador = new JButton[SIZE][SIZE];
    private JButton[][] tableroEnemigo = new JButton[SIZE][SIZE];

    private Tablero_Logico logicaJugador;
    private Tablero_Logico logicaEnemigo;

    private PanelBarcos panelJugador;
    private PanelBarcos panelEnemigo;

    private JTextArea chat;
    private JLabel turnoLabel;

    private boolean turnoJugador = true;
    private boolean modoTutorial; // ✅ CORRECCIÓN

    private String nombreJugador;
    private String nombreEnemigo;

    private Random rand = new Random();

    /* ================= CONSTRUCTORES ================= */

    public Tablero(String jugador, String enemigo,
                   Tablero_Logico tJugador,
                   Tablero_Logico tEnemigo) {

        this(jugador, enemigo, tJugador, tEnemigo, false);
    }

    public Tablero(String jugador, String enemigo,
                   Tablero_Logico tJugador,
                   Tablero_Logico tEnemigo,
                   boolean tutorial) {

        nombreJugador = jugador;
        nombreEnemigo = enemigo;
        logicaJugador = tJugador;
        logicaEnemigo = tEnemigo;
        modoTutorial = tutorial;

        setLayout(new BorderLayout());

        add(crearTop(), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);
        add(crearChat(), BorderLayout.SOUTH);

        actualizarTurno();
    }

    /* ================= TOP ================= */

    private JPanel crearTop() {
        JPanel p = new JPanel(new GridLayout(1, 3));

        p.add(new JLabel("🧑 " + nombreJugador, SwingConstants.CENTER));

        turnoLabel = new JLabel("", SwingConstants.CENTER);
        p.add(turnoLabel);

        p.add(new JLabel("🤖 " + nombreEnemigo, SwingConstants.CENTER));

        return p;
    }

    /* ================= CENTRO ================= */

    private JPanel crearCentro() {
        JPanel centro = new JPanel(new GridLayout(1, 4, 10, 0));

        panelJugador = new PanelBarcos(logicaJugador.barcos, "Tus Barcos");
        panelEnemigo = new PanelBarcos(logicaEnemigo.barcos, "Barcos Enemigos");

        centro.add(panelJugador);
        centro.add(crearPanel(tableroJugador, false));
        centro.add(crearPanel(tableroEnemigo, true));
        centro.add(panelEnemigo);

        return centro;
    }

    private JPanel crearPanel(JButton[][] botones, boolean enemigo) {
        JPanel panel = new JPanel(new GridLayout(SIZE, SIZE));

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(50, 50));
                final int r = i, c = j;

                if (enemigo) {
                    b.addActionListener(e -> atacar(r, c));
                }

                botones[i][j] = b;
                panel.add(b);
            }
        }
        return panel;
    }

    /* ================= ATAQUES ================= */

    private void atacar(int f, int c) {
        if (!turnoJugador) return;

        if (logicaEnemigo.yaAtacado(f, c)) {
            JOptionPane.showMessageDialog(this, "⚠ Ya atacaste esa posición");
            return;
        }

        procesarAtaque(logicaEnemigo, tableroEnemigo, f, c, nombreJugador, panelEnemigo);

        if (logicaEnemigo.todosHundidos()) {
            finJuego(nombreJugador);
            return;
        }

        turnoJugador = false;
        actualizarTurno();
        ataqueEnemigo();
    }

    private void ataqueEnemigo() {
        int f, c;
        do {
            f = rand.nextInt(SIZE);
            c = rand.nextInt(SIZE);
        } while (logicaJugador.yaAtacado(f, c));

        procesarAtaque(logicaJugador, tableroJugador, f, c, nombreEnemigo, panelJugador);

        if (logicaJugador.todosHundidos()) {
            finJuego(nombreEnemigo);
            return;
        }

        turnoJugador = true;
        actualizarTurno();
    }

    private void procesarAtaque(Tablero_Logico t, JButton[][] botones,
                                int f, int c, String atacante,
                                PanelBarcos panel) {

        String hit = t.atacar(f, c);

        if (hit == null) {
            botones[f][c].setIcon(icon("miss"));
            chat.append("💦 " + atacante + " falló (" + f + "," + c + ")\n");
            return;
        }

        botones[f][c].setIcon(icon("hit"));
        chat.append("💥 " + atacante + " impactó " + hit + "\n");

        for (Barco b : t.barcos) {
            if (b.prefijo.equals(hit)) {
                panel.actualizar(b);
                if (b.estaHundido()) {
                    chat.append("🚢 " + b.codigo + " HUNDIDO\n");
                }
            }
        }
    }

    /* ================= FIN DEL JUEGO ================= */

    private void finJuego(String ganador) {
        JOptionPane.showMessageDialog(this, "🏆 GANADOR: " + ganador);

        Battleship game = Battleship.getInstance();
        Player p = game.buscarJugador(ganador);
        if (p != null) p.sumarPuntos(3);

        game.registrarPartida(
                nombreJugador,
                nombreEnemigo,
                ganador,
                "Hundió todos los barcos"
        );
    }

    /* ================= CHAT ================= */

    private JScrollPane crearChat() {
        chat = new JTextArea(6, 40);
        chat.setEditable(false);
        return new JScrollPane(chat);
    }

    private void actualizarTurno() {
        turnoLabel.setText(
                turnoJugador
                        ? "👉 Turno de " + nombreJugador
                        : "🤖 Turno del Enemigo"
        );
    }

    private ImageIcon icon(String name) {
        return new ImageIcon(
                getClass().getResource("/Imagenes/" + name + ".png")
        );
    }
}
 