/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

/**
 *
 * @author Nathan
 */
public interface ISistemaJuego {

    boolean registrarJugador(String user, char[] pass);
    boolean verificarCredenciales(String user, char[] pass);
    Player buscarJugador(String user);

    void registrarResultadoPartida(Player ganador, Player perdedor, boolean fueRetiro);

    String getDificultad();
    void setDificultad(String dificultad);

    String getModoJuego();
    void setModoJuego(String modo);

    boolean esTutorial();
    int barcosPorDificultad();

    Player[] getRanking();

    void setJugadorActual(Player p);
    Player getJugadorActual();
    void cerrarSesion();
    boolean eliminarJugador(Player jugador);
}
