    import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Renda{
    private String idRenda;
    private String nomeRenda;
    private double valor;
    private Date data;
    private boolean tipoRenda;

    // --- Construtor Vazio (Boas práticas) ---
    public Renda() {
    }

    // --- Construtor Completo (Para preencher tudo de uma vez) ---
    public Renda(String idRenda, String nomeRenda, double valor, Date data, boolean tipoRenda) {
        this.idRenda = idRenda;
        this.nomeRenda = nomeRenda;
        this.valor = valor;
        this.data = data;
        this.tipoRenda = tipoRenda;
    }

    // Getters e Setters para manipular os dados.

    public String getIdRenda() {
        return idRenda;
    }

    public void setIdRenda(String idRenda) {
        this.idRenda = idRenda;
    }

    public String getNomeRenda() {
        return nomeRenda;
    }

    public void setNomeRenda(String nomeRenda) {
        this.nomeRenda = nomeRenda;
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

    // DICA: Para booleanos, o padrão Java costuma usar "is" no lugar de "get"
    public boolean isTipoRenda() {
        return tipoRenda;
    }

    public void setTipoRenda(boolean tipoRenda) {
        this.tipoRenda = tipoRenda;
    }

}