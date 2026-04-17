package Modelo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * GestionarCompras
 * Clase de modelo que encapsula toda la lógica del carrito de compras.
 * El JSP (compras.jsp) y el servlet (ServletCompras) delegan aquí
 * toda la lógica de negocio, manteniendo los JSP limpios de código Java.
 */
public class GestionarCompras {

    // ------------------------------------------------------------------
    // Catálogo estático de productos disponibles
    // ------------------------------------------------------------------
    private static final LinkedList<Producto> CATALOGO = new LinkedList<>();

    static {
        CATALOGO.add(new Producto("G001", "Elden Ring",               15, 59000, "images/EldenRing.png"));
        CATALOGO.add(new Producto("G002", "God of War Ragnarök",       10, 49000, "images/GOWRagnarok.jpg"));
        CATALOGO.add(new Producto("G003", "Cyberpunk 2077",            20, 39000, "images/Cyberpunk.jpeg"));
        CATALOGO.add(new Producto("G004", "Zelda: Tears of the Kingdom", 8, 69000, "images/Zelda.jpg"));
        CATALOGO.add(new Producto("G005", "Hollow Knight",             50, 14000, "images/images.jpeg"));
        CATALOGO.add(new Producto("G006", "Red Dead Redemption 2",     12, 44000, "images/RDR2.jpeg"));
    }

    // ------------------------------------------------------------------
    // Consulta del catálogo
    // ------------------------------------------------------------------

    /** Devuelve el catálogo completo de juegos disponibles. */
    public LinkedList<Producto> getCatalogo() {
        return CATALOGO;
    }

    /** Busca un producto en el catálogo por su código. */
    public Producto buscarEnCatalogo(String codigo) {
        for (Producto p : CATALOGO) {
            if (p.getCodigo().equals(codigo)) return p;
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Operaciones sobre el carrito
    // ------------------------------------------------------------------

    /**
     * Agrega un producto al carrito (por código).
     * Si ya está en el carrito, incrementa su cantidad en 1
     * sin superar el stock disponible en el catálogo.
     *
     * @param carrito lista actual del carrito (se modifica en lugar)
     * @param codigo  código del producto a agregar
     * @return mensaje descriptivo del resultado
     */
    public String agregarAlCarrito(List<Producto> carrito, String codigo) {
        Producto original = buscarEnCatalogo(codigo);
        if (original == null) return "Producto no encontrado en el catálogo.";

        // Buscar si ya existe en el carrito
        for (Producto c : carrito) {
            if (c.getCodigo().equals(codigo)) {
                if (c.getCantidad() < original.getCantidad()) {
                    c.setCantidad(c.getCantidad() + 1);
                    return "Cantidad actualizada: " + c.getNombre();
                } else {
                    return "Stock máximo alcanzado para: " + c.getNombre();
                }
            }
        }

        // No está en el carrito → agregar con cantidad 1
        carrito.add(new Producto(
                original.getCodigo(), original.getNombre(),
                1, original.getPrecio(), original.getImagen()
        ));
        return original.getNombre() + " agregado al carrito.";
    }

    /**
     * Reduce en 1 la cantidad de un producto en el carrito.
     * Si la cantidad llega a 0 lo elimina.
     *
     * @param carrito lista actual del carrito
     * @param codigo  código del producto
     * @return mensaje descriptivo del resultado
     */
    public String reducirCantidad(List<Producto> carrito, String codigo) {
        for (Producto c : carrito) {
            if (c.getCodigo().equals(codigo)) {
                if (c.getCantidad() > 1) {
                    c.setCantidad(c.getCantidad() - 1);
                    return "Cantidad reducida: " + c.getNombre();
                } else {
                    carrito.remove(c);
                    return c.getNombre() + " eliminado del carrito.";
                }
            }
        }
        return "Producto no encontrado en el carrito.";
    }

    /**
     * Elimina por completo un producto del carrito.
     *
     * @param carrito lista actual del carrito
     * @param codigo  código del producto
     */
    public void eliminarDelCarrito(List<Producto> carrito, String codigo) {
        carrito.removeIf(p -> p.getCodigo().equals(codigo));
    }

    /**
     * Vacía completamente el carrito.
     *
     * @param carrito lista actual del carrito
     */
    public void vaciarCarrito(List<Producto> carrito) {
        carrito.clear();
    }

    /**
     * Calcula el total monetario del carrito.
     *
     * @param carrito lista actual del carrito
     * @return suma de precio × cantidad de cada producto
     */
    public double calcularTotal(List<Producto> carrito) {
        double total = 0;
        for (Producto p : carrito) {
            total += p.getPrecio() * p.getCantidad();
        }
        return total;
    }

    /**
     * Devuelve la cantidad total de ítems (unidades) en el carrito.
     *
     * @param carrito lista actual del carrito
     * @return número total de unidades
     */
    public int contarItems(List<Producto> carrito) {
        int count = 0;
        for (Producto p : carrito) count += p.getCantidad();
        return count;
    }

    /**
     * Simula la compra: valida stock, descuenta del catálogo (en memoria)
     * y vacía el carrito.
     *
     * @param carrito lista actual del carrito
     * @return mensaje de resultado ("ok: ..." o "error: ...")
     */
    public String procesarCompra(List<Producto> carrito) {
        if (carrito.isEmpty()) {
            return "error: El carrito está vacío.";
        }

        // Verificar stock de cada ítem
        for (Producto item : carrito) {
            Producto original = buscarEnCatalogo(item.getCodigo());
            if (original == null) {
                return "error: El producto '" + item.getNombre() + "' ya no está disponible.";
            }
            if (item.getCantidad() > original.getCantidad()) {
                return "error: Stock insuficiente para '" + item.getNombre()
                        + "'. Disponible: " + original.getCantidad();
            }
        }

        // Descontar stock
        for (Producto item : carrito) {
            Producto original = buscarEnCatalogo(item.getCodigo());
            if (original != null) {
                original.setCantidad(original.getCantidad() - item.getCantidad());
            }
        }

        double total = calcularTotal(carrito);
        int unidades = contarItems(carrito);
        carrito.clear();

        return "ok: Compra realizada con éxito. " + unidades
                + " unidad(es) por $" + String.format("%,.0f", total);
    }

    /**
     * Verifica si un producto ya está en el carrito.
     *
     * @param carrito lista actual del carrito
     * @param codigo  código del producto
     * @return true si el producto está en el carrito
     */
    public boolean estaEnCarrito(List<Producto> carrito, String codigo) {
        for (Producto p : carrito) {
            if (p.getCodigo().equals(codigo)) return true;
        }
        return false;
    }

    /**
     * Inicializa un carrito vacío (ArrayList).
     *
     * @return nueva lista vacía
     */
    public List<Producto> nuevoCarrito() {
        return new ArrayList<>();
    }
}
