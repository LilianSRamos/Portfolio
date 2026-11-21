<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="tituloPagina" value="${postagem.titulo}" />
<%@ include file="/topo.jspf" %>

<a href="${raiz}/postagens">&larr; Voltar às postagens</a>

<article class="mt-3">
  <h1 class="h2 titulo-pagina"><c:out value="${postagem.titulo}"/></h1>
  <div class="meta mb-3">
    Por <c:out value="${postagem.autorNome}"/> em <fmt:formatDate value="${postagem.dataPublicacao}" pattern="dd/MM/yyyy"/>
    <c:if test="${postagem.status != 'PUBLICADA'}"> (rascunho, só você vê)</c:if>
  </div>

  <c:if test="${not empty postagem.imagem}">
    <img class="capa-post" src="${raiz}/uploads/<c:out value='${postagem.imagem}'/>" alt="">
  </c:if>

  <div class="texto-post"><c:out value="${postagem.conteudo}"/></div>

  <c:if test="${not empty sessionScope.usuarioLogado and sessionScope.usuarioLogado.id == postagem.autorId}">
    <div class="mt-4">
      <a class="btn btn-outline-secondary" href="${raiz}/selecaoPostagem?id=${postagem.id}">Editar postagem</a>
    </div>
  </c:if>
</article>

<%@ include file="/rodape.jspf" %>
