/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battle_ship;

import Logica.Battleship;
import Logica.Player;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Nathan
 */

public class Tablero extends JPanel {

    //Configuracion Tablero
    private static final int SIZE = 8;
    private static final int CELL = 60;

    private final JButton[][] tableroJugador = new JButton[SIZE][SIZE];
    private final JButton[][] tableroEnemigo = new JButton[SIZE][SIZE];

    private final Tablero_Logico logicaJugador;
    private final Tablero_Logico logicaEnemigo;

    private final boolean modoTutorial;
    private final boolean modoArcade;

    private final String nombreJugador;
    private final String nombreEnemigo;

    private final JTextArea chat = new JTextArea(6, 40);
    private JLabel turnoLabel;
    private boolean turnoDeJugador1 = true;

    private final JTextField filaInput = new JTextField(3);
    private final JTextField colInput  = new JTextField(3);

    //Barras de Vida
    private JProgressBar[] barrasJ1;
    private JProgressBar[] barrasJ2;

    private JPanel panelVidasJ1;
    private JPanel panelVidasJ2;

    private final Battleship sistema;
    private final Player p1;
    private final Player p2;
    private final JFrame ventanaJuego;
    private final Menu_Principal menuPrincipal;

    public Tablero(Battleship sistema,
                   Player p1,
                   Player p2,
                   String jugador,
                   String enemigo,
                   Tablero_Logico tJugador,
                   Tablero_Logico tEnemigo,
                   boolean tutorial,
                   JFrame ventanaJuego,
                   Menu_Principal menuPrincipal) {

        this.sistema = sistema;
        this.p1 = p1;
        this.p2 = p2;
        this.ventanaJuego = ventanaJuego;
        this.menuPrincipal = menuPrincipal;

        this.nombreJugador = jugador;
        this.nombreEnemigo = enemigo;

        this.logicaJugador = tJugador;
        this.logicaEnemigo = tEnemigo;

        this.modoTutorial = tutorial;
        this.modoArcade = "ARCADE".equalsIgnoreCase(sistema.getModoJuego());

        setLayout(new BorderLayout());

        add(crearTop(), BorderLayout.NORTH);      
        add(crearCentro(), BorderLayout.CENTER);   
        add(crearInferior(), BorderLayout.SOUTH);  

        chat.setEditable(false);

        construirBarras();
        actualizarTurno();

        SwingUtilities.invokeLater(() -> {
            boolean mostrarJ1 = !modoArcade;            
            boolean mostrarJ2 = (!modoArcade) && modoTutorial;

            redibujarTablero(tableroJugador, logicaJugador, mostrarJ1);
            redibujarTablero(tableroEnemigo, logicaEnemigo, mostrarJ2);

            revalidate();
            repaint();
        });
    }

        //Nombres y Turnos
    private JPanel crearTop() {
        JPanel p = new JPanel(new GridLayout(1, 3));
        JLabel j1 = new JLabel("Jugador: " + nombreJugador, SwingConstants.CENTER);
        JLabel j2 = new JLabel("Enemigo: " + nombreEnemigo, SwingConstants.CENTER);

        turnoLabel = new JLabel("", SwingConstants.CENTER);
        turnoLabel.setFont(new Font("Arial", Font.BOLD, 18));

        p.add(j1);
        p.add(turnoLabel);
        p.add(j2);
        return p;
    }

    // Fondo Tblero y Vidas
    private JPanel crearCentro() {
        JPanel fondo = new FondoPanel("/Imagenes/fjuego.jpg");
        fondo.setLayout(new BorderLayout());

        JPanel fila = new JPanel(new GridLayout(1, 4, 15, 0));
        fila.setOpaque(false);

        // Panel vidas jugador
        panelVidasJ1 = new PanelTransparente(new Color(0, 0, 0, 150));
        panelVidasJ1.setLayout(new GridLayout(0, 1, 0, 6));
        javax.swing.border.TitledBorder b1 = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Vidas " + nombreJugador
        );
        b1.setTitleColor(Color.WHITE);
        panelVidasJ1.setBorder(b1);

        // Tablero jugador
        JPanel panelTableroJugador = crearPanelConCoordenadas(tableroJugador);
        panelTableroJugador.setOpaque(false);

        // Tablero enemigo
        JPanel panelTableroEnemigo = crearPanelConCoordenadas(tableroEnemigo);
        panelTableroEnemigo.setOpaque(false);

        // Panel vidas enemigo
        panelVidasJ2 = new PanelTransparente(new Color(0, 0, 0, 150));
        panelVidasJ2.setLayout(new GridLayout(0, 1, 0, 6));
        javax.swing.border.TitledBorder b2 = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Vidas " + nombreEnemigo
        );
        b2.setTitleColor(Color.WHITE);
        panelVidasJ2.setBorder(b2);

        fila.add(panelVidasJ1);
        fila.add(panelTableroJugador);
        fila.add(panelTableroEnemigo);
        fila.add(panelVidasJ2);

        fondo.add(fila, BorderLayout.CENTER);
        return fondo;
    }

    // Crea tablero con coordenadas 0-7
    private JPanel crearPanelConCoordenadas(JButton[][] botones) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel grid = new JPanel(new GridLayout(SIZE + 1, SIZE + 1));
        grid.setOpaque(false);

        grid.add(labelCoord(""));
        for (int i = 0; i < SIZE; i++) grid.add(labelCoord(String.valueOf(i)));

        for (int i = 0; i < SIZE; i++) {
            grid.add(labelCoord(String.valueOf(i)));
            for (int j = 0; j < SIZE; j++) {

                JButton b = new JButton("~");
                b.setPreferredSize(new Dimension(CELL, CELL));
                b.setFocusPainted(false);
                b.setFont(new Font("Arial", Font.BOLD, 16));

                b.setRolloverEnabled(false);
                b.setBorderPainted(true);

                b.setHorizontalTextPosition(SwingConstants.CENTER);
                b.setVerticalTextPosition(SwingConstants.CENTER);
                b.setIconTextGap(0);
                b.setMargin(new Insets(0, 0, 0, 0));
                b.setContentAreaFilled(true);
                b.setOpaque(true);

                botones[i][j] = b;
                grid.add(b);
            }
        }

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JLabel labelCoord(String t) {
        JLabel l = new JLabel(t, SwingConstants.CENTER);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        return l;
    }

    //Ataque Retiro y Chat
    private JPanel crearInferior() {
        JPanel inferior = new JPanel(new BorderLayout());

        JPanel ataquePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));

        JLabel retiroInfo = new JLabel("🏳 RETIRO: escribe -1 en FILA y -1 en COLUMNA");
        retiroInfo.setFont(new Font("Arial", Font.BOLD, 14));
        retiroInfo.setForeground(new Color(150, 0, 0)); // rojo oscuro

        JButton atacarBtn = new JButton("Atacar");
        atacarBtn.addActionListener(e -> atacarManual());

        ataquePanel.add(new JLabel("Fila:"));
        ataquePanel.add(filaInput);
        ataquePanel.add(new JLabel("Columna:"));
        ataquePanel.add(colInput);
        ataquePanel.add(atacarBtn);

        ataquePanel.add(retiroInfo);

        inferior.add(ataquePanel, BorderLayout.NORTH);
        inferior.add(new JScrollPane(chat), BorderLayout.CENTER);

        return inferior;
    }

    //Barras de Vida
    private void construirBarras() {
        panelVidasJ1.removeAll();
        panelVidasJ2.removeAll();

        barrasJ1 = new JProgressBar[logicaJugador.barcos.size()];
        barrasJ2 = new JProgressBar[logicaEnemigo.barcos.size()];

        for (int i = 0; i < logicaJugador.barcos.size(); i++) {
            Barco b = logicaJugador.barcos.get(i);
            String id = logicaJugador.idPorIndice(i);

            JLabel lbl = new JLabel(nombreBonito(b.codigo) + " [" + id + "]");
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.BOLD, 13));
            panelVidasJ1.add(lbl);

            barrasJ1[i] = crearBarraVida(b);
            panelVidasJ1.add(barrasJ1[i]);
        }

        for (int i = 0; i < logicaEnemigo.barcos.size(); i++) {
            Barco b = logicaEnemigo.barcos.get(i);
            String id = logicaEnemigo.idPorIndice(i);

            JLabel lbl = new JLabel(nombreBonito(b.codigo) + " [" + id + "]");
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.BOLD, 13));
            panelVidasJ2.add(lbl);

            barrasJ2[i] = crearBarraVida(b);
            panelVidasJ2.add(barrasJ2[i]);
        }

        panelVidasJ1.revalidate();
        panelVidasJ2.revalidate();
        panelVidasJ1.repaint();
        panelVidasJ2.repaint();
    }

    private JProgressBar crearBarraVida(Barco b) {
        JProgressBar pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);
        pb.setValue(100);
        pb.setString("100% (" + b.vidas + "/" + b.tamaño + ")");
        return pb;
    }

    private void actualizarBarraDeBarco(Tablero_Logico t, JProgressBar[] barras, Barco b) {
        int idx = indexOfBarcoRef(t, b);
        if (idx < 0 || idx >= barras.length) return;

        int pct = (int) Math.round((b.vidas * 100.0) / b.tamaño);
        if (pct < 0) pct = 0;

        barras[idx].setValue(pct);
        if (b.estaHundido()) barras[idx].setString("HUNDIDO (0/" + b.tamaño + ")");
        else barras[idx].setString(pct + "% (" + b.vidas + "/" + b.tamaño + ")");
    }

    private int indexOfBarcoRef(Tablero_Logico t, Barco target) {
        for (int i = 0; i < t.barcos.size(); i++) {
            if (t.barcos.get(i) == target) return i;
        }
        return -1;
    }

    private String nombreBonito(String codigo) {
        return switch (codigo) {
            case "PA" -> "Portaaviones (PA)";
            case "AZ" -> "Acorazado (AZ)";
            case "SM" -> "Submarino (SM)";
            case "DT" -> "Destructor (DT)";
            default -> codigo;
        };
    }

    //Atacar Coordenadas
    private void atacarManual() {

        int f, c;

        try {
            f = Integer.parseInt(filaInput.getText());
            c = Integer.parseInt(colInput.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa números válidos.");
            return;
        }

        //Retiro con -1 -1
        if (f == -1 && c == -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas retirarte?", "Retiro", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Player perdedor = turnoDeJugador1 ? p1 : p2;
                Player ganador = turnoDeJugador1 ? p2 : p1;
                chat.append("🏳 " + perdedor.getUsername() + " se retiró. Gana " + ganador.getUsername() + " por retiro.\n");
                finalizarJuego(ganador, perdedor, true);
            }
            return;
        }

        if (f < 0 || c < 0 || f >= SIZE || c >= SIZE) {
            JOptionPane.showMessageDialog(this, "Coordenadas inválidas (0-7).");
            return;
        }

        //Turno de Jugadores
        if (turnoDeJugador1) {
            if (logicaEnemigo.yaAtacado(f, c)) {
                JOptionPane.showMessageDialog(this, "⚠ Ya atacaste esa posición.");
                return;
            }
            procesarAtaque(logicaEnemigo, tableroEnemigo, barrasJ2, p1, p2, f, c);
        } else {
            if (logicaJugador.yaAtacado(f, c)) {
                JOptionPane.showMessageDialog(this, "⚠ Ya atacaste esa posición.");
                return;
            }
            procesarAtaque(logicaJugador, tableroJugador, barrasJ1, p2, p1, f, c);
        }

        turnoDeJugador1 = !turnoDeJugador1;
        actualizarTurno();
    }

    //Ataque(Fallo,Acierto y Regenerar Tablero)
    private void procesarAtaque(Tablero_Logico t,
                                JButton[][] botones,
                                JProgressBar[] barras,
                                Player atacantePlayer,
                                Player defensorPlayer,
                                int f, int c) {

        String atacante = atacantePlayer.getUsername();
        String hitId = t.atacar(f, c);

        if (hitId == null) {
            chat.append("❌ " + atacante + " falló [" + f + "," + c + "]\n");
            botones[f][c].setIcon(null);
            botones[f][c].setText("F");
            botones[f][c].setForeground(Color.RED);
            return;
        }

        botones[f][c].setIcon(null);
        botones[f][c].setText("X");
        botones[f][c].setForeground(Color.BLACK);

        Barco barco = t.barcoDeId(hitId);
        if (barco == null) {
            chat.append("⚠ Error: impacto no asociado a barco.\n");
            return;
        }

        chat.append("💥 " + atacante + " impactó " + nombreBonito(barco.codigo) + " (" + hitId + ")\n");
        actualizarBarraDeBarco(t, barras, barco);

        if (barco.estaHundido()) {
            chat.append("🚢 SE HUNDIÓ " + nombreBonito(barco.codigo) + " (" + hitId + ")\n");
        }

        if (t.todosHundidos()) {
            chat.append("🏆 " + atacante + " hundió todos los barcos de " + defensorPlayer.getUsername() + "\n");
            finalizarJuego(atacantePlayer, defensorPlayer, false);
            return;
        }

        // Regenera posiciones
        t.regenerarPosiciones();

        boolean mostrar;
        if (modoArcade) {
            mostrar = false;
        } else if (botones == tableroJugador) {
            mostrar = true;
        } else {
            mostrar = modoTutorial;
        }

        redibujarTablero(botones, t, mostrar);
    }

    //Fin de Juego
    private void finalizarJuego(Player ganador, Player perdedor, boolean fueRetiro) {
        sistema.registrarResultadoPartida(ganador, perdedor, fueRetiro);
        JOptionPane.showMessageDialog(this, "🏆 El jugador " + ganador.getUsername() + " fue el triunfador.");
        if (ventanaJuego != null) ventanaJuego.dispose();
        if (menuPrincipal != null) menuPrincipal.volverAMenuPrincipal();
    }

    
    private void actualizarTurno() {
        if (turnoDeJugador1) {
            turnoLabel.setText("✅ TURNO DE " + nombreJugador);
            turnoLabel.setForeground(new Color(0, 180, 0));
        } else {
            turnoLabel.setText("✅ TURNO DE " + nombreEnemigo);
            turnoLabel.setForeground(new Color(255, 160, 0));
        }
    }

    //Redibujar Tablero
    private void redibujarTablero(JButton[][] botones, Tablero_Logico t, boolean mostrarBarcos) {

        // Primero dibuja marcas: X (Acierto), F (fallo) o ~ (agua)
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                botones[i][j].setIcon(null);
                botones[i][j].setForeground(Color.BLACK);

                byte st = t.getEstado(i, j);
                if (st == 2) {
                    botones[i][j].setText("X");
                } else if (st == 1) {
                    botones[i][j].setText("F");
                    botones[i][j].setForeground(Color.RED);
                } else {
                    botones[i][j].setText("~");
                }
            }
        }

        if (!mostrarBarcos) {
            revalidate();
            repaint();
            return;
        }

        // Colocar Barcos
        for (int idx = 0; idx < t.barcos.size(); idx++) {

            Barco b = t.barcos.get(idx);
            if (b.estaHundido()) continue;

            String id = t.idPorIndice(idx);

            int[] rr = new int[b.tamaño];
            int[] cc = new int[b.tamaño];
            int count = 0;

            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (id.equals(t.matriz[r][c])) {
                        if (count < b.tamaño) {
                            rr[count] = r;
                            cc[count] = c;
                            count++;
                        }
                    }
                }
            }

            if (count == 0) continue;

            boolean horizontal = true;
            if (count >= 2 && cc[0] == cc[1]) horizontal = false;

            for (int a = 0; a < count - 1; a++) {
                for (int d = 0; d < count - a - 1; d++) {
                    boolean swap = horizontal ? (cc[d] > cc[d + 1]) : (rr[d] > rr[d + 1]);
                    if (swap) {
                        int tr = rr[d]; rr[d] = rr[d + 1]; rr[d + 1] = tr;
                        int tc = cc[d]; cc[d] = cc[d + 1]; cc[d + 1] = tc;
                    }
                }
            }

            for (int k = 0; k < count; k++) {
                int r = rr[k];
                int c = cc[k];

                if (t.getEstado(r, c) != 0) continue;

                ImageIcon icon = escalarImagenSeguro(b.prefijo, k + 1);
                botones[r][c].setIcon(icon);
                botones[r][c].setText(icon == null ? "~" : "");
            }
        }

        revalidate();
        repaint();
    }

    //Imagenes
    private java.net.URL buscarImagen(String archivo) {
        String[] rutas = {
                "/Imagenes/" + archivo,
                "/imagenes/" + archivo,
                "/battle_ship/Imagenes/" + archivo,
                "/battle_ship/imagenes/" + archivo
        };

        for (int i = 0; i < rutas.length; i++) {
            java.net.URL url = getClass().getResource(rutas[i]);
            if (url != null) return url;
        }
        return null;
    }

    // Ajustar Imagen segun cuadros
    private ImageIcon escalarImagenSeguro(String prefijo, int parte) {

        String archivo = prefijo + parte + ".png";
        java.net.URL url = buscarImagen(archivo);

        if (url == null) {
            archivo = prefijo + parte + ".PNG";
            url = buscarImagen(archivo);
        }

        if (url == null) {
            System.out.println("No se encontró imagen: " + prefijo + parte);
            return null;
        }

        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage().getScaledInstance(CELL, CELL, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    //Panel transparente de Vidas
    private static class PanelTransparente extends JPanel {
        private final Color bg;

        public PanelTransparente(Color bg) {
            this.bg = bg;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g2.dispose();
        }
    }

    //Fondo
    private static class FondoPanel extends JPanel {
        private Image fondo;

        public FondoPanel(String ruta) {
            java.net.URL url = getClass().getResource(ruta);
            if (url != null) fondo = new ImageIcon(url).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (fondo != null) g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
