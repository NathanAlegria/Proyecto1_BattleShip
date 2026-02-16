/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package Logica;

/**
 *
 * @author Nathan
 */
public enum Dificultad {
    EASY, NORMAL, EXPERT, GENIUS;

    public static Dificultad fromString(String s) {
        if (s == null) return NORMAL;
        try { return Dificultad.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException ex) { return NORMAL; }
    }
}

