package Modelo;

public class Usuario extends Persona {
   String tipoUsuario;
   String direccion;
   //constructor clase Usuario

    public Usuario(String nombre, int edad, String cedula, String correo, String password, String imagenPerfil, String tipoUsuario, String direccion) {
        super(nombre, edad, cedula, correo, password, imagenPerfil);
        this.tipoUsuario = tipoUsuario;
        this.direccion = direccion;
        
    }
   
    //encapsulamiento
    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    
   
}
