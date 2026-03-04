/*
 * GameVerse Digital - Servicio de Juegos
 */
package Clases;

import java.util.ArrayList;

/**
 * Gestiona el catálogo de juegos en memoria.
 */
public class JuegoService {

    private ArrayList<Juego> lista = new ArrayList<>();
    private int contadorId = 1;

    public JuegoService() {
        agregar("The Legend of Zelda", "Aventura",  59.99, 10);
        agregar("FIFA 25",             "Deportes",  69.99, 15);
        agregar("Minecraft",           "Sandbox",   29.99, 50);
        agregar("Call of Duty",        "Accion",    64.99,  8);
        agregar("Elden Ring",          "RPG",       59.99, 12);
        agregar("Resident Evil 4",     "Terror",    49.99,  6);
        agregar("Among Us",            "Multijugador", 4.99, 100);
    }

    /** CREATE */
    public void agregar(String titulo, String genero, double precio, int stock) {
        lista.add(new Juego(contadorId++, titulo, genero, precio, stock));
    }

    /** READ - todos */
    public ArrayList<Juego> getLista() { return lista; }

    /** READ - por ID */
    public Juego buscarPorId(int id) {
        for (Juego j : lista) if (j.getId() == id) return j;
        return null;
    }

    /** READ - por título (parcial) */
    public ArrayList<Juego> buscarPorTitulo(String titulo) {
        ArrayList<Juego> resultado = new ArrayList<>();
        for (Juego j : lista)
            if (j.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                resultado.add(j);
        return resultado;
    }

    /** READ - por género */
    public ArrayList<Juego> buscarPorGenero(String genero) {
        ArrayList<Juego> resultado = new ArrayList<>();
        for (Juego j : lista)
            if (j.getGenero().equalsIgnoreCase(genero))
                resultado.add(j);
        return resultado;
    }

    /** UPDATE */
    public boolean actualizar(int id, String titulo, String genero,
                               double precio, int stock) {
        Juego j = buscarPorId(id);
        if (j == null) return false;
        j.setTitulo(titulo); j.setGenero(genero);
        j.setPrecio(precio); j.setStock(stock);
        return true;
    }

    /** DELETE */
    public boolean eliminar(int id) {
        Juego j = buscarPorId(id);
        if (j == null) return false;
        lista.remove(j);
        return true;
    }
}
