/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battle_ship;

import Logica.Battleship;
import Logica.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Arrays;

/**
 *
 * @author Nathan
 */
public class Menu extends JFrame {

    private Image backgroundImage;
    private Image buttonImage;
    private Image buttonHoverImage;

    private JTextField loginUserField;
    private JPasswordField loginPassField;
    private JTextField registerUserField;
    private JPasswordField registerPassField;
    private JPasswordField registerConfPassField;

    private Battleship sistema;

    public Menu() {
        setTitle("Battle Ship - Inicio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        sistema = Battleship.getInstance();

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/Imagenes/fondo.jpg"));
            buttonImage = ImageIO.read(getClass().getResource("/Imagenes/botones.jpg"));
            buttonHoverImage = buttonImage;
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error cargando imágenes, se usaran colores base.");
        }

        setContentPane(buildUI());
        setVisible(true);
    }

    /* ===================== DISEÑO VISUAL MEJORADO ===================== */
    private class BackgroundPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private class ThemedButton extends JButton {

        private boolean hovered = false;

        public ThemedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFont(new Font("Impact", Font.PLAIN, 26));
            setForeground(Color.WHITE);
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
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Image img = hovered ? buttonHoverImage : buttonImage;
            if (img != null) {
                g2.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    /**
     * EFECTO NEÓN: Dibuja el borde azul en múltiples direcciones para que sea
     * muy notable.
     */
    private JLabel createShadowLabel(String text, int fontSize, Color borderBlue) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Dibujar el "resplandor" azul cyan en 4 direcciones
                g2.setColor(borderBlue);
                g2.drawString(getText(), 2, fontSize + 2);
                g2.drawString(getText(), -2, fontSize - 2);
                g2.drawString(getText(), 2, fontSize - 2);
                g2.drawString(getText(), -2, fontSize + 2);

                // Texto principal en GRIS CARBÓN OSCURO
                g2.setColor(new Color(40, 40, 40));
                g2.drawString(getText(), 0, fontSize);

                g2.dispose();
            }
        };
        label.setFont(new Font("Impact", Font.BOLD, fontSize));
        label.setPreferredSize(new Dimension(700, fontSize + 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /* ===================== CONSTRUCCIÓN DE PANELES ===================== */
    private JPanel buildUI() {
        BackgroundPanel bg = new BackgroundPanel();
        bg.setLayout(new GridBagLayout());

        CardLayout cards = new CardLayout();
        JPanel panelContenedor = new JPanel(cards);
        panelContenedor.setOpaque(false);
        panelContenedor.setPreferredSize(new Dimension(500, 400));

        panelContenedor.add(buildMenuInicio(cards, panelContenedor), "MENU");
        panelContenedor.add(buildLoginPanel(cards, panelContenedor), "LOGIN");
        panelContenedor.add(buildRegisterPanel(cards, panelContenedor), "REGISTER");

        GridBagConstraints gbc = new GridBagConstraints();

        // TÍTULO: Ajustado (top 280) para quedar cerca de los botones y centrado
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(280, 0, 5, 0);

        JLabel title = createShadowLabel("BATTLESHIP", 110, new Color(0, 225, 255));
        bg.add(title, gbc);

        // CONTENEDOR DE CARTAS (Login, Registro, Botones principales)
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        bg.add(panelContenedor, gbc);

        return bg;
    }

    private JPanel buildMenuInicio(CardLayout cards, JPanel container) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        ThemedButton btnLogin = new ThemedButton("LOGIN");
        ThemedButton btnCreate = new ThemedButton("CREAR PLAYER");
        ThemedButton btnExit = new ThemedButton("SALIR");

        for (JButton b : new JButton[]{btnLogin, btnCreate, btnExit}) {
            b.setMaximumSize(new Dimension(380, 70));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        btnLogin.addActionListener(e -> cards.show(container, "LOGIN"));
        btnCreate.addActionListener(e -> cards.show(container, "REGISTER"));
        btnExit.addActionListener(e -> System.exit(0));

        p.add(Box.createVerticalGlue());
        p.add(btnLogin);
        p.add(Box.createVerticalStrut(15));
        p.add(btnCreate);
        p.add(Box.createVerticalStrut(15));
        p.add(btnExit);
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JPanel buildLoginPanel(CardLayout cards, JPanel container) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;

        c.gridy = 0;
        p.add(createShadowLabel("LOGIN", 40, new Color(0, 225, 255)), c);
        c.gridy++;
        p.add(createShadowLabel("Username:", 20, Color.WHITE), c);
        c.gridy++;
        loginUserField = new JTextField(15);
        p.add(loginUserField, c);
        c.gridy++;
        p.add(createShadowLabel("Password:", 20, Color.WHITE), c);
        c.gridy++;
        loginPassField = new JPasswordField(15);
        p.add(loginPassField, c);

        c.gridwidth = 1;
        c.gridy++;
        ThemedButton btnEntrar = new ThemedButton("Entrar");
        ThemedButton btnVolver = new ThemedButton("Volver");

        btnEntrar.addActionListener(e -> ejecutarLogin());
        btnVolver.addActionListener(e -> cards.show(container, "MENU"));

        c.gridx = 0;
        p.add(btnEntrar, c);
        c.gridx = 1;
        p.add(btnVolver, c);
        return p;
    }

    private JPanel buildRegisterPanel(CardLayout cards, JPanel container) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 5, 2, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;

        c.gridy = 0;
        p.add(createShadowLabel("CREAR PLAYER", 40, new Color(0, 225, 255)), c);
        c.gridy++;
        p.add(createShadowLabel("Nuevo Username:", 18, Color.WHITE), c);
        c.gridy++;
        registerUserField = new JTextField(15);
        p.add(registerUserField, c);
        c.gridy++;
        p.add(createShadowLabel("Password:", 18, Color.WHITE), c);
        c.gridy++;
        registerPassField = new JPasswordField(15);
        p.add(registerPassField, c);
        c.gridy++;
        p.add(createShadowLabel("Confirmar:", 18, Color.WHITE), c);
        c.gridy++;
        registerConfPassField = new JPasswordField(15);
        p.add(registerConfPassField, c);

        c.gridwidth = 1;
        c.gridy++;
        ThemedButton btnReg = new ThemedButton("Registrar");
        ThemedButton btnVol = new ThemedButton("Volver");

        btnReg.addActionListener(e -> ejecutarRegistro());
        btnVol.addActionListener(e -> cards.show(container, "MENU"));

        c.gridx = 0;
        p.add(btnReg, c);
        c.gridx = 1;
        p.add(btnVol, c);
        return p;
    }

    /* ===================== LÓGICA DE TRANSICIÓN ===================== */
    private void ejecutarLogin() {
        String user = loginUserField.getText();
        char[] pass = loginPassField.getPassword();
        if (sistema.verificarCredenciales(user, pass)) {
            sistema.setJugadorActual(sistema.buscarJugador(user));
            abrirMenuPrincipal();
        } else {
            JOptionPane.showMessageDialog(this, "Error: Username o Password incorrectos.", "Error de Login", JOptionPane.ERROR_MESSAGE);
            loginUserField.setText("");
            loginPassField.setText("");
        }
    }

    private void ejecutarRegistro() {
        String user = registerUserField.getText();
        char[] pass = registerPassField.getPassword();
        char[] conf = registerConfPassField.getPassword();

        if (!Arrays.equals(pass, conf)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
            return;
        }

        if (!passwordValida(pass)) { 
            JOptionPane.showMessageDialog(this,
                    "La contraseña debe tener mínimo 5 caracteres,\n"
                    + "una letra mayúscula, un número y un símbolo.");
            return;
        }

        if (sistema.registrarJugador(user, pass)) {
            sistema.setJugadorActual(sistema.buscarJugador(user));
            abrirMenuPrincipal();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error: El Username ya existe o está vacío.");
        }
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

    private void abrirMenuPrincipal() {
        this.setVisible(false);
        new Menu_Principal(sistema, this, sistema.getJugadorActual()).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu());
    }
}
  