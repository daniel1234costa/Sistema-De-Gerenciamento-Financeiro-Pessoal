import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Renda{
    private static List<Renda> bancoDeDadosRenda = new ArrayList<>();
    private String idRenda;
    private String nomeRenda;
    private double valor;
    private Date data;
    private boolean tipoRenda;
    private static int contador = 0;

    
    public Renda() {
    }


    public Renda(String idRenda, String nomeRenda, double valor, Date data, boolean tipoRenda) {
        this.idRenda = idRenda;
        this.nomeRenda = nomeRenda;
        this.valor = valor;
        this.data = data;
        this.tipoRenda = tipoRenda;
    }

  

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

  
    public boolean isTipoRenda() {
        return tipoRenda;
    }

    public void setTipoRenda(boolean tipoRenda) {
        this.tipoRenda = tipoRenda;
    }

    public static Renda cadastrarRenda(String nome, double valor, Date data, boolean tipoRenda) {

    contador++; 

    String novoId = "R" + contador; 

    return new Renda(novoId, nome, valor, data, tipoRenda);
    }

    public void editarRenda(String nome, double valor) {
    
        this.nomeRenda = nome;
        this.valor = valor;
        
}

public static boolean excluirRenda(Renda renda) {
    return bancoDeDadosRenda.remove(renda);
}




    }


