/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

/**
 *
 * @author Nathan
 */
public class Player extends CuentaUsuario {

    private int puntos;

    // últimos 10 juegos del jugador
    private String[] logs; 
    private int logCount;

    public Player(String username, char[] password) {
        super(username, password);
        this.puntos = 0;
        this.logs = new String[10];
        this.logCount = 0;
    }

    public int getPuntos() { return puntos; }

    public String[] getLogs() { return logs; }

    public void addPuntos(int p) { puntos += p; }
    public void sumarPuntos(int p) { addPuntos(p); }

    @Override
    public void addLog(String mensaje) {
        for (int i = Math.min(logCount, 9); i > 0; i--) {
            logs[i] = logs[i - 1];
        }
        logs[0] = mensaje;
        if (logCount < 10) logCount++;
    }
}

