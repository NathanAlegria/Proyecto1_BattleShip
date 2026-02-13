/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

/**
 *
 * @author Nathan
 */
public class Player {

    private String username;
    private char[] password;
    private int puntos;

    // últimos 10 juegos del jugador LOGGED IN
    private String[] logs;
    private int logCount;

    public Player(String username, char[] password) {
        this.username = username;
        this.password = password.clone();
        this.puntos = 0;
        this.logs = new String[10];
        this.logCount = 0;
    }

    public String getUsername() { return username; }
    public int getPuntos() { return puntos; }

    // devuelve el arreglo completo (con nulls)
    public String[] getLogs() { return logs; }

    public void setUsername(String username) {
        if (username != null && !username.trim().isEmpty()) {
            this.username = username;
        }
    }

    public void setPassword(String pass) {
        if (pass != null && !pass.isEmpty()) {
            this.password = pass.toCharArray();
        }
    }

    public void addPuntos(int p) { puntos += p; }
    public void sumarPuntos(int p) { addPuntos(p); }

    // Más reciente primero
    public void addLog(String mensaje) {
        for (int i = Math.min(logCount, 9); i > 0; i--) {
            logs[i] = logs[i - 1];
        }
        logs[0] = mensaje;
        if (logCount < 10) logCount++;
    }

    public boolean verificarContrasena(char[] pass) {
        if (pass == null || password == null || pass.length != password.length) return false;
        for (int i = 0; i < pass.length; i++) {
            if (pass[i] != password[i]) return false;
        }
        return true;
    }
}

