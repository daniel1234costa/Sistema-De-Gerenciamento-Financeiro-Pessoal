package controller;

import java.util.List;
import model.Categoria;
import model.CategoriaDAO;

public class CategoriaController {

    private final CategoriaDAO dao = new CategoriaDAO();

    //Cadastrar categoria
    public void cadastrarCategoria(String nomeCategoria) {

        if (nomeCategoria == null || nomeCategoria.trim().isEmpty()) {
            System.out.println("Erro: O nome da categoria é obrigatório.");
            return;
        }

        dao.cadastrarCategoria(nomeCategoria);

        System.out.println("Sucesso: Categoria " + nomeCategoria + " cadastrada.");
    }

    //Editar categoria
    public void editarCategoria(String idCategoria, String novoNome) {

        if (novoNome == null || novoNome.trim().isEmpty()) {
            System.out.println("Erro: O nome da categoria é obrigatório.");
            return;
        }

        dao.editarCategoria(idCategoria, novoNome);
    }

    //Desativar categoria
    public void desativarCategoria(String idCategoria) {
        dao.desativarCategoria(idCategoria);
    }

    //Visualizar categoria
    public void visualizarCategoria(String idCategoria) {
        dao.visualizarCategoria(idCategoria);
    }

    //Listar categorias
    public List<Categoria> listarCategorias() {
        return dao.listarCategorias();
    }

    //Buscar categoria por nome ou ID
    public Categoria buscarCategoria(String termo) {
        return dao.buscarCategoria(termo);
    }
}