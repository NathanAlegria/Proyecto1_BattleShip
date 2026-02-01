/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.ArrayList;

/**
 *
 * @author Nathan
 */
public interface InterfaceCuentas {

    void registrarUsuario(String user, char[] pass) throws UsuarioException;

    void verificarCredenciales(String user, char[] pass) throws CredencialesException;

    Usuarios buscarUsuario(String user);

    boolean cambiarContrasena(String user, char[] actual, char[] nueva);

    boolean eliminarUsuario(String user, char[] pass);

    ArrayList<Usuarios> getUsuariosRegistrados();

    ArrayList<Usuarios> getRankingData();

    ArrayList<String> getLogsPorJugador(String user);

    void agregarLog(String user, String evento);
}
