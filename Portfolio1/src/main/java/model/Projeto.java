package model;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.CadastroProjetos;

public class Projeto {

    private int id;
    private String titulo;
    private String descricao;
    private String link;
    private String tecnologias;
    private String data_criacao;

    public Projeto() {}

    public Projeto(int id, String titulo, String descricao, String link, String tecnologias, String data_criacao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.link = link;
        this.tecnologias = tecnologias;
        this.data_criacao = data_criacao;
    }

    public Projeto(String titulo, String descricao, String link, String tecnologias, String data_criacao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.link = link;
        this.tecnologias = tecnologias;
        this.data_criacao = data_criacao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getTecnologias() { return tecnologias; }
    public void setTecnologias(String tecnologias) { this.tecnologias = tecnologias; }

    public String getData_criacao() { return data_criacao; }
    public void setData_criacao(String data_criacao) { this.data_criacao = data_criacao; }

    public void cadastrarProjeto(Projeto projeto) throws SQLException {
        try {
            new CadastroProjetos().CadastrarProjeto(projeto);
        } catch (Exception ex) {
            Logger.getLogger(Projeto.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException(ex);
        }
    }

    public void buscaProjeto(Projeto projeto) throws SQLException {
        try {
            new CadastroProjetos().selecionarProjeto(projeto);
        } catch (Exception ex) {
            Logger.getLogger(Projeto.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException(ex);
        }
    }

    public void editaProjeto(Projeto projeto) throws SQLException {
        try {
            new CadastroProjetos().alterarProjeto(projeto);
        } catch (Exception ex) {
            Logger.getLogger(Projeto.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException(ex);
        }
    }

    public void excluirProjeto(Projeto projeto) throws SQLException {
        try {
            new CadastroProjetos().deletarProjeto(projeto.getId());
        } catch (Exception ex) {
            Logger.getLogger(Projeto.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException(ex);
        }
    }
}