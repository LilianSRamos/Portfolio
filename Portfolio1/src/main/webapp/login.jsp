<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Entrar" />
<%@ include file="/topo.jspf" %>

<div class="row justify-content-center">
  <div class="col-md-7 col-lg-5">
    <h1 class="h3 titulo-pagina">Entrar</h1>
    <div class="subtitulo mb-3">Acesse sua conta para gerenciar os projetos.</div>

    <c:if test="${not empty erro}">
      <div class="alert alert-danger"><c:out value="${erro}"/></div>
    </c:if>
    <c:choose>
      <c:when test="${param.msg == 'conta-criada'}"><div class="alert alert-success">Conta criada. Entre com seu e-mail e senha.</div></c:when>
      <c:when test="${param.msg == 'saiu'}"><div class="alert alert-info">Você saiu da conta.</div></c:when>
      <c:when test="${param.msg == 'entre'}"><div class="alert alert-warning">Entre na sua conta para acessar os projetos.</div></c:when>
    </c:choose>

    <div class="painel p-4">
      <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="mb-3">
          <label for="email" class="form-label">E-mail</label>
          <input type="email" class="form-control" id="email" name="email" maxlength="150"
                 value="<c:out value='${email}'/>" required autofocus>
        </div>
        <div class="mb-3">
          <label for="senha" class="form-label">Senha</label>
          <input type="password" class="form-control" id="senha" name="senha" required>
        </div>
        <button type="submit" class="btn btn-destaque w-100">Entrar</button>
      </form>
    </div>

    <p class="text-center mt-3 mb-0">
      Ainda não tem conta?
      <a href="${pageContext.request.contextPath}/cadastroUsuario">Criar conta</a>
    </p>
  </div>
</div>

<%@ include file="/rodape.jspf" %>
