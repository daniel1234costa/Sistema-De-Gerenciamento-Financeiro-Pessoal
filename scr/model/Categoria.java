package model;

public class Categoria {
    private String idCategoria;
    private String nomeCategoria;
    private boolean status;

    public Categoria() {}

    public Categoria(String idCategoria, String nomeCategoria, boolean status) {
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
        this.status = status;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public String getStatus() {
        return status ? "Ativo" : "Desativado";
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}