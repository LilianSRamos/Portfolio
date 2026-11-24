package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.CadastroProjetos;
import dao.PostagemDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Projeto;

@WebServlet(name = "InicioController", urlPatterns = { "/inicio" })
public class InicioController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        HttpSession sessao = request.getSession(false);
        if (sessao != null && sessao.getAttribute("usuarioLogado") != null) {
            response.sendRedirect(request.getContextPath() + "/minhasPostagens");
            return;
        }

        try {
            request.setAttribute("postagens", new PostagemDAO().listarPublicadas(3));

            ArrayList<Projeto> todos = new CadastroProjetos().listarProjetos();
            request.setAttribute("projetos", todos.size() > 6 ? new ArrayList<>(todos.subList(0, 6)) : todos);

            request.getRequestDispatcher("/inicio.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Erro ao acessar o banco de dados", e);
        }
    }
}
