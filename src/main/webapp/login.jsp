<%@include file="./lib/header.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // Si ya está logueado, redirigir al catálogo
    if (session.getAttribute("usuarioLogueado") != null) {
        response.sendRedirect("catalogo.jsp");
        return;
    }

    String error   = (String) request.getAttribute("error");
    String msgParam = request.getParameter("msg");
    String tipoMsg  = request.getParameter("tipoMsg");
    String correoIngresado = (String) request.getAttribute("correoIngresado");
    if (correoIngresado == null) correoIngresado = "";
%>

<div class="container mt-5" style="max-width: 480px;">
    <h2 class="mb-4 text-center">Iniciar Sesión</h2>

    <%-- Mensaje de éxito (ej. registro exitoso) --%>
    <% if (msgParam != null && !msgParam.isEmpty()) { %>
        <div class="alert alert-<%= "ok".equals(tipoMsg) ? "success" : "danger" %> alert-dismissible fade show" role="alert">
            <%= msgParam.replace("+", " ") %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    <% } %>

    <%-- Error de autenticación --%>
    <% if (error != null) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= error %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    <% } %>

    <div class="card shadow-sm">
        <div class="card-body p-4">
            <form method="POST" action="login">

                <div class="mb-3">
                    <label for="correo" class="form-label fw-semibold">Correo electrónico</label>
                    <input type="email" class="form-control" id="correo" name="correo"
                           value="<%= correoIngresado %>"
                           placeholder="tu@correo.com" required autofocus>
                </div>

                <div class="mb-3">
                    <label for="password" class="form-label fw-semibold">Contraseña</label>
                    <input type="password" class="form-control" id="password" name="password"
                           placeholder="Ingresa tu contraseña" required>
                </div>

                <div class="d-grid gap-2 mt-4">
                    <button type="submit" class="btn btn-primary">Ingresar</button>
                </div>
            </form>
        </div>
    </div>

    <p class="text-center mt-3 text-muted">
        ¿No tienes cuenta?
        <a href="registro" class="text-decoration-none">Regístrate aquí</a>
    </p>
</div>

<%@include file="./lib/footer.jsp"%>
