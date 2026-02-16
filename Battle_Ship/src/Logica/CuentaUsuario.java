/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

/**
 *
 * @author Nathan
 */
public abstract class CuentaUsuario {

    protected String username;
    protected char[] password;

    public CuentaUsuario(String username, char[] password) {
        this.username = username;
        this.password = (password == null) ? new char[0] : password.clone();
    }

    public String getUsername() { return username; }

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

    public boolean verificarContrasena(char[] pass) {
        if (pass == null || password == null || pass.length != password.length) return false;
        for (int i = 0; i < pass.length; i++) {
            if (pass[i] != password[i]) return false;
        }
        return true;
    }

    public abstract void addLog(String mensaje);
}

