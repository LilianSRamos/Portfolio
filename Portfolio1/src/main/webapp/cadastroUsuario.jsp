<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Criar conta" />
<%@ include file="/topo.jspf" %>

<div class="row justify-content-center">
  <div class="col-md-8 col-lg-6">
    <h1 class="h3 titulo-pagina">Criar conta</h1>
    <div class="subtitulo mb-3">Cadastre-se para registrar os seus projetos.</div>

    <c:if test="${not empty erro}">
      <div class="alert alert-danger"><c:out value="${erro}"/></div>
    </c:if>

    <div class="painel p-4">
      <form method="post" action="${pageContext.request.contextPath}/cadastroUsuario">
        <div class="mb-3">
          <label for="nome" class="form-label">Nome</label>
          <input type="text" class="form-control" id="nome" name="nome" maxlength="100"
                 value="<c:out value='${nome}'/>" required autofocus>
        </div>
        <div class="mb-3">
          <label for="email" class="form-label">E-mail</label>
          <input type="email" class="form-control" id="email" name="email" maxlength="150"
                 value="<c:out value='${email}'/>" required>
        </div>
        <div class="row">
          <div class="col-md-6 mb-3">
            <label for="senha" class="form-label">Senha</label>
            <input type="password" class="form-control" id="senha" name="senha" minlength="6" required>
            <div class="form-text">Mínimo de 6 caracteres.</div>
          </div>
          <div class="col-md-6 mb-3">
            <label for="confirmar" class="form-label">Repita a senha</label>
            <input type="password" class="form-control" id="confirmar" name="confirmar" minlength="6" required>
          </div>
        </div>
        <div class="d-flex gap-2">
          <button type="submit" class="btn btn-destaque">Criar conta</button>
          <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/login">Cancelar</a>
        </div>
      </form>
    </div>
  </div>
</div>

<%@ include file="/rodape.jspf" %>
