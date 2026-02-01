/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

/**
 *
 * @author Nathan
 */

public class Battleship {

    private Player[] jugadores;
    private int totalJugadores;
    private Player jugadorActual;

    private String dificultad = "NORMAL";
    private String modoJuego = "TUTORIAL";

    private static Battleship instancia;

    private Battleship() {
        jugadores = new Player[20];
        totalJugadores = 0;
        jugadorActual = null;
    }

    public static Battleship getInstance() {
        if (instancia == null) {
            instancia = new Battleship();
        }
        return instancia;
    }

    /* ===================== USUARIOS ===================== */

    public boolean registrarJugador(String user, char[] pass) {
        if (user == null || user.trim().isEmpty()) return false;

        if (buscarJugador(user) != null) return false;

        if (totalJugadores < jugadores.length) {
            jugadores[totalJugadores++] = new Player(user, pass);
            return true;
        }
        return false;
    }

    public Player buscarJugador(String user) {
        for (int i = 0; i < totalJugadores; i++) {
            if (jugadores[i].getUsername().equals(user)) {
                return jugadores[i];
            }
        }
        return null;
    }

    public boolean verificarCredenciales(String user, char[] pass) {
        Player p = buscarJugador(user);
        return p != null && p.verificarContrasena(pass);
    }

    public void setJugadorActual(Player p) {
        this.jugadorActual = p;
    }

    public Player getJugadorActual() {
        return jugadorActual;
    }

    public void cerrarSesion() {
        jugadorActual = null;
    }

    /* ===================== ELIMINAR CUENTA ===================== */
    public boolean eliminarJugador(Player jugador) {
        if (jugador == null) return false;

        for (int i = 0; i < totalJugadores; i++) {
            if (jugadores[i] == jugador) {
                // Desplazar arreglo
                for (int j = i; j < totalJugadores - 1; j++) {
                    jugadores[j] = jugadores[j + 1];
                }
                jugadores[--totalJugadores] = null;
                return true;
            }
        }
        return false;
    }

    /* ===================== CONFIG ===================== */

    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }

    public String getModoJuego() { return modoJuego; }
    public void setModoJuego(String modoJuego) { this.modoJuego = modoJuego; }

    /* ===================== RANKING ===================== */

    public Player[] getRanking() {
        Player[] ranking = new Player[totalJugadores];
        System.arraycopy(jugadores, 0, ranking, 0, totalJugadores);

        for (int i = 0; i < ranking.length - 1; i++) {
            for (int j = 0; j < ranking.length - i - 1; j++) {
                if (ranking[j].getPuntos() < ranking[j + 1].getPuntos()) {
                    Player tmp = ranking[j];
                    ranking[j] = ranking[j + 1];
                    ranking[j + 1] = tmp;
                }
            }
        }
        return ranking;
    }
}
