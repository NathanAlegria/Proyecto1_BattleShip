/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

/**
 *
 * @author Nathan
 */

public class Battleship implements ISistemaJuego {

    private Player[] jugadores;
    private int totalJugadores;
    private Player jugadorActual;

    //Enum
    private Dificultad dificultad = Dificultad.NORMAL;
    private ModoJuego modoJuego = ModoJuego.TUTORIAL;

    private static Battleship instancia;

    private Battleship() {
        jugadores = new Player[20];
        totalJugadores = 0;
        jugadorActual = null;
    }

    public static Battleship getInstance() {
        if (instancia == null) instancia = new Battleship();
        return instancia;
    }

    //Historial
    private String[] historial = new String[10];
    private int partidas = 0;

    public String[] getHistorial() {
        String[] h = new String[partidas];
        System.arraycopy(historial, 0, h, 0, partidas);
        return h;
    }

    private void pushHistorial(String txt) {
        //Recursividad
        desplazarHistorialRec(historial.length - 1);

        historial[0] = txt;
        if (partidas < 10) partidas++;
    }

    // Recursividad
    private void desplazarHistorialRec(int pos) {
        if (pos <= 0) return;                 // caso base
        historial[pos] = historial[pos - 1];  // paso
        desplazarHistorialRec(pos - 1);       // llamada recursiva
    }

    //Resultado Partidas
    public void registrarResultadoPartida(Player ganador, Player perdedor, boolean fueRetiro) {
        if (ganador == null || perdedor == null) return;

        ganador.sumarPuntos(3);

        String txt;
        if (fueRetiro) {
            txt = ganador.getUsername() + " ganó porque " + perdedor.getUsername() + " huyó (retiro).";
        } else {
            txt = ganador.getUsername() + " hundió todos los barcos de " + perdedor.getUsername()
                    + " en modo " + getDificultad() + ".";
        }

        ganador.addLog(txt);
        perdedor.addLog(txt);

        pushHistorial("Partida " + ganador.getUsername() + " vs " + perdedor.getUsername()
                + " | Ganador: " + ganador.getUsername()
                + (fueRetiro ? " (RETIRO)" : " (HUNDIÓ TODO)"));
    }

    //Jugadores
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
            if (jugadores[i].getUsername().equals(user)) return jugadores[i];
        }
        return null;
    }

    public boolean verificarCredenciales(String user, char[] pass) {
        Player p = buscarJugador(user);
        return p != null && p.verificarContrasena(pass);
    }

    public void setJugadorActual(Player p) { jugadorActual = p; }

    public Player getJugadorActual() { return jugadorActual; }

    public void cerrarSesion() { jugadorActual = null; }

    public boolean eliminarJugador(Player jugador) {
        for (int i = 0; i < totalJugadores; i++) {
            if (jugadores[i] == jugador) {
                for (int j = i; j < totalJugadores - 1; j++) {
                    jugadores[j] = jugadores[j + 1];
                }
                jugadores[--totalJugadores] = null;
                return true;
            }
        }
        return false;
    }

    //Configuracion
    public String getDificultad() { return dificultad.name(); }

    public void setDificultad(String dificultad) {
        this.dificultad = Dificultad.fromString(dificultad);
    }

    public String getModoJuego() { return modoJuego.name(); }

    public void setModoJuego(String modoJuego) {
        this.modoJuego = ModoJuego.fromString(modoJuego);
    }

    public boolean esTutorial() {
        return modoJuego == ModoJuego.TUTORIAL;
    }

    public int barcosPorDificultad() {
        switch (dificultad) {
            case EASY:   return 5;
            case EXPERT: return 2;
            case GENIUS: return 1;
            default:     return 4; // NORMAL
        }
    }

    //Ranking
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

