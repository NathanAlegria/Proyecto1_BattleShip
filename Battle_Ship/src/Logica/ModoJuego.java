/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Logica;

/**
 *
 * @author Nathan
 */
public enum ModoJuego {
    TUTORIAL, ARCADE;

    public static ModoJuego fromString(String s) {
        if (s == null) return TUTORIAL;
        try { return ModoJuego.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException ex) { return TUTORIAL; }
    }
}
