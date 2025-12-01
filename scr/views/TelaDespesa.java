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
//import dao.CategoriaDAO;

public class TelaDespesa {

    // Scanner próprio conforme o padrão da sua TelaUsuario
    private final Scanner scanner = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void exibirMenu() {
        // Verifica se tem alguém logado antes de abrir o menu
        if (!Sessao.isLogado()) {
            System.out.println("\n⚠️  ACESSO NEGADO: Você precisa fazer LOGIN no 'Módulo de Usuários' primeiro!");
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
                     //   listarDespesas();
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
        Date data = lerData();
        if (data == null) return;

        // Pega o ID do usuário da sessão global
        String idUsuario = Sessao.getIdUsuarioLogado();

        Despesa novaDespesa = new Despesa(idUsuario, descricao, valor, data, idCategoria);

        if (Despesa.cadastrarDespesa(novaDespesa)) {
            // Sucesso (DAO já imprime mensagem)
        } else {
            System.err.println("Erro ao salvar despesa.");
        }
    }

   /*  private void listarDespesas() {
        System.out.println("\n--- MINHAS DESPESAS ---");
        String idUsuario = Sessao.getIdUsuarioLogado();
        
        List<Despesa> lista = Despesa.listarDespesas(idUsuario);

        if (lista.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada.");
        } else {
         //   CategoriaDAO catDao = new CategoriaDAO();
            double total = 0;

            System.out.printf("%-36s | %-15s | %-15s | %-10s | %-12s%n", "ID", "Categoria", "Descrição", "Valor", "Data");
            System.out.println("--------------------------------------------------------------------------------------------------");
            
            for (Despesa d : lista) {
                total += d.getValor(); */
                
                // Busca nome da categoria para exibir bonito
                String nomeCategoria = "N/D";
              //  Categoria c = catDao.buscarPorId(d.getIdCategoria());
             //   if (c != null) nomeCategoria = c.getNome();

             //   System.out.printf("%-36s | %-15s | %-15s | R$ %-7.2f | %-12s%n", 
               //     d.getIdDespesa(), 
               //     nomeCategoria,
               //     d.getNomeDespesa(), 
               //     d.getValor(), 
              //      dateFormat.format(d.getData()));
          //  }
         //   System.out.println("--------------------------------------------------------------------------------------------------");
         //   System.out.printf("TOTAL GASTO: R$ %.2f%n", total);
       // }
   // }

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

        // Atualiza usando o método do objeto
        despesa.editarDespesa();
    }

    private void excluirDespesa() {
        System.out.print("\nDigite o ID da despesa para excluir: ");
        String id = scanner.nextLine();

        // Verificação de segurança antes de excluir
        DespesaDAO dao = new DespesaDAO();
        Despesa d = dao.buscarPorId(id);

        if (d != null && d.getIdUsuario().equals(Sessao.getIdUsuarioLogado())) {
            System.out.print("Tem certeza que deseja excluir? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                Despesa.excluirDespesa(id);
            }
        } else {
            System.out.println("Despesa não encontrada ou não pertence a você.");
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private String selecionarCategoria() {
      //  CategoriaDAO catDao = new CategoriaDAO();
        List<Categoria> categorias = catDao.listarCategorias(Sessao.getIdUsuarioLogado());

        if (categorias.isEmpty()) {
            System.out.println("⚠️  ATENÇÃO: Você não tem categorias cadastradas!");
            System.out.println("Vá ao 'Módulo de Categorias' e cadastre algumas (Ex: Alimentação, Lazer) antes de lançar despesas.");
            return null;
        }

        System.out.println("\n--- CATEGORIAS DISPONÍVEIS ---");
        for (Categoria c : categorias) {
            System.out.println("ID: " + c.getIdCategoria() + " | Nome: " + c.getNome());
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
        try {
            return dateFormat.parse(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Data inválida (use dd/MM/yyyy).");
            return null;
        }
    }
}