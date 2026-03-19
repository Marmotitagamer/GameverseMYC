/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Mflas
 */
public class Persona {
    protected String identificacion;
    protected String nombre;
    protected String ciudadOrigen;
    protected int edad;

    public Persona() {}

    public Persona(String identificacion, String nombre, String ciudadOrigen, int edad) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.ciudadOrigen = ciudadOrigen;
        this.edad = edad;
    }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCiudadOrigen() { return ciudadOrigen; }
    public void setCiudadOrigen(String ciudadOrigen) { this.ciudadOrigen = ciudadOrigen; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    @Override
    public String toString() {
        return "Persona{id=" + identificacion + ", nombre=" + nombre + "}";
    }
}
