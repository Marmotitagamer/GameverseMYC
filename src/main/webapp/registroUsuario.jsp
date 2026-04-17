<%@include file="lib/header.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // Si ya está logueado, redirigir al catálogo
    if (session.getAttribute("usuarioLogueado") != null) {
        response.sendRedirect("catalogo.jsp");
        return;
    }
    String error = (String) request.getAttribute("error");
%>

<div class="container mt-4" style="max-width: 680px;">
    <h2 class="mb-4 text-center">Crear Cuenta</h2>

    <% if (error != null) { %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <%= error %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    <% } %>

    <div class="card shadow-sm">
        <div class="card-body p-4">
            <form method="POST" action="registro">

                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Cédula / Identificación *</label>
                        <input type="text" class="form-control" name="cedula"
                               placeholder="Ej: 12345678" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Nombre completo *</label>
                        <input type="text" class="form-control" name="nombre"
                               placeholder="Tu nombre" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Edad *</label>
                        <input type="number" class="form-control" name="edad"
                               placeholder="Ej: 25" min="1" max="120" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Correo electrónico *</label>
                        <input type="email" class="form-control" name="correo"
                               placeholder="tu@correo.com" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Contraseña *</label>
                        <input type="password" class="form-control" name="password"
                               placeholder="Mínimo 6 caracteres" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Tipo de usuario *</label>
                        <select class="form-select" name="tipoUsuario" required>
                            <option value="cliente" selected>Cliente</option>
                            <option value="vendedor">Vendedor</option>
                            <option value="admin">Administrador</option>
                        </select>
                    </div>
                    <div class="col-md-8">
                        <label class="form-label fw-semibold">Dirección</label>
                        <input type="text" class="form-control" name="direccion"
                               placeholder="Calle 123, Ciudad">
                    </div>
                    
                </div>

                <div class="d-flex gap-2 mt-4">
                    <button type="submit" class="btn btn-primary flex-fill">Registrarse</button>
                    <a href="login.jsp" class="btn btn-secondary">Cancelar</a>
                </div>
            </form>
        </div>
    </div>

    <p class="text-center mt-3 text-muted">
        ¿Ya tienes cuenta? <a href="login.jsp" class="text-decoration-none">Inicia sesión</a>
    </p>
</div>

<%@include file="lib/footer.jsp" %>
