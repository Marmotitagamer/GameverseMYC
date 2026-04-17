package Modelo;

public class Persona {
    String nombre;
    int edad;
    String cedula;
    String correo;
    String password;
    String imagenPerfil;
    
    //constructor
    public Persona(String nombre, int edad, String cedula, String correo, String password, String imagenPerfil) {
        this.nombre = nombre;
        this.edad = edad;
        this.cedula = cedula;
        this.correo = correo;
        this.password = password;
        this.imagenPerfil = imagenPerfil;
    }
    //Encapsulamiento

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }
    
}
