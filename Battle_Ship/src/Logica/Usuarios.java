/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.Arrays;
import java.util.Calendar;

/**
 *
 * @author Nathan
 */
public class Usuarios {

    private String usuario;
    private char[] password;
    private int puntos;
    private String fechaCreacion;
    private boolean estado;

    public Usuarios(String usuario, char[] password) {
        this.usuario = usuario;
        this.password = Arrays.copyOf(password, password.length);
        this.puntos = 0;
        this.estado = true;

        // Generar fecha actual formateada
        Calendar c = Calendar.getInstance();
        this.fechaCreacion = c.get(Calendar.DAY_OF_MONTH) + "/"
                + (c.get(Calendar.MONTH) + 1) + "/"
                + c.get(Calendar.YEAR);
    }

    // Métodos de Seguridad
    public boolean verificarPassword(char[] intento) {
        return Arrays.equals(this.password, intento);
    }

    public void setPassword(char[] nuevaPassword) {
        limpiarContrasena(this.password); // Borrar la anterior
        this.password = Arrays.copyOf(nuevaPassword, nuevaPassword.length);
    }

    public static void limpiarContrasena(char[] p) {
        if (p != null) {
            Arrays.fill(p, '0');
        }
    }

    // Getters
    public String getUsuario() {
        return usuario;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public boolean getEstado() {
        return estado;
    }

    // Setters
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void sumarPuntos(int cantidad) {
        this.puntos += cantidad;
    }
}
