package servlets;

import Modelo.GestionarUsuarios;
import Modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class ServletLogin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        // Cerrar sesión
        if ("logout".equals(accion)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=Sesion+cerrada+exitosamente&tipoMsg=ok");
            return;
        }

        // Si ya está autenticado, redirigir al catálogo
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            response.sendRedirect(request.getContextPath() + "/catalogo.jsp");
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String correo   = request.getParameter("correo");
        String password = request.getParameter("password");

        // Validar campos vacíos
        if (correo == null || correo.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Por favor completa todos los campos.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Buscar usuario por correo en la lista estática
        GestionarUsuarios gestor = new GestionarUsuarios();
        Usuario encontrado = null;
        for (Usuario u : gestor.ListarUsuarios()) {
            if (u.getCorreo().equalsIgnoreCase(correo.trim())
                    && u.getPassword().equals(password)) {
                encontrado = u;
                break;
            }
        }

        if (encontrado == null) {
            request.setAttribute("error", "Correo o contraseña incorrectos.");
            request.setAttribute("correoIngresado", correo);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Crear sesión y guardar usuario
        HttpSession session = request.getSession(true);
        session.setAttribute("usuarioLogueado", encontrado);
        session.setAttribute("nombreUsuario", encontrado.getNombre());
        session.setAttribute("tipoUsuario", encontrado.getTipoUsuario());
        session.setMaxInactiveInterval(1800); // 30 minutos

        // Redirigir según tipo de usuario
        if ("admin".equalsIgnoreCase(encontrado.getTipoUsuario())) {
            response.sendRedirect(request.getContextPath() + "/adminUsuarios.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/catalogo.jsp");
        }
    }
}
