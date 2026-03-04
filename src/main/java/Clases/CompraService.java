/*
 * GameVerse Digital - Servicio de Compras
 */
package Clases;

import java.util.ArrayList;

/**
 * Gestiona las compras en memoria, relacionando Usuario y Juego.
 */
public class CompraService {

    private ArrayList<Compra> lista = new ArrayList<>();
    private int contadorId = 1;

    /**
     * Registra una compra. Retorna mensaje de resultado.
     */
    public String registrar(UsuarioService us, JuegoService js,
                            String codigoCliente, int idJuego, int cantidad) {
        Usuario usuario = us.buscarPorCodigo(codigoCliente);
        if (usuario == null) return "Usuario no encontrado.";

        Juego juego = js.buscarPorId(idJuego);
        if (juego == null) return "Juego no encontrado.";

        if (juego.getStock() < cantidad)
            return "Stock insuficiente. Disponible: " + juego.getStock();

        juego.setStock(juego.getStock() - cantidad);
        lista.add(new Compra(contadorId++, usuario, juego, cantidad));
        return "OK";
    }

    /** Cancela una compra y restaura el stock */
    public boolean cancelar(int id, JuegoService js) {
        Compra c = buscarPorId(id);
        if (c == null) return false;
        Juego j = js.buscarPorId(c.getJuego().getId());
        if (j != null) j.setStock(j.getStock() + c.getCantidad());
        lista.remove(c);
        return true;
    }

    public Compra buscarPorId(int id) {
        for (Compra c : lista) if (c.getId() == id) return c;
        return null;
    }

    public ArrayList<Compra> buscarPorUsuario(String codigoCliente) {
        ArrayList<Compra> resultado = new ArrayList<>();
        for (Compra c : lista)
            if (c.getUsuario().getCodigoCliente().equalsIgnoreCase(codigoCliente))
                resultado.add(c);
        return resultado;
    }

    public ArrayList<Compra> getLista() { return lista; }
}
