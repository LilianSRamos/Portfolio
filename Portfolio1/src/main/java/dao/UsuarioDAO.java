package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Usuario;

public class UsuarioDAO {

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

    // CREATE: a senha já deve chegar como hash
    public void cadastrar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getSenha());
            ps.executeUpdate();
        }
    }

    public boolean emailExiste(String email) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE email = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // READ: devolve o usuário (com o hash da senha) ou null se não existir
    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT id, nome, email, senha FROM usuario WHERE email = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario(rs.getInt("id"), rs.getString("nome"), rs.getString("email"));
                    u.setSenha(rs.getString("senha"));
                    return u;
                }
            }
        }
        return null;
    }
}
