/*
 * GameVerse Digital - Servicio de Usuarios
 */
package Clases;

import java.util.ArrayList;

/**
 * Gestiona la lista de usuarios en memoria.
 * Reemplaza los métodos stub de Usuario.java con lógica real.
 */
public class UsuarioService {

    private ArrayList<Usuario> lista = new ArrayList<>();
    private int contadorId = 1;

    public UsuarioService() {
        // Datos de ejemplo
        agregar("1001", "Carlos Ramirez",  "Bogotá",    25, TipoCliente.REGULAR, "CLI-001");
        agregar("1002", "Laura Torres",    "Medellín",  30, TipoCliente.PREMIUM, "CLI-002");
        agregar("1003", "Miguel Perez",    "Cali",      22, TipoCliente.VIP,     "CLI-003");
    }

    /** CREATE */
    public void agregar(String identificacion, String nombre, String ciudad,
                        int edad, TipoCliente tipo, String codigo) {
        lista.add(new Usuario(identificacion, nombre, ciudad, edad, tipo, codigo));
    }

    /** READ - todos */
    public ArrayList<Usuario> getLista() {
        return lista;
    }

    /** READ - buscar por código de cliente */
    public Usuario buscarPorCodigo(String codigo) {
        for (Usuario u : lista)
            if (u.getCodigoCliente().equalsIgnoreCase(codigo)) return u;
        return null;
    }

    /** READ - buscar por nombre (parcial) */
    public ArrayList<Usuario> buscarPorNombre(String nombre) {
        ArrayList<Usuario> resultado = new ArrayList<>();
        for (Usuario u : lista)
            if (u.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                resultado.add(u);
        return resultado;
    }

    /** READ - buscar por identificación */
    public Usuario buscarPorIdentificacion(String id) {
        for (Usuario u : lista)
            if (u.getIdentificacion().equals(id)) return u;
        return null;
    }

    /** UPDATE */
    public boolean actualizar(String codigo, String nombre, String ciudad,
                               int edad, TipoCliente tipo) {
        Usuario u = buscarPorCodigo(codigo);
        if (u == null) return false;
        u.setNombre(nombre);
        u.setCiudadOrigen(ciudad);
        u.setEdad(edad);
        u.setTipoCliente(tipo);
        return true;
    }

    /** DELETE */
    public boolean eliminar(String codigo) {
        Usuario u = buscarPorCodigo(codigo);
        if (u == null) return false;
        lista.remove(u);
        return true;
    }
}
