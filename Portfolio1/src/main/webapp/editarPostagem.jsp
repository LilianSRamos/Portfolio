<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Editar postagem" />
<%@ include file="/topo.jspf" %>

<h1 class="h3 titulo-pagina">Editar postagem</h1>
<div class="subtitulo mb-3">Altere os dados e salve as mudanças.</div>

<c:if test="${not empty erro}">
  <div class="alert alert-danger"><c:out value="${erro}"/></div>
</c:if>

<div class="painel p-4">
  <form method="post" action="${raiz}/editarPostagem" enctype="multipart/form-data">
    <input type="hidden" name="id" value="${postagem.id}">
    <div class="mb-3">
      <label for="titulo" class="form-label">Título</label>
      <input type="text" class="form-control" id="titulo" name="titulo" maxlength="180"
             value="<c:out value='${postagem.titulo}'/>" required>
    </div>
    <div class="mb-3">
      <label for="conteudo" class="form-label">Conteúdo</label>
      <textarea class="form-control" id="conteudo" name="conteudo" rows="8" required><c:out value="${postagem.conteudo}"/></textarea>
    </div>
    <div class="row">
      <div class="col-md-8 mb-3">
        <label for="imagem" class="form-label">Imagem</label>
        <c:if test="${not empty postagem.imagem}">
          <img class="miniatura" src="${raiz}/uploads/<c:out value='${postagem.imagem}'/>" alt="Imagem atual">
        </c:if>
        <input type="file" class="form-control" id="imagem" name="imagem" accept="image/png,image/jpeg,image/gif,image/webp">
        <div class="form-text">Deixe em branco para manter a imagem atual.</div>
      </div>
      <div class="col-md-4 mb-3">
        <label for="status" class="form-label">Situação</label>
        <select class="form-select" id="status" name="status">
          <option value="PUBLICADA" ${postagem.status == 'PUBLICADA' ? 'selected' : ''}>Publicada</option>
          <option value="RASCUNHO" ${postagem.status != 'PUBLICADA' ? 'selected' : ''}>Rascunho</option>
        </select>
      </div>
    </div>
    <div class="d-flex gap-2">
      <button type="submit" class="btn btn-destaque">Salvar alterações</button>
      <a class="btn btn-outline-secondary" href="${raiz}/minhasPostagens">Cancelar</a>
    </div>
  </form>
</div>

<script>
  (function () {
    var campo = document.getElementById('imagem');
    var tipos = ['image/png', 'image/jpeg', 'image/gif', 'image/webp'];
    campo.addEventListener('change', function () {
      var f = campo.files[0];
      if (f && (tipos.indexOf(f.type) === -1 || f.size > 5 * 1024 * 1024)) {
        alert('Escolha uma imagem PNG, JPG, GIF ou WEBP de até 5 MB.');
        campo.value = '';
      }
    });
  })();
</script>

<%@ include file="/rodape.jspf" %>
