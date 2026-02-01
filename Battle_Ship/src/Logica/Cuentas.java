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

public class Cuentas implements InterfaceCuentas {
    private static Cuentas instance;
    private ArrayList<Usuarios> listaUsuarios;
    private ArrayList<String> logsBatallas;

    private Cuentas() {
        listaUsuarios = new ArrayList<>();
        logsBatallas = new ArrayList<>();
    }

    public static Cuentas getInstance() {
        if (instance == null) instance = new Cuentas();
        return instance;
    }

    @Override
    public void registrarUsuario(String user, char[] pass) throws UsuarioException {
        if (user.isEmpty() || pass.length == 0) throw new UsuarioException("Datos incompletos.");
        if (buscarUsuario(user) != null) throw new UsuarioException("El comandante ya existe.");
        
        listaUsuarios.add(new Usuarios(user, pass));
    }

    @Override
    public void verificarCredenciales(String user, char[] pass) throws CredencialesException {
        Usuarios u = buscarUsuario(user);
        if (u == null || !u.getEstado()) throw new CredencialesException("Usuario no encontrado o inactivo.");
        if (!u.verificarPassword(pass)) throw new CredencialesException("Contraseña incorrecta.");
    }

    @Override
    public Usuarios buscarUsuario(String user) {
        for (Usuarios u : listaUsuarios) {
            if (u.getUsuario().equalsIgnoreCase(user)) return u;
        }
        return null;
    }

    @Override
    public boolean cambiarContrasena(String user, char[] actual, char[] nueva) {
        Usuarios u = buscarUsuario(user);
        if (u != null && u.verificarPassword(actual)) {
            u.setPassword(nueva);
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarUsuario(String user, char[] pass) {
        Usuarios u = buscarUsuario(user);
        if (u != null && u.verificarPassword(pass)) {
            u.setEstado(false); // Desactivación lógica
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Usuarios> getUsuariosRegistrados() {
        return listaUsuarios;
    }

    @Override
    public ArrayList<Usuarios> getRankingData() {
        ArrayList<Usuarios> activos = new ArrayList<>();
        for (Usuarios u : listaUsuarios) {
            if (u.getEstado()) activos.add(u);
        }
        return activos;
    }

    @Override
    public void agregarLog(String user, String evento) {
        logsBatallas.add(user + ": " + evento);
    }

    @Override
    public ArrayList<String> getLogsPorJugador(String user) {
        ArrayList<String> userLogs = new ArrayList<>();
        for (String log : logsBatallas) {
            if (log.startsWith(user)) userLogs.add(log);
        }
        return userLogs;
    }
}
