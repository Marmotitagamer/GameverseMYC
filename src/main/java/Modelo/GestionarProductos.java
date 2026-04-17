package Modelo;

import java.util.LinkedList;

public class GestionarProductos {

    public static LinkedList<Producto> listaProductos = new LinkedList<>();

    // CREAR PRODUCTO
    public void agregarProducto(Producto producto) {
        listaProductos.add(producto);
    }

    // LISTAR TODOS
    public LinkedList<Producto> listarProductos() {
        return listaProductos;
    }

    // BUSCAR POR CÓDIGO
    public Producto buscarProducto(String codigo) {
        for (Producto p : listaProductos) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        return null;
    }

    // EDITAR PRODUCTO
    public void editarProducto(Producto actualizado) {
        for (int i = 0; i < listaProductos.size(); i++) {
            if (listaProductos.get(i).getCodigo().equals(actualizado.getCodigo())) {
                listaProductos.set(i, actualizado);
                break;
            }
        }
    }

    // ELIMINAR PRODUCTO
    public void eliminarProducto(String codigo) {
        listaProductos.removeIf(p -> p.getCodigo().equals(codigo));
    }
}
