package servlets;

import Modelo.GestionarUsuarios;
import Modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/registro")
public class ServletRegistro extends HttpServlet {

    private final GestionarUsuarios gestor = new GestionarUsuarios();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Mostrar el formulario de registro
        request.getRequestDispatcher("/registroUsuario.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String cedula    = request.getParameter("cedula");
        String nombre    = request.getParameter("nombre");
        String edadStr   = request.getParameter("edad");
        String correo    = request.getParameter("correo");
        String password  = request.getParameter("password");
        String tipo      = request.getParameter("tipoUsuario");
        String direccion = request.getParameter("direccion");
        String imagen    = request.getParameter("imagenPerfil");

        // Validaciones básicas
        if (cedula == null || cedula.trim().isEmpty()
                || nombre == null || nombre.trim().isEmpty()
                || correo == null || correo.trim().isEmpty()
                || password == null || password.trim().isEmpty()
                || edadStr == null || edadStr.trim().isEmpty()) {
            request.setAttribute("error", "Todos los campos obligatorios deben completarse.");
            request.getRequestDispatcher("/registroUsuario.jsp").forward(request, response);
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr.trim());
            if (edad < 1 || edad > 120) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            request.setAttribute("error", "La edad debe ser un número válido.");
            request.getRequestDispatcher("/registroUsuario.jsp").forward(request, response);
            return;
        }

        // Verificar que la cédula no exista
        if (gestor.buscarUsuario(cedula.trim()) != null) {
            request.setAttribute("error", "Ya existe un usuario registrado con esa cédula.");
            request.getRequestDispatcher("/registroUsuario.jsp").forward(request, response);
            return;
        }

        // Verificar que el correo no esté en uso
        boolean correoEnUso = gestor.ListarUsuarios().stream()
                .anyMatch(u -> u.getCorreo().equalsIgnoreCase(correo.trim()));
        if (correoEnUso) {
            request.setAttribute("error", "Ya existe un usuario registrado con ese correo.");
            request.getRequestDispatcher("/registroUsuario.jsp").forward(request, response);
            return;
        }

        // Crear y guardar el usuario
        if (imagen == null) imagen = "";
        if (tipo == null) tipo = "cliente";
        if (direccion == null) direccion = "";

        Usuario nuevo = new Usuario(
                nombre.trim(), edad, cedula.trim(),
                correo.trim(), password,
                imagen.trim(), tipo, direccion.trim()
        );
        gestor.agregarUsuario(nuevo);

        // Redirigir al login con mensaje de éxito
        response.sendRedirect(request.getContextPath()
                + "/login.jsp?msg=Usuario+registrado+exitosamente.+Inicia+sesion.&tipoMsg=ok");
    }
}
