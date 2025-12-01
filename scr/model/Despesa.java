package model;

import java.util.Date;
import java.util.List;
import dao.DespesaDAO;

public class Despesa {

    private String idDespesa;
    private String nomeDespesa; 
    private double valor;
    private Date data;          
    private String idUsuario; 
    private String idCategoria;

   
    public Despesa() {
    }

    public Despesa(String idUsuario, String nomeDespesa, double valor, Date data, String idCategoria) {
        this.idUsuario = idUsuario;
        this.nomeDespesa = nomeDespesa;
        this.valor = valor;
        this.data = data;
        this.idCategoria = idCategoria;
    }

   
    public static boolean cadastrarDespesa(Despesa despesa) {
        DespesaDAO dao = new DespesaDAO();
        return dao.cadastrarDespesa(despesa);
    }

    // Excluir (Static no diagrama)
    public static boolean excluirDespesa(String idDespesa) {
        DespesaDAO dao = new DespesaDAO();
        return dao.excluirDespesa(idDespesa);
    }

    // Listar (Static no diagrama) - Adicionamos idUsuario para filtrar
    public static List<Despesa> listarDespesas(String idUsuario) {
        DespesaDAO dao = new DespesaDAO();
        return dao.listarDespesas(idUsuario);
    }

    public static List<Despesa> listarDespesasPorPeriodo(Date inicio, Date fim, String idUsuario) {
        DespesaDAO dao = new DespesaDAO();
        return dao.listarDespesasPorPeriodo(idUsuario, inicio, fim);
    }

   
    public static double calcularDespesaTotalMensal(int mes, int ano, String idUsuario) {
        DespesaDAO dao = new DespesaDAO();
        return dao.calcularDespesaTotalMensal(mes, ano, idUsuario);
    }

   
    public void editarDespesa() {
        DespesaDAO dao = new DespesaDAO();
        // O DAO pega o ID deste objeto e atualiza no banco
        dao.editarDespesa(this);
    }

    // Visualizar (Usa o ID do próprio objeto)
    public void visualizarDespesa() {
        DespesaDAO dao = new DespesaDAO();
        dao.visualizarDespesa(this.idDespesa);
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

    public String getIdUsuario() {
        return idUsuario; 
        }
    public void setIdUsuario(String idUsuario) { 
        this.idUsuario = idUsuario; 
        }

    public String getIdCategoria() { 
        return idCategoria; 
        }
    public void setIdCategoria(String idCategoria) { 
        this.idCategoria = idCategoria; 
        }
}