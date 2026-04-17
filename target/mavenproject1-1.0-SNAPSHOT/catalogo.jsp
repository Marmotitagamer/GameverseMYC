<%-- 
    Document   : catalogo
    Created on : 26/03/2026, 7:39:38 a. m.
    Author     : Mflass
--%>
<%@include file="lib/header.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>
    .catalogo-wrapper {
        display: flex;
        flex-wrap: wrap;
        gap: 1.5rem;
        padding: 2rem 3rem 6rem 3rem;
        justify-content: flex-start;
    }
</style>

<div class="catalogo-wrapper">

<div class="container" style="width: 18rem;">
    <img src="./images/EldenRing.png" class="container-img-top" alt="Imagen Juego Elden Ring">
  <div class="container-body">
    <h5 class="container-title">Elden Ring</h5>
    <p class="container-text">Explora las Tierras Intermedias y derrota a los semidioses en este épico RPG de mundo abierto.</p>
  </div>
  <ul class="list-group list-group-flush">
    <li class="list-group-item">Precio: $59.990</li>
    <li class="list-group-item">Genero: RPG / Mundo Abierto</li>
    <li class="list-group-item">Clasificación: +17</li>
  </ul>
  <div class="container-body">
      <a href="./compras.jsp" class="container-link">Comprar</a>
      
  </div>
</div>

<div class="container" style="width: 18rem;">
    <img src="./images/GOWRagnarok.jpg" class="container-img-top" alt="Imagen Juego God Of War: Ragnarok">
  <div class="container-body">
    <h5 class="container-title">God of War Ragnarök</h5>
    <p class="container-text">Kratos y Atreus enfrentan el fin del mundo nórdico en esta impresionante aventura de acción.</p>
  </div>
  <ul class="list-group list-group-flush">
    <li class="list-group-item">Precio: $49.990</li>
    <li class="list-group-item">Genero: Acción / Aventura</li>
    <li class="list-group-item">Clasificación: +17</li>
  </ul>
  <div class="container-body">
    <a href="./compras.jsp" class="container-link">Comprar</a>
  </div>
</div>

<div class="container" style="width: 18rem;">
    <img src="./images/Cyberpunk.jpeg" class="container-img-top" alt="Imagen Juego Cyberpunk 2077">
  <div class="container-body">
    <h5 class="container-title">Cyberpunk 2077</h5>
    <p class="container-text">Sumérgete en Night City, una megalópolis futurista llena de peligro, tecnología y crimen.</p>
  </div>
  <ul class="list-group list-group-flush">
    <li class="list-group-item">Precio: $39.990</li>
    <li class="list-group-item">Genero: RPG / Acción</li>
    <li class="list-group-item">Clasificación: +17</li>
  </ul>
  <div class="container-body">
    <a href="./compras.jsp" class="container-link">Comprar</a>
  </div>
</div>

<div class="container" style="width: 18rem;">
    <img src="./images/Zelda.jpg" class="container-img-top" alt="Imagen Juego Zelda: Tears of the Kingdom">
  <div class="container-body">
    <h5 class="container-title">Zelda: Tears of the Kingdom</h5>
    <p class="container-text">Link regresa a Hyrule para descubrir una nueva amenaza que surge desde las profundidades de la tierra.</p>
  </div>
  <ul class="list-group list-group-flush">
    <li class="list-group-item">Precio: $69.990</li>
    <li class="list-group-item">Genero: Aventura / Puzzle</li>
    <li class="list-group-item">Clasificación: +10</li>
  </ul>
  <div class="container-body">
    <a href="./compras.jsp" class="container-link">Comprar</a>
  </div>
</div>

<div class="container" style="width: 18rem;">
    <img src="./images/images.jpeg" class="container-img-top" alt="Imagen Juego Hollow knight">
  <div class="container-body">
    <h5 class="container-title">Hollow Knight</h5>
    <p class="container-text">Explora un vasto reino subterráneo de insectos en este desafiante metroidvania indie.</p>
  </div>
  <ul class="list-group list-group-flush">
    <li class="list-group-item">Precio: $14.990</li>
    <li class="list-group-item">Genero: Plataformas / Indie</li>
    <li class="list-group-item">Clasificación: +10</li>
  </ul>
  <div class="container-body">
    <a href="./compras.jsp" class="container-link">Comprar</a>
  </div>
</div>

<div class="container" style="width: 18rem;">
    <img src="./images/RDR2.jpeg" class="container-img-top" alt="Imagen Juego Red Dead Redemption 2">
  <div class="container-body">
    <h5 class="container-title">Red Dead Redemption 2</h5>
    <p class="container-text">Vive la vida de un forajido en el ocaso del Viejo Oeste americano con una historia inolvidable.</p>
  </div>
  <ul class="list-group list-group-flush">
    <li class="list-group-item">Precio: $44.990</li>
    <li class="list-group-item">Genero: Acción / Mundo Abierto</li>
    <li class="list-group-item">Clasificación: +17</li>
  </ul>
  <div class="container-body">
    <a href="./compras.jsp" class="container-link">Comprar</a>
  </div>
</div>
 

</div>

<%@include file="lib/footer.jsp"%>
