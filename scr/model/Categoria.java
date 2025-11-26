package model;

import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private String idCategoria;
    private String nomeCategoria;

    private static List<Categoria> desativadas = new ArrayList<>();

    // --- Construtor Vazio (Boas práticas) ---
    public Categoria() {}

    // --- Construtor Completo ---
    public Categoria(String idCategoria, String nomeCategoria) {
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
    }

    // --- Getters e Setters ---
    public String getIdCategoria() { 
        return idCategoria; 
    }

    public void setIdCategoria(String idCategoria) { 
        this.idCategoria = idCategoria; 
    }

    public String getNomeCategoria() { 
        return nomeCategoria; 
    }

    public void setNomeCategoria(String nomeCategoria) { 
        this.nomeCategoria = nomeCategoria; 
    }

    // --- Cadastrar Categoria ---
    public static Categoria cadastrarCategoria(String idCategoria, String nome) {
        return new Categoria(idCategoria, nome);
    }

    // --- Editar Categoria ---
    public boolean editarCategoria(String nome) {
        this.nomeCategoria = nome;
        return true;
    }

    // --- Desativar Categoria ---
    public boolean desativarCategoria() {
        if (!desativadas.contains(this)) {
            desativadas.add(this);
            return true;
        }
        return false;
    }

    // --- Visualizar Categoria ---
    public void visualizarCategoria() {
        System.out.println(this);
    }

    // --- Métodos para retornar as listas ---
    public static List<Categoria> getDesativadas() {
        return desativadas;
    }

    // --- Listar Categorias ---
    public static void listarCategorias(List<Categoria> categorias) {
        for (Categoria c : categorias) {
            System.out.println(c);
        }
    }
    
    // --- Buscar Categoria por ID ---
    public static Categoria buscarCategoria(List<Categoria> categorias, String idCategoria) {
        for (Categoria c : categorias) {
            if (c.getIdCategoria().equals(idCategoria)) {
                return c;
            }
        }
        return null;
    }

    // --- toString para exibir informações da Categoria ---
    @Override
    public String toString() {
        String status = desativadas.contains(this) ? "Desativada" : "Ativa";
        return "\nCategoria: \nId = " + idCategoria + 
               "\nNome = " + nomeCategoria + 
               "\nStatus = " + status + "\n";
    }
}