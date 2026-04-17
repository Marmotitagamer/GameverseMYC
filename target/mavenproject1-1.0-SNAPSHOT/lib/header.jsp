<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
              rel="stylesheet"
              integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
              crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI"
                crossorigin="anonymous"></script>
        <link href="./styles/style.css" rel="stylesheet" type="text/css"/>
        <title>GameVerse Digital</title>
    </head>
    <body>
        <%
            String nombreSesion = (String) session.getAttribute("nombreUsuario");
            String tipoSesion   = (String) session.getAttribute("tipoUsuario");
            boolean autenticado = (nombreSesion != null);
        %>
        <nav class="navbar navbar-expand-lg bg-body-tertiary">
            <div class="container-fluid">
                <a class="navbar-brand fw-bold" href="index.jsp"> GameVerse Digital</a>
                <button class="navbar-toggler" type="button"
                        data-bs-toggle="collapse" data-bs-target="#navbarMain"
                        aria-controls="navbarMain" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarMain">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link" href="./catalogo.jsp">Catálogo</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="compras"> Compras</a>
                        </li>
                        <% if (autenticado && "admin".equalsIgnoreCase(tipoSesion)) { %>
                        <li class="nav-item">
                            <a class="nav-link" href="usuarios"> Admin Usuarios</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="productos"> Admin Productos</a>
                        </li>
                        <% } %>
                    </ul>

                    <%-- Búsqueda --%>
                    <form class="d-flex me-3" role="search">
                        <input class="form-control me-2" type="search"
                               placeholder="Buscar juego" aria-label="Search"/>
                        <button class="btn btn-outline-success" type="submit">Buscar</button>
                    </form>

                    <%-- Sesión --%>
                    <ul class="navbar-nav">
                        <% if (autenticado) { %>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">
                                 <%= nombreSesion %>
                                <% if (tipoSesion != null) { %>
                                    <span class="badge bg-secondary ms-1" style="font-size:.7rem;">
                                        <%= tipoSesion %>
                                    </span>
                                <% } %>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><a class="dropdown-item" href="login?accion=logout">Cerrar sesión</a></li>
                            </ul>
                        </li>
                        <% } else { %>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button"
                               data-bs-toggle="dropdown" aria-expanded="false">
                                Cuenta
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><a class="dropdown-item" href="./registro">Registrarse</a></li>
                                <li><a class="dropdown-item" href="./login.jsp">Iniciar Sesión</a></li>
                            </ul>
                        </li>
                        <% } %>
                    </ul>
                </div>
            </div>
        </nav>
