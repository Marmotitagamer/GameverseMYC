package Modelo;

import java.util.LinkedList;

public class GestionarUsuarios {
    // métodos para el CRUD

    //definir contenedora LinkedList o ArrayList
    public static LinkedList<Usuario> listaUsuarios = new LinkedList<>();

    //CREAR USUARIO
    public void agregarUsuario(Usuario usuario) {
        //agregar a lista
        listaUsuarios.add(usuario);
    }

    //CONSULTAR TODOS
    public LinkedList<Usuario> ListarUsuarios() {
        //listaUsuarios.clear(); 
        return listaUsuarios;
    }

    //CONSULTAR POR CÓDIGO .-- filtrado
    public Usuario buscarUsuario(String codigo) {
        //foreach
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getCedula().equals(codigo)) {
                return usuario;
            }
        }
        return null;
    }

    //EDITAR USUARIO
    public void editarUsuario(Usuario actualizado) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            Usuario usuario = listaUsuarios.get(i);
            if (usuario.getCedula().equals(actualizado.getCedula())) {
                listaUsuarios.set(i, actualizado);
                System.out.println("Usuario actualizado: " + actualizado.getNombre());
                break;
            }
        }
    }

    //ELIMINAR UN USUARIO
    public void eliminarUsuario(String cedula) {
        /*for (Usuario usuario: listaUsuarios){
           if(usuario.getCedula().equals)*/
        //funcion flecha en una sola linea for e if
        listaUsuarios.removeIf(usuario -> usuario.getCedula().equals(cedula));
    }

}
