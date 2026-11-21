package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Projeto;

public class CadastroProjetos {

    private static final String URL = "jdbc:mysql://localhost:3306/portfolio?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    private Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL não encontrado", e);
        }
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    // CREATE
    public void CadastrarProjeto(Projeto p) throws SQLException {
        String sql = "INSERT INTO project (titulo, descricao, link, tecnologias, data_criacao) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getDescricao());
            ps.setString(3, p.getLink());
            ps.setString(4, p.getTecnologias());
            ps.setString(5, p.getData_criacao());
            ps.executeUpdate();
        }
    }

    // READ 
    public ArrayList<Projeto> listarProjetos() throws SQLException {
        ArrayList<Projeto> lista = new ArrayList<>();
        String sql = "SELECT id, titulo, descricao, link, tecnologias, data_criacao FROM project ORDER BY id DESC";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Projeto(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    rs.getString("link"),
                    rs.getString("tecnologias"),
                    rs.getString("data_criacao")));
            }
        }
        return lista;
    }

    // READ 
    public void selecionarProjeto(Projeto p) throws SQLException {
        String sql = "SELECT titulo, descricao, link, tecnologias, data_criacao FROM project WHERE id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p.setTitulo(rs.getString("titulo"));
                    p.setDescricao(rs.getString("descricao"));
                    p.setLink(rs.getString("link"));
                    p.setTecnologias(rs.getString("tecnologias"));
                    p.setData_criacao(rs.getString("data_criacao"));
                }
            }
        }
    }

    // UPDATE
    public void alterarProjeto(Projeto p) throws SQLException {
        String sql = "UPDATE project SET titulo = ?, descricao = ?, link = ?, tecnologias = ?, data_criacao = ? WHERE id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getDescricao());
            ps.setString(3, p.getLink());
            ps.setString(4, p.getTecnologias());
            ps.setString(5, p.getData_criacao());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void deletarProjeto(int id) throws SQLException {
        String sql = "DELETE FROM project WHERE id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
