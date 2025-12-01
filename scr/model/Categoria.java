package model;

import dao.CategoriaDAO;
import java.util.List;
import java.util.UUID;

public class Categoria {

    private String idCategoria;
    private String nomeCategoria;
    private Boolean status;

    public Categoria() {}

    public Categoria(String idCategoria, String nomeCategoria, Boolean status) {
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
        this.status = status;
    }

    // ---------------- MÉTODOS ESTÁTICOS ----------------

    public static Categoria cadastrarCategoria(String nome) {

        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("[ERRO] O nome não pode ser vazio.");
            return null;
        }

        CategoriaDAO dao = new CategoriaDAO();

        if (dao.buscarCategoria(nome) != null) {
            System.out.println("[ERRO] Já existe uma categoria com esse nome!");
            return null;
        }

        Categoria nova = new Categoria(UUID.randomUUID().toString(), nome, true);

        if (dao.inserir(nova)) {
            System.out.println("Categoria cadastrada: " + nome);
            return nova;
        }

        System.out.println("[ERRO] Falha ao salvar no banco.");
        return null;
    }

    public static List<Categoria> listarCategorias() {
        return new CategoriaDAO().listarTodos();
    }

    public static Categoria buscarCategoria(String nome) {
        return new CategoriaDAO().buscarCategoria(nome);
    }

    // ---------------- MÉTODOS DE INSTÂNCIA ----------------

    public Categoria editarCategoria(String novoNome) {

        if (novoNome == null || novoNome.trim().isEmpty()) {
            System.out.println("[ERRO] O novo nome não pode ser vazio.");
            return null;
        }

        if (!this.status) {
            System.out.println("[ERRO] Categoria desativada NÃO pode ser editada.");
            return null;
        }

        CategoriaDAO dao = new CategoriaDAO();

        if (dao.buscarCategoria(novoNome) != null) {
            System.out.println("[ERRO] Já existe outra categoria com esse nome!");
            return null;
        }

        this.nomeCategoria = novoNome;

        if (dao.atualizar(this)) {
            System.out.println("Categoria atualizada para: " + novoNome);
            return this;
        }

        System.out.println("[ERRO] Falha ao atualizar categoria.");
        return null;
    }

    public boolean desativarCategoria() {

        if (!this.status) {
            System.out.println("[ERRO] Categoria já está desativada.");
            return false;
        }

        this.status = false;

        if (new CategoriaDAO().atualizar(this)) {
            System.out.println("Categoria desativada: " + nomeCategoria);
            return true;
        }

        System.out.println("[ERRO] Falha ao desativar categoria.");
        return false;
    }

    public void visualizarCategoria() {
        System.out.println("\n--- Categoria ---");
        System.out.println("ID: " + idCategoria);
        System.out.println("Nome: " + nomeCategoria);
        System.out.println("Status: " + (status ? "ATIVA" : "INATIVA"));
    }

    public String getIdCategoria() { return idCategoria; }
    public String getNomeCategoria() { return nomeCategoria; }
    public Boolean getStatus() { return status; }
}