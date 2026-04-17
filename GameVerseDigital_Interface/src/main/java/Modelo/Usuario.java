/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;

public class Usuario extends Persona {
    private TipoCliente tipoCliente;
    private String codigoCliente;

    public Usuario() {}

    public Usuario(String identificacion, String nombre, String ciudadOrigen, int edad,
                   TipoCliente tipoCliente, String codigoCliente) {
        super(identificacion, nombre, ciudadOrigen, edad);
        this.tipoCliente = tipoCliente;
        this.codigoCliente = codigoCliente;
    }

    public void crearCliente() {
        System.out.println("Cliente creado: " + this.nombre);
    }

    public Usuario editarCliente(String codigo) {
        System.out.println("Editando cliente con código: " + codigo);
        return this;
    }

    public Usuario buscarCliente(String codigo) {
        System.out.println("Buscando cliente con código: " + codigo);
        return null;
    }

    public Usuario eliminarCliente(String codigo) {
        System.out.println("Eliminando cliente con código: " + codigo);
        return null;
    }

    public ArrayList<Usuario> listarClientes() {
        return new ArrayList<>();
    }

    // Getters y Setters
    public TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(TipoCliente tipoCliente) { this.tipoCliente = tipoCliente; }

    public String getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(String codigoCliente) { this.codigoCliente = codigoCliente; }
}
