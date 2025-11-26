package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RendaDAO {

   public Renda cadastrarRenda(String nome, double valor, java.util.Date data, boolean tipoRenda) {
    Renda novaRenda = new Renda(nome, valor, data, tipoRenda);
    String sql = "INSERT INTO renda (nome, valor, data, tipo) VALUES (?, ?, ?, ?)";

    try (Connection conn = DatabaseConnector.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, novaRenda.getNomeRenda());
        stmt.setDouble(2, novaRenda.getValor());
        stmt.setString(3, UtilData.formatarData(novaRenda.getData()));
        stmt.setBoolean(4, novaRenda.isTipoRenda());

        stmt.execute();
        System.out.println("Renda cadastrada com sucesso no SQLite!");

    } catch (SQLException e) {
        System.out.println("Erro ao cadastrar renda: " + e.getMessage());
    }

    return novaRenda;

    }

    public void editarRenda(String id, String nome, double valor) {
    String sql = "UPDATE renda SET nome = ?, valor = ? WHERE id = ?";
    
    try (Connection conn = DatabaseConnector.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, nome);
        stmt.setDouble(2, valor);
        stmt.setInt(3, Integer.parseInt(id));

        int linhasAfetadas = stmt.executeUpdate();
        
        if (linhasAfetadas > 0) {
            System.out.println("Renda atualizada com sucesso!");
        } else {
            System.out.println("Nenhuma renda encontrada com esse ID.");
        }

    } catch (SQLException e) {
        System.out.println("Erro ao editar renda: " + e.getMessage());
    }
}

   public void excluirRenda(Renda renda) {
    String sql = "DELETE FROM renda WHERE id = ?";
    
    try (Connection conn = DatabaseConnector.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, Integer.parseInt(renda.getIdRenda()));

        int linhasAfetadas = stmt.executeUpdate();
        
        if (linhasAfetadas > 0) {
            System.out.println("Renda excluída com sucesso!");
        } else {
            System.out.println("Nenhuma renda encontrada com esse ID.");
        }

    } catch (SQLException e) {
        System.out.println("Erro ao excluir renda: " + e.getMessage());
    }
}

    public void visualizarRenda(String id) {
    String sql = "SELECT * FROM renda WHERE id = ?";

    try (Connection conn = DatabaseConnector.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, Integer.parseInt(id));
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            System.out.println("--- DETALHES DA RENDA ---");
            System.out.println("ID: " + rs.getInt("id"));
            System.out.println("Nome: " + rs.getString("nome"));
            System.out.println("Valor: " + rs.getDouble("valor"));
            System.out.println("Data: " + rs.getString("data"));
            System.out.println("Tipo: " + (rs.getBoolean("tipo") ? "Fixa" : "Extra"));
        } else {
            System.out.println("Nenhuma renda encontrada com esse ID.");
        }
        rs.close();

    } catch (SQLException e) {
        System.out.println("Erro ao visualizar renda: " + e.getMessage());
    }
}

   public List<Renda> listarRendasExtras() {
    List<Renda> rendas = new ArrayList<>();
    String sql = "SELECT * FROM renda WHERE tipo = 0"; 

    try (Connection conn = DatabaseConnector.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Renda r = new Renda();
            r.setIdRenda(String.valueOf(rs.getInt("id")));
            r.setNomeRenda(rs.getString("nome"));
            r.setValor(rs.getDouble("valor"));
            r.setData(UtilData.parseData(rs.getString("data")));
            r.setTipoRenda(rs.getBoolean("tipo"));
            
            rendas.add(r);
        }

    } catch (SQLException e) {
        System.out.println("Erro ao listar rendas extras: " + e.getMessage());
    }

    return rendas;
}

    public List<Renda> listarRendasFixas() {
    List<Renda> rendas = new ArrayList<>();
    String sql = "SELECT * FROM renda WHERE tipo = 1"; 

    try (Connection conn = DatabaseConnector.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Renda r = new Renda();
            r.setIdRenda(String.valueOf(rs.getInt("id")));
            r.setNomeRenda(rs.getString("nome"));
            r.setValor(rs.getDouble("valor"));
            r.setData(UtilData.parseData(rs.getString("data")));
            r.setTipoRenda(rs.getBoolean("tipo"));
            
            rendas.add(r);
        }

    } catch (SQLException e) {
        System.out.println("Erro ao listar rendas fixas: " + e.getMessage());
    }

    return rendas;
}
    
   public double calcularRendaTotalMensal(int mes, int ano) {
    double total = 0;
    String sql = "SELECT SUM(valor) as total FROM renda WHERE substr(data, 4, 2) = ? AND substr(data, 7, 4) = ?";
    
    try (Connection conn = DatabaseConnector.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        String mesFormatado = String.format("%02d", mes);
        String anoFormatado = String.valueOf(ano);

        stmt.setString(1, mesFormatado);
        stmt.setString(2, anoFormatado);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        }
        
    } catch (SQLException e) {
        System.out.println("Erro ao calcular total: " + e.getMessage());
    }
    
    return total;
}
}