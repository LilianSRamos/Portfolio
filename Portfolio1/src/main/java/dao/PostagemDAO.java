package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Postagem;

public class PostagemDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/portfolio?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    private static final String SELECT =
        "SELECT p.id, p.titulo, p.conteudo, p.imagem, p.autor_id, u.nome AS autor_nome, "
      + "p.data_publicacao, p.status FROM postagem p JOIN usuario u ON u.id = p.autor_id ";

    private Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL não encontrado", e);
        }
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    private Postagem montar(ResultSet rs) throws SQLException {
        Postagem p = new Postagem();
        p.setId(rs.getInt("id"));
        p.setTitulo(rs.getString("titulo"));
        p.setConteudo(rs.getString("conteudo"));
        p.setImagem(rs.getString("imagem"));
        p.setAutorId(rs.getInt("autor_id"));
        p.setAutorNome(rs.getString("autor_nome"));
        p.setDataPublicacao(rs.getTimestamp("data_publicacao"));
        p.setStatus(rs.getString("status"));
        return p;
    }

    // CREATE
    public void inserir(Postagem p) throws SQLException {
        String sql = "INSERT INTO postagem (titulo, conteudo, imagem, autor_id, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getConteudo());
            ps.setString(3, p.getImagem());
            ps.setInt(4, p.getAutorId());
            ps.setString(5, p.getStatus());
            ps.executeUpdate();
        }
    }

    // READ: postagens publicadas (limite 0 = todas)
    public ArrayList<Postagem> listarPublicadas(int limite) throws SQLException {
        String sql = SELECT + "WHERE p.status = 'PUBLICADA' ORDER BY p.data_publicacao DESC, p.id DESC"
                + (limite > 0 ? " LIMIT ?" : "");
        ArrayList<Postagem> lista = new ArrayList<>();
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (limite > 0) {
                ps.setInt(1, limite);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        }
        return lista;
    }

    // READ: todas as postagens de um autor (inclui rascunhos)
    public ArrayList<Postagem> listarPorAutor(int autorId) throws SQLException {
        String sql = SELECT + "WHERE p.autor_id = ? ORDER BY p.data_publicacao DESC, p.id DESC";
        ArrayList<Postagem> lista = new ArrayList<>();
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, autorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        }
        return lista;
    }

    // READ: uma postagem (ou null)
    public Postagem buscarPorId(int id) throws SQLException {
        String sql = SELECT + "WHERE p.id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montar(rs) : null;
            }
        }
    }

    // UPDATE: só altera se a postagem for do autor informado
    public boolean atualizar(Postagem p) throws SQLException {
        String sql = "UPDATE postagem SET titulo = ?, conteudo = ?, imagem = ?, status = ? WHERE id = ? AND autor_id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getConteudo());
            ps.setString(3, p.getImagem());
            ps.setString(4, p.getStatus());
            ps.setInt(5, p.getId());
            ps.setInt(6, p.getAutorId());
            return ps.executeUpdate() > 0;
        }
    }

    // DELETE: só apaga se a postagem for do autor informado
    public boolean excluir(int id, int autorId) throws SQLException {
        String sql = "DELETE FROM postagem WHERE id = ? AND autor_id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, autorId);
            return ps.executeUpdate() > 0;
        }
    }
}
