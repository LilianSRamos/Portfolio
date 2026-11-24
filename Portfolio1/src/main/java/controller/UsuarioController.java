package controller;

import java.io.IOException;
import java.sql.SQLException;

import dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Usuario;
import util.Senha;

@WebServlet(name = "UsuarioController", urlPatterns = { "/login", "/logout", "/cadastroUsuario" })
public class UsuarioController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String rota = request.getServletPath();

        switch (rota) {
        case "/logout":
            HttpSession sessao = request.getSession(false);
            if (sessao != null) {
                sessao.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login?msg=saiu");
            break;

        case "/cadastroUsuario":
            request.getRequestDispatcher("/cadastroUsuario.jsp").forward(request, response);
            break;

        default: // /login
            if (logado(request)) {
                response.sendRedirect(request.getContextPath() + "/minhasPostagens");
            } else {
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            if ("/cadastroUsuario".equals(request.getServletPath())) {
                cadastrar(request, response);
            } else if ("/login".equals(request.getServletPath())) {
                entrar(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
        } catch (SQLException e) {
            throw new ServletException("Erro ao acessar o banco de dados", e);
        }
    }

    private void entrar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String email = valor(request, "email").toLowerCase();
        String senha = request.getParameter("senha");

        Usuario u = new UsuarioDAO().buscarPorEmail(email);

        if (u == null || !Senha.conferir(senha, u.getSenha())) {
            request.setAttribute("erro", "E-mail ou senha incorretos.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        request.changeSessionId(); 
        u.setSenha(null);          
        request.getSession().setAttribute("usuarioLogado", u);
        response.sendRedirect(request.getContextPath() + "/minhasPostagens");
    }

    private void cadastrar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String nome = valor(request, "nome");
        String email = valor(request, "email").toLowerCase();
        String senha = request.getParameter("senha");
        String confirmar = request.getParameter("confirmar");

        String erro = null;
        if (nome.isEmpty() || email.isEmpty() || senha == null || senha.isEmpty()) {
            erro = "Preencha todos os campos.";
        } else if (senha.length() < 6) {
            erro = "A senha precisa ter pelo menos 6 caracteres.";
        } else if (!senha.equals(confirmar)) {
            erro = "As senhas não são iguais.";
        } else if (new UsuarioDAO().emailExiste(email)) {
            erro = "Já existe uma conta com esse e-mail.";
        }

        if (erro != null) {
            request.setAttribute("erro", erro);
            request.setAttribute("nome", nome);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/cadastroUsuario.jsp").forward(request, response);
            return;
        }

        new UsuarioDAO().cadastrar(new Usuario(nome, email, Senha.gerarHash(senha)));
        response.sendRedirect(request.getContextPath() + "/login?msg=conta-criada");
    }

    private boolean logado(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        return sessao != null && sessao.getAttribute("usuarioLogado") != null;
    }

    private String valor(HttpServletRequest request, String campo) {
        String v = request.getParameter(campo);
        return v == null ? "" : v.trim();
    }
}
