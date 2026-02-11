package battle_ship;

import Logica.Battleship;
import Logica.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Menu_Principal extends JFrame {

    private Player jugadorActual;
    private Battleship sistema;
    private Menu menuReferencia;

    private CardLayout cards;
    private JPanel cardPanel;

    private JLabel lblUsuario;

    private Image backgroundImage, buttonImage, buttonHoverImage;

    //Panel de Fondo
    private class BackgroundPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    //Fondo de Botones
    private class ThemedButton extends JButton {

        private boolean hovered = false;

        public ThemedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("Impact", Font.PLAIN, 20));
            setForeground(Color.WHITE);
            setPreferredSize(new Dimension(300, 50));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Image img = hovered ? buttonHoverImage : buttonImage;
            if (img != null) {
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
            super.paintComponent(g);
        }
    }

    //Constructor
    public Menu_Principal(Battleship sistema, Menu menu, Player jugador) {
        this.sistema = sistema;
        this.menuReferencia = menu;
        this.jugadorActual = jugador;

        setTitle("Battle Ship - Menú Principal");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/Imagenes/fondom.jpg"));
            buttonImage = ImageIO.read(getClass().getResource("/Imagenes/botones.jpg"));
            buttonHoverImage = buttonImage;
        } catch (IOException e) {
            System.out.println("Error cargando imágenes");
        }

        BackgroundPanel fondo = new BackgroundPanel();
        fondo.setLayout(new GridBagLayout());

        cards = new CardLayout();
        cardPanel = new JPanel(cards);
        cardPanel.setOpaque(false);

        // MENÚS
        cardPanel.add(menuPrincipal(), "MAIN");
        cardPanel.add(panelConfiguracion(), "CONFIG");
        cardPanel.add(panelReportes(), "REPORTS");
        cardPanel.add(panelPerfil(), "PROFILE");
        cardPanel.add(panelVerMisDatos(), "PROFILE_VIEW");
        cardPanel.add(panelEditarMisDatos(), "PROFILE_EDIT");
        cardPanel.add(panelRanking(), "RANK");
        cardPanel.add(panelUltimosJuegos(), "LOGS");

        fondo.add(cardPanel);
        setContentPane(fondo);
    }

    //Menu Principal con Botones
    private JPanel menuPrincipal() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;

        lblUsuario = new JLabel("COMANDANTE: " + jugadorActual.getUsername());
        lblUsuario.setFont(new Font("Impact", Font.PLAIN, 26));
        lblUsuario.setForeground(Color.WHITE);
        c.gridy = 0;
        p.add(lblUsuario, c);

        c.gridy = 1;
        ThemedButton play = new ThemedButton("JUGAR BATTLESHIP");
        play.addActionListener(e -> seleccionarOponente());
        p.add(play, c);

        c.gridy = 2;
        ThemedButton config = new ThemedButton("CONFIGURACIÓN");
        config.addActionListener(e -> cards.show(cardPanel, "CONFIG"));
        p.add(config, c);

        c.gridy = 3;
        ThemedButton reports = new ThemedButton("REPORTES");
        reports.addActionListener(e -> cards.show(cardPanel, "REPORTS"));
        p.add(reports, c);

        c.gridy = 4;
        ThemedButton profile = new ThemedButton("MI PERFIL");
        profile.addActionListener(e -> cards.show(cardPanel, "PROFILE"));
        p.add(profile, c);

        c.gridy = 5;
        ThemedButton out = new ThemedButton("SALIR");
        out.addActionListener(e -> {
            sistema.cerrarSesion();
            dispose();
            menuReferencia.setVisible(true);
        });
        p.add(out, c);

        return p;
    }

    //SubPanel de Perfil
    private JPanel panelPerfil() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;

        JLabel title = new JLabel("MI PERFIL");
        title.setFont(new Font("Impact", Font.PLAIN, 30));
        title.setForeground(Color.CYAN);
        c.gridy = 0;
        p.add(title, c);

        c.gridy = 1;
        ThemedButton datos = new ThemedButton("VER MIS DATOS");
        datos.addActionListener(e -> cards.show(cardPanel, "PROFILE_VIEW"));
        p.add(datos, c);

        c.gridy = 2;
        ThemedButton modificar = new ThemedButton("MODIFICAR MIS DATOS");
        modificar.addActionListener(e -> cards.show(cardPanel, "PROFILE_EDIT"));
        p.add(modificar, c);

        c.gridy = 3;
        ThemedButton eliminar = new ThemedButton("ELIMINAR MI CUENTA");
        eliminar.addActionListener(e -> eliminarCuenta());
        p.add(eliminar, c);

        c.gridy = 4;
        ThemedButton back = new ThemedButton("VOLVER");
        back.addActionListener(e -> cards.show(cardPanel, "MAIN"));
        p.add(back, c);

        return p;
    }

    //SubMenu de Datos
    private JPanel panelVerMisDatos() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(new Color(0, 0, 0, 150)); // Fondo semitransparente
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;

        JLabel title = new JLabel("MIS DATOS");
        title.setFont(new Font("Impact", Font.PLAIN, 30));
        title.setForeground(Color.CYAN);
        c.gridy = 0;
        infoPanel.add(title, c);

        JLabel user = new JLabel("USUARIO: " + jugadorActual.getUsername());
        user.setFont(new Font("Arial", Font.BOLD, 18));
        user.setForeground(Color.WHITE);
        c.gridy = 1;
        infoPanel.add(user, c);

        JLabel puntos = new JLabel("PUNTOS: " + jugadorActual.getPuntos());
        puntos.setFont(new Font("Arial", Font.BOLD, 18));
        puntos.setForeground(Color.WHITE);
        c.gridy = 2;
        infoPanel.add(puntos, c);

        JLabel dificultad = new JLabel("DIFICULTAD: " + sistema.getDificultad());
        dificultad.setForeground(Color.WHITE);
        c.gridy = 3;
        infoPanel.add(dificultad, c);

        JLabel modo = new JLabel("MODO DE JUEGO: " + sistema.getModoJuego());
        modo.setForeground(Color.WHITE);
        c.gridy = 4;
        infoPanel.add(modo, c);

        ThemedButton back = new ThemedButton("VOLVER");
        back.addActionListener(e -> cards.show(cardPanel, "PROFILE"));
        c.gridy = 5;
        infoPanel.add(back, c);

        p.add(infoPanel);
        return p;
    }

    //SubMenu de Modificar Datos
    private JPanel panelEditarMisDatos() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(new Color(0, 0, 0, 150)); // Fondo semitransparente
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;

        JLabel title = new JLabel("MODIFICAR MIS DATOS");
        title.setFont(new Font("Impact", Font.PLAIN, 28));
        title.setForeground(Color.CYAN);
        c.gridy = 0;
        infoPanel.add(title, c);

        JTextField txtUser = new JTextField(jugadorActual.getUsername(), 15);
        JPasswordField txtPass = new JPasswordField(15);

        c.gridy = 1;
        JLabel labelUsername = new JLabel("Nuevo Username");
        labelUsername.setForeground(Color.WHITE);
        infoPanel.add(labelUsername, c);

        c.gridy = 2;
        infoPanel.add(txtUser, c);

        c.gridy = 3;
        JLabel labelPassword = new JLabel("Nuevo Password");
        labelPassword.setForeground(Color.WHITE);
        infoPanel.add(labelPassword, c);
        c.gridy = 4;
        infoPanel.add(txtPass, c);

        ThemedButton save = new ThemedButton("GUARDAR CAMBIOS");
        save.addActionListener(e -> {
            String nuevoUser = txtUser.getText();
            char[] nuevaPass = txtPass.getPassword();

            if (nuevoUser.isEmpty() || nuevaPass.length == 0) {
                JOptionPane.showMessageDialog(this, "Campos incompletos");
                return;
            }

            if (!passwordValida(nuevaPass)) {
                JOptionPane.showMessageDialog(this,
                        "La contraseña debe tener mínimo 5 caracteres,\n"
                        + "una letra mayúscula, un número y un símbolo.");
                return;
            }

            jugadorActual.setUsername(nuevoUser);
            jugadorActual.setPassword(new String(nuevaPass));
            lblUsuario.setText("COMANDANTE: " + jugadorActual.getUsername());

            cards.show(cardPanel, "PROFILE");
        });

        c.gridy = 5;
        infoPanel.add(save, c);

        ThemedButton back = new ThemedButton("CANCELAR");
        back.addActionListener(e -> cards.show(cardPanel, "PROFILE"));
        c.gridy = 6;
        infoPanel.add(back, c);

        p.add(infoPanel);
        return p;
    }

    //Opcion Eliminar Cuenta
    private void eliminarCuenta() {
        int op = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar su cuenta?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (op == JOptionPane.YES_OPTION) {
            sistema.eliminarJugador(jugadorActual);
            sistema.cerrarSesion();
            dispose();
            menuReferencia.setVisible(true);
        }
    }

    //Sub Panel de Configuracion de Juego
    private JPanel panelConfiguracion() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;

        JLabel title = new JLabel("CONFIGURACIÓN");
        title.setFont(new Font("Impact", Font.PLAIN, 30));
        title.setForeground(Color.CYAN);
        c.gridy = 0;
        p.add(title, c);

        c.gridy = 1;
        ThemedButton dificultad = new ThemedButton("DIFICULTAD");
        dificultad.addActionListener(e -> seleccionarDificultad());
        p.add(dificultad, c);

        c.gridy = 2;
        ThemedButton modo = new ThemedButton("MODO DE JUEGO");
        modo.addActionListener(e -> seleccionarModoJuego());
        p.add(modo, c);

        c.gridy = 3;
        ThemedButton back = new ThemedButton("VOLVER");
        back.addActionListener(e -> cards.show(cardPanel, "MAIN"));
        p.add(back, c);

        return p;
    }

    //Sub Panel de Reportes
    private JPanel panelReportes() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;

        JLabel title = new JLabel("REPORTES");
        title.setFont(new Font("Impact", Font.PLAIN, 30));
        title.setForeground(Color.CYAN);
        c.gridy = 0;
        p.add(title, c);

        c.gridy = 1;
        ThemedButton logs = new ThemedButton("ÚLTIMOS 10 JUEGOS");
        logs.addActionListener(e -> cards.show(cardPanel, "LOGS"));
        p.add(logs, c);

        c.gridy = 2;
        ThemedButton rank = new ThemedButton("RANKING DE JUGADORES");
        rank.addActionListener(e -> cards.show(cardPanel, "RANK"));
        p.add(rank, c);

        c.gridy = 3;
        ThemedButton back = new ThemedButton("VOLVER");
        back.addActionListener(e -> cards.show(cardPanel, "MAIN"));
        p.add(back, c);

        return p;
    }

    //Sub Panel de Ultimos 10 Juegos Jugados
    private JPanel panelUltimosJuegos() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel title = new JLabel("MIS ÚLTIMOS 10 JUEGOS", SwingConstants.CENTER);
        title.setFont(new Font("Impact", Font.PLAIN, 30));
        title.setForeground(Color.CYAN);
        p.add(title, BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();
        String[] logs = jugadorActual.getLogs();

        for (String log : logs) {
            if (log != null) {
                model.addElement(log);
            }
        }

        JList<String> list = new JList<>(model);
        list.setFont(new Font("Arial", Font.PLAIN, 16));
        p.add(new JScrollPane(list), BorderLayout.CENTER);

        ThemedButton back = new ThemedButton("VOLVER");
        back.addActionListener(e -> cards.show(cardPanel, "REPORTS"));
        p.add(back, BorderLayout.SOUTH);

        return p;
    }


    /* ===================== PANEL RANKING ===================== */
    private JPanel panelRanking() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel title = new JLabel("RANKING GLOBAL", SwingConstants.CENTER);
        title.setFont(new Font("Impact", Font.PLAIN, 30));
        title.setForeground(Color.CYAN);
        p.add(title, BorderLayout.NORTH);

        Player[] ranking = sistema.getRanking();

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"#", "Jugador", "Puntos"}, 0
        );

        for (int i = 0; i < ranking.length && ranking[i] != null; i++) {
            model.addRow(new Object[]{
                i + 1,
                ranking[i].getUsername(),
                ranking[i].getPuntos()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        ThemedButton back = new ThemedButton("VOLVER");
        back.addActionListener(e -> cards.show(cardPanel, "REPORTS"));
        p.add(back, BorderLayout.SOUTH);

        return p;
    }

    //Opcion Seleccionar Oponente
    private void seleccionarOponente() {

        Player[] jugadores = sistema.getRanking();
        int count = 0;

        for (Player p : jugadores) {
            if (p != null && !p.getUsername().equals(jugadorActual.getUsername())) {
                count++;
            }
        }

        if (count == 0) {
            JOptionPane.showMessageDialog(this, "No hay oponentes disponibles.");
            return;
        }

        String[] opciones = new String[count];
        int i = 0;
        for (Player p : jugadores) {
            if (p != null && !p.getUsername().equals(jugadorActual.getUsername())) {
                opciones[i++] = p.getUsername();
            }
        }

        String rival = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione oponente",
                "Radar de Batalla",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (rival == null) {
            return;
        }

        /* ================== ORDEN DE BARCOS ================== */
        Tablero_Logico tableroJugador = new Tablero_Logico();

        JFrame setupFrame = new JFrame(
                "Coloca tus barcos - Jugador: " + jugadorActual.getUsername()
        );

        Orden setupPanel = new Orden(tableroJugador);
        setupFrame.add(setupPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel();

        JButton startGame = new JButton("JUGAR");
        startGame.setFont(new Font("Impact", Font.BOLD, 18));
        startGame.setBackground(Color.BLUE);
        startGame.setForeground(Color.WHITE);

        startGame.addActionListener(e -> {
            setupFrame.dispose();

            /* ================== TABLEROS DE BATALLA ================== */
            Tablero_Logico tableroEnemigo = new Tablero_Logico();
            tableroEnemigo.barcos = tableroJugador.barcos; // mismos tipos
            tableroEnemigo.generarRandom();

            JFrame ventanaJuego = new JFrame(
                    "Battleship - " + jugadorActual.getUsername() + " vs " + rival
            );
            ventanaJuego.setSize(900, 750);
            ventanaJuego.setLocationRelativeTo(null);
            ventanaJuego.setResizable(false);
            ventanaJuego.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            Tablero batalla = new Tablero(
                    jugadorActual.getUsername(),
                    rival,
                    tableroJugador,
                    tableroEnemigo,
                    sistema.esTutorial()
            );

            ventanaJuego.add(batalla);
            ventanaJuego.setVisible(true);
        });

        bottom.add(startGame);
        setupFrame.add(bottom, BorderLayout.SOUTH);

        setupFrame.setSize(650, 700);
        setupFrame.setLocationRelativeTo(this);
        setupFrame.setResizable(false);
        setupFrame.setVisible(true);
    }

    private boolean passwordValida(char[] pass) {
        if (pass.length < 5) {
            return false;
        }

        boolean mayus = false;
        boolean numero = false;
        boolean simbolo = false;

        for (char c : pass) {
            if (Character.isUpperCase(c)) {
                mayus = true;
            } else if (Character.isDigit(c)) {
                numero = true;
            } else if (!Character.isLetterOrDigit(c)) {
                simbolo = true;
            }
        }

        return mayus && numero && simbolo;
    }

    private void seleccionarModoJuego() {
        String[] opciones = {"TUTORIAL", "ARCADE"};

        String sel = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el modo de juego",
                "Modo de Juego",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                sistema.getModoJuego()
        );

        if (sel != null) {
            sistema.setModoJuego(sel);
            JOptionPane.showMessageDialog(this,
                    "Modo de juego cambiado a: " + sel);
        }
    }

    private void seleccionarDificultad() {
        String[] opciones = {"EASY", "NORMAL", "EXPERT", "GENIUS"};

        String sel = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione dificultad",
                "Dificultad",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                sistema.getDificultad()
        );

        if (sel != null) {
            sistema.setDificultad(sel);
            JOptionPane.showMessageDialog(this,
                    "Dificultad cambiada a: " + sel);
        }
    }

}
