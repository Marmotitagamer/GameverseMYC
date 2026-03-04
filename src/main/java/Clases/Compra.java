/*
 * GameVerse Digital - Clase Compra
 */
package Clases;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa una compra: relaciona un Usuario con un Juego.
 */
public class Compra {
    private int    id;
    private Usuario usuario;
    private Juego   juego;
    private int     cantidad;
    private double  total;
    private String  fecha;

    public Compra(int id, Usuario usuario, Juego juego, int cantidad) {
        this.id       = id;
        this.usuario  = usuario;
        this.juego    = juego;
        this.cantidad = cantidad;
        this.total    = juego.getPrecio() * cantidad;
        this.fecha    = LocalDateTime.now()
                          .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public int     getId()       { return id; }
    public Usuario getUsuario()  { return usuario; }
    public Juego   getJuego()    { return juego; }
    public int     getCantidad() { return cantidad; }
    public double  getTotal()    { return total; }
    public String  getFecha()    { return fecha; }
}
