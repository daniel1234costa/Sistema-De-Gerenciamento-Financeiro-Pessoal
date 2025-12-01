package views;

import java.util.Scanner;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import model.Despesa;
import model.Categoria;
import model.Sessao;
import dao.DespesaDAO;
import dao.CategoriaDAO;

public class TelaDespesa {

    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void exibirMenu() {
        if (!Sessao.isLogado()) {
            System.out.println("\n ACESSO NEGADO: Você precisa fazer LOGIN no 'Módulo de Usuários' primeiro!");
            return;
        }

        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--- GESTÃO DE DESPESAS ---");
            System.out.println("1. Cadastrar Nova Despesa");
            System.out.println("2. Listar Minhas Despesas");
            System.out.println("3. Editar Despesa");
            System.out.println("4. Excluir Despesa");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir quebra de linha
            } else {
                System.out.println("Opção inválida. Digite um número.");
                scanner.nextLine();
                continue;
            }

            try {
                switch (opcao) {
                    case 1:
                        cadastrarDespesa();
                        break;
                    case 2:
                        listarDespesas();
                        break;
                    case 3:
                        editarDespesa();
                        break;
                    case 4:
                        excluirDespesa();
                        break;
                    case 0:
                        System.out.println("Voltando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.err.println("Erro na operação: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void cadastrarDespesa() {
        System.out.println("\n--- CADASTRAR DESPESA ---");

        // 1. Selecionar Categoria (Obrigatório)
        String idCategoria = selecionarCategoria();
        if (idCategoria == null) return; // Cancela se não tiver categoria

        System.out.print("Descrição da Despesa: ");
        String descricao = scanner.nextLine();

        System.out.print("Valor (R$): ");
        double valor = lerDouble();
        if (valor == -1) return;

        System.out.print("Data (dd/MM/yyyy): ");
        Date data = lerData(); // Aqui chamamos o método corrigido
        if (data == null) return;

        String idUsuario = Sessao.getIdUsuarioLogado();

        Despesa novaDespesa = new Despesa(idUsuario, descricao, valor, data, idCategoria);

        if (new DespesaDAO().cadastrarDespesa(novaDespesa)) {
            // Sucesso
        } else {
            System.err.println("Erro ao salvar despesa.");
        }
    }

    private void listarDespesas() {
        System.out.println("\n--- MINHAS DESPESAS ---");
        String idUsuario = Sessao.getIdUsuarioLogado();
        
        DespesaDAO despesaDao = new DespesaDAO();
        List<Despesa> lista = despesaDao.listarDespesas(idUsuario);

        if (lista.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada.");
        } else {
            CategoriaDAO catDao = new CategoriaDAO();
            double total = 0;

            System.out.printf("%-36s | %-15s | %-15s | %-10s | %-12s%n", "ID", "Categoria", "Descrição", "Valor", "Data");
            System.out.println("--------------------------------------------------------------------------------------------------");
            
            for (Despesa d : lista) {
                total += d.getValor(); 
                
                String nomeCategoria = "N/D";
                Categoria c = catDao.buscarCategoria(d.getIdCategoria());
                if (c != null) nomeCategoria = c.getNomeCategoria();

                System.out.printf("%-36s | %-15s | %-15s | R$ %-7.2f | %-12s%n", 
                    d.getIdDespesa(), 
                    nomeCategoria,
                    d.getNomeDespesa(), 
                    d.getValor(), 
                    dateFormat.format(d.getData()));
            }
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.printf("TOTAL GASTO: R$ %.2f%n", total);
        }
    }

    private void editarDespesa() {
        System.out.print("\nDigite o ID da despesa para editar: ");
        String id = scanner.nextLine();

        DespesaDAO dao = new DespesaDAO();
        Despesa despesa = dao.buscarPorId(id);

        if (despesa == null) {
            System.out.println("Despesa não encontrada.");
            return;
        }

        if (!despesa.getIdUsuario().equals(Sessao.getIdUsuarioLogado())) {
            System.out.println("Acesso negado.");
            return;
        }

        System.out.println("--- EDITANDO (Deixe vazio para manter o atual) ---");

        System.out.print("Nova Descrição [" + despesa.getNomeDespesa() + "]: ");
        String desc = scanner.nextLine();
        if (!desc.isEmpty()) despesa.setNomeDespesa(desc);

        System.out.print("Novo Valor [" + despesa.getValor() + "]: ");
        String valStr = scanner.nextLine();
        if (!valStr.isEmpty()) {
            try {
                despesa.setValor(Double.parseDouble(valStr.replace(",", ".")));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Mantendo anterior.");
            }
        }

        System.out.print("Alterar Categoria? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            String novaCat = selecionarCategoria();
            if (novaCat != null) despesa.setIdCategoria(novaCat);
        }

        dao.editarDespesa(despesa);
    }

    private void excluirDespesa() {
        System.out.print("\nDigite o ID da despesa para excluir: ");
        String id = scanner.nextLine();

        DespesaDAO dao = new DespesaDAO();
        Despesa d = dao.buscarPorId(id);

        if (d != null && d.getIdUsuario().equals(Sessao.getIdUsuarioLogado())) {
            System.out.print("Tem certeza que deseja excluir? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                dao.excluirDespesa(id);
            }
        } else {
            System.out.println("Despesa não encontrada ou não pertence a você.");
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private String selecionarCategoria() {
        CategoriaDAO catDao = new CategoriaDAO(); 
        List<Categoria> categorias = catDao.listarCategorias(); 

        if (categorias.isEmpty()) {
            System.out.println(" ATENÇÃO: Não há categorias cadastradas no sistema!");
            System.out.println("Vá ao 'Módulo de Categorias' e cadastre algumas antes de lançar despesas.");
            return null;
        }

        System.out.println("\n--- CATEGORIAS DISPONÍVEIS ---");
        for (Categoria c : categorias) {
            System.out.println("ID: " + c.getIdCategoria() + " | Nome: " + c.getNomeCategoria());
        }
        System.out.print("Copie e cole o ID da categoria desejada: ");
        return scanner.nextLine();
    }

    private double lerDouble() {
        try {
            String linha = scanner.nextLine();
            return Double.parseDouble(linha.replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido.");
            return -1;
        }
    }

  
    private Date lerData() {
       
        String entrada = scanner.nextLine();

       
        if (entrada.trim().isEmpty()) {
            entrada = scanner.nextLine();
        }

        try {
            return dateFormat.parse(entrada);
        } catch (ParseException e) {
            System.out.println("❌ Data inválida. Use o formato dd/MM/yyyy (Ex: 25/11/2025).");
            return null;
        }
    }
}