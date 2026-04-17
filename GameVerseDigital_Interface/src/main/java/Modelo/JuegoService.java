/*
 * GameVerse Digital - Servicio de Juegos
 */
package Modelo;

import java.util.ArrayList;

/**
 * Gestiona el catálogo de juegos en memoria.
 */
public class JuegoService {

    private ArrayList<Juego> lista = new ArrayList<>();
    private int contadorId = 1;

    public JuegoService() {
        agregar("The Legend of Zelda: Breath of the Wild", "Aventura",    228_641, 10);
        agregar("FIFA 25",                                  "Deportes",    266_490, 15);
        agregar("Minecraft",                                "Sandbox",     114_132, 50);
        agregar("Call of Duty: Black Ops 6",                "Accion",      247_386,  8);
        agregar("Elden Ring",                               "RPG",         228_641, 12);
        agregar("Resident Evil 4 Remake",                   "Terror",      190_293,  6);
        agregar("Among Us",                                 "Multijugador",  18_997, 100);
        agregar("Hollow Knight",           "Metroidvania", 57_111, 20);
        agregar("Hollow Knight: Silksong", "Metroidvania", 76_122, 15);
        agregar("Grand Theft Auto V",      "Accion",       114_132, 10);
        agregar("R.E.P.O.",                "Terror",        38_052, 30);
    }

    /** CREATE */
    public void agregar(String titulo, String genero, int precio, int stock) {
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
