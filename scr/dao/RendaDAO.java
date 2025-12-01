package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.DatabaseConnector;
import model.Renda;
import model.UtilData;

public class RendaDAO {

   public static Renda cadastrarRenda(String nome, double valor, java.util.Date data, boolean tipoRenda) {
        Renda novaRenda = new Renda(nome, valor, data, tipoRenda);
        
        // 1. GERA O UUID (String) AQUI
        String novoId = java.util.UUID.randomUUID().toString();
        novaRenda.setIdRenda(novoId);

        // SQL ajustado: enviamos o ID manualmente agora
        String sql = "INSERT INTO renda (id, nome, valor, data, tipo, id_usuario) VALUES (?, ?, ?, ?, ?, '1')";

        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novaRenda.getIdRenda()); // Envia o UUID gerado
            stmt.setString(2, novaRenda.getNomeRenda());
            stmt.setDouble(3, novaRenda.getValor());
            stmt.setString(4, UtilData.formatarData(novaRenda.getData()));
            stmt.setBoolean(5, novaRenda.isTipoRenda());

            stmt.execute();
            System.out.println("✅ Renda cadastrada! ID: " + novaRenda.getIdRenda());

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
            return null;
        }
        return novaRenda;
    }

    public boolean excluirRenda(Renda renda) {
        String sql = "DELETE FROM renda WHERE id = ?";
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, renda.getIdRenda());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir: " + e.getMessage());
            return false;
        }
    }

   public void editarRenda(String id, String nome, double valor) {
        String sql = "UPDATE renda SET nome = ?, valor = ? WHERE id = ?";
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setDouble(2, valor);
            stmt.setString(3, id); // MUDANÇA: setString

            if (stmt.executeUpdate() > 0) System.out.println("✅ Renda atualizada!");
            else System.out.println("⚠️ ID não encontrado.");

        } catch (SQLException e) { System.out.println("Erro editar: " + e.getMessage()); }
    }

    public void visualizarRenda(String id) {
        String sql = "SELECT * FROM renda WHERE id = ?"; // Nome da coluna é 'id' no banco novo
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // MUDANÇA: setString em vez de setInt
            stmt.setString(1, id); 
            
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("--- DETALHES ---");
                System.out.println("ID: " + rs.getString("id")); // getString
                System.out.println("Nome: " + rs.getString("nome"));
                System.out.println("Valor: R$ " + rs.getDouble("valor"));
                System.out.println("Data: " + rs.getString("data"));
                System.out.println("Tipo: " + (rs.getBoolean("tipo") ? "Fixa" : "Extra"));
            } else {
                System.out.println("Renda não encontrada no banco.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao visualizar: " + e.getMessage());
        }
    }
    public List<Renda> listarRendasExtras() {
        return listarGenerico(0); // 0 = Extra
    }

    public List<Renda> listarRendasFixas() {
        return listarGenerico(1); // 1 = Fixa
    }

  private List<Renda> listarGenerico(int tipo) {
        List<Renda> lista = new ArrayList<>();
        String sql = "SELECT * FROM renda WHERE tipo = ?";
        
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 1. Preenche o ? com o tipo (0 ou 1)
            stmt.setInt(1, tipo); 
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Renda r = new Renda();
                // 2. CORREÇÃO: Pega String direto (pois é UUID agora)
                r.setIdRenda(rs.getString("id")); 
                
                r.setNomeRenda(rs.getString("nome"));
                r.setValor(rs.getDouble("valor"));
                r.setData(UtilData.parseData(rs.getString("data")));
                r.setTipoRenda(rs.getBoolean("tipo"));
                
                lista.add(r);
            }
        } catch (SQLException e) { 
            System.out.println("Erro listar: " + e.getMessage()); 
        }
        return lista;
    }
    public double calcularRendaTotalMensal(int mes, int ano) {
        double total = 0;
        String sql = "SELECT SUM(valor) as total FROM renda WHERE substr(data, 4, 2) = ? AND substr(data, 7, 4) = ?";
        try (Connection conn = DatabaseConnector.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.format("%02d", mes));
            stmt.setString(2, String.valueOf(ano));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) total = rs.getDouble("total");
        } catch (SQLException e) { System.out.println("Erro calc: " + e.getMessage()); }
        return total;
    }
}