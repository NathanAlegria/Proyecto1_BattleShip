/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battle_ship;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
/**
 *
 * @author Nathan
 */

public class PanelBarcos extends JPanel {

    private HashMap<String, JProgressBar> barras = new HashMap<>();

    public PanelBarcos(java.util.List<Barco> barcos, String titulo) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(titulo));

        for (Barco b : barcos) {
            JLabel lbl = new JLabel(b.codigo);
            JProgressBar bar = new JProgressBar(0, b.vidas);
            bar.setValue(b.vidas);
            bar.setStringPainted(true);

            barras.put(b.prefijo, bar);

            add(lbl);
            add(bar);
            add(Box.createVerticalStrut(5));
        }
    }

    public void actualizar(Barco b) {
        JProgressBar bar = barras.get(b.prefijo);
        if (bar != null) {
            bar.setValue(b.vidas);
            if (b.estaHundido()) {
                bar.setString("HUNDIDO");
                bar.setForeground(Color.GRAY);
            }
        }
    }
}

