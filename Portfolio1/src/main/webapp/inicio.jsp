<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Início" />
<%@ include file="/topo.jspf" %>

<section class="hero">
  <img src="${raiz}/assets/img/cursos.png" alt="" onerror="this.style.display='none'">
  <div>
    <h1 class="titulo-pagina">Meu Portfólio</h1>
    <div class="subtitulo">Postagens e projetos reunidos em um só lugar.</div>
  </div>
</section>

<section class="secao">
  <div class="secao-topo">
    <h2 class="h4 m-0">Postagens recentes</h2>
    <a href="${raiz}/postagens">Ver todas</a>
  </div>
  <c:choose>
    <c:when test="${empty postagens}">
      <div class="painel vazio">
        <p class="mb-3">Ainda não há postagens publicadas.</p>
        <c:if test="${not empty sessionScope.usuarioLogado}">
          <a class="btn btn-destaque" href="${raiz}/cadastroPostagem">Escrever a primeira postagem</a>
        </c:if>
      </div>
    </c:when>
    <c:otherwise>
      <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-3">
        <c:forEach var="p" items="${postagens}">
          <%@ include file="/cartaoPostagem.jspf" %>
        </c:forEach>
      </div>
    </c:otherwise>
  </c:choose>
</section>

<section class="secao">
  <div class="secao-topo">
    <h2 class="h4 m-0">Projetos</h2>
    <c:if test="${not empty sessionScope.usuarioLogado}">
      <a href="${raiz}/listaProjeto">Gerenciar projetos</a>
    </c:if>
  </div>
  <c:choose>
    <c:when test="${empty projetos}">
      <div class="painel vazio"><p class="mb-0">Nenhum projeto cadastrado ainda.</p></div>
    </c:when>
    <c:otherwise>
      <div class="row row-cols-1 row-cols-md-2 g-3">
        <c:forEach var="pr" items="${projetos}">
          <div class="col">
            <div class="painel p-3 h-100">
              <h3 class="h5"><c:out value="${pr.titulo}"/></h3>
              <p class="resumo"><c:out value="${pr.descricao}"/></p>
              <div class="mb-2">
                <c:forTokens var="t" items="${pr.tecnologias}" delims=",">
                  <span class="etiqueta"><c:out value="${t}"/></span>
                </c:forTokens>
              </div>
              <a href="<c:out value='${pr.link}'/>" target="_blank" rel="noopener">Abrir projeto</a>
            </div>
          </div>
        </c:forEach>
      </div>
    </c:otherwise>
  </c:choose>
</section>

<%@ include file="/rodape.jspf" %>
