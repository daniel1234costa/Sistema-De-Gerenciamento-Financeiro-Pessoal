package model;
import java.util.Date;

public class Despesa {
    private String idDespesa;
    private String nomeDespesa;
    private double valor;
    private Date data;
    private Categoria categoria;

    public Despesa(String idDespesa, String nomeDespesa, double valor, Date data, Categoria categoria) {
        this.idDespesa = idDespesa;
        this.nomeDespesa = nomeDespesa;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
    }

    public Despesa() {
    }

    public String getIdDespesa() {
        return idDespesa;
    }

    public void setIdDespesa(String idDespesa) {
        this.idDespesa = idDespesa;
    }

    public String getNomeDespesa() {
        return nomeDespesa; 
    }

    public void setNomeDespesa(String nomeDespesa) {
        this.nomeDespesa = nomeDespesa;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    
    public Date getData() {
        return data;
    }
    
    public void setData(Date data) {
        this.data = data;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void visualizar(){
        System.out.println("ID da Despesa: " +this.idDespesa);
        System.out.println("Nome da Despesa: " +this.nomeDespesa);
        System.out.println("Valor da Despesa: " +this.valor);
        System.out.println("Data da Despesa: " +this.data);
        System.out.println("Categoria da Despesa: " +categoria.getNomeCategoria());
    }

    

}
