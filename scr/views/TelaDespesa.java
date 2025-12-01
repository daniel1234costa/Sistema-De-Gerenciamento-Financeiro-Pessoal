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
            System.out.println("2. Listar TODAS Minhas Despesas");
            System.out.println("3. Listar Despesas por Categoria "); 
            System.out.println("4. Calcular Total Mensal "); 
            System.out.println("5. Buscar Despesa por Posição"); 
            System.out.println("6. Editar Despesa");
            System.out.println("7. Excluir Despesa");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine(); 
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
                        listarTodasDespesas(); 
                        break;
                    case 3:
                        listarDespesasPorCategoria(); 
                        break;
                    case 4:
                        calcularTotalMensal();
                        break;
                    case 5:
                        buscarDespesaPorId();
                        break;
                    case 6:
                        editarDespesa(); 
                        break;
                    case 7:
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
            }
        }
    }

    private void cadastrarDespesa() {
        System.out.println("\n--- CADASTRAR DESPESA ---");

        String idCategoria = selecionarCategoria();
        if (idCategoria == null) return; 

        System.out.print("Descrição da Despesa: ");
        String descricao = scanner.nextLine();

        System.out.print("Valor (R$): ");
        double valor = lerDouble();
        if (valor == -1) return;

        System.out.print("Data (dd/MM/yyyy): ");
        Date data = lerData();
        if (data == null) return;

        String idUsuario = Sessao.getIdUsuarioLogado();

        Despesa novaDespesa = new Despesa(idUsuario, descricao, valor, data, idCategoria);

        if (Despesa.cadastrarDespesa(novaDespesa)) {
            System.out.println("Despesa cadastrada com sucesso!");
        } else {
            System.err.println("Erro ao salvar despesa.");
        }
    }

    private void listarTodasDespesas() {
        System.out.println("\n--- MINHAS DESPESAS (Todas) ---");
        String idUsuario = Sessao.getIdUsuarioLogado();
        
        List<Despesa> lista = Despesa.listarDespesas(idUsuario); 

        if (lista.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada.");
        } else {
            CategoriaDAO catDao = new CategoriaDAO();
            double total = 0;

            System.out.printf("%-8s | %-15s | %-15s | %-10s | %-12s%n", "Posição", "Categoria", "Descrição", "Valor", "Data");
            System.out.println("--------------------------------------------------------------------------------------------------");
            
            int cont = 0;
            for (Despesa d : lista) {
                total += d.getValor(); 
                
                String nomeCategoria = "N/D";
                Categoria c = catDao.buscarCategoria(d.getIdCategoria());
                if (c != null) nomeCategoria = c.getNomeCategoria();

                System.out.printf("%-8d | %-15s | %-15s | R$ %-7.2f | %-12s%n", 
                    cont, 
                    nomeCategoria,
                    d.getNomeDespesa(), 
                    d.getValor(), 
                    dateFormat.format(d.getData()));
                cont++;
            }
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.printf("TOTAL GASTO: R$ %.2f%n", total);
        }
    }
    

    private void listarDespesasPorCategoria() {
        System.out.println("\n--- LISTAR POR CATEGORIA ---");
        
        String idCategoria = selecionarCategoria();
        if (idCategoria == null) return;
        
        List<Despesa> lista = Despesa.listarDespesasPorCategoria(idCategoria); 

        if (lista.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada para esta categoria.");
        } else {
            CategoriaDAO catDao = new CategoriaDAO();
            String nomeCategoria = catDao.buscarCategoria(idCategoria) != null ? catDao.buscarCategoria(idCategoria).getNomeCategoria() : idCategoria;

            System.out.printf("%n--- DESPESAS NA CATEGORIA: %s ---%n", nomeCategoria);
            System.out.printf("%-8s | %-15s | %-10s | %-12s%n", "Posição", "Descrição", "Valor", "Data");
            System.out.println("------------------------------------------------------------------");
            double total = 0;
            int cont = 0;
            for (Despesa d : lista) {
                total += d.getValor(); 
                System.out.printf("%-8d | %-15s | R$ %-7.2f | %-12s%n", 
                    cont, 
                    d.getNomeDespesa(), 
                    d.getValor(), 
                    dateFormat.format(d.getData()));
                cont++;
            }
            System.out.println("------------------------------------------------------------------");
            System.out.printf("TOTAL GASTO NESSA CATEGORIA: R$ %.2f%n", total);
        }
    }

   
    private String selecionarDespesaPorPosicao() {
        String idUsuario = Sessao.getIdUsuarioLogado();
        List<Despesa> despesas = Despesa.listarDespesas(idUsuario); 

        if (despesas.isEmpty()) {
            System.out.println(" Nenhuma despesa encontrada para selecionar.");
            return null;
        }

        System.out.println("\n--- SELECIONAR DESPESA ---");
        
        CategoriaDAO catDao = new CategoriaDAO();
        int cont = 0;
        
        System.out.printf("%-8s | %-15s | %-15s | %-10s%n", "Posição", "Descrição", "Categoria", "Valor");
        System.out.println("------------------------------------------------------------------");
        
        for (Despesa d : despesas) {
            String nomeCategoria = catDao.buscarCategoria(d.getIdCategoria()) != null 
                                   ? catDao.buscarCategoria(d.getIdCategoria()).getNomeCategoria() 
                                   : "N/D";
            
            System.out.printf("%-8d | %-15s | %-15s | R$ %-7.2f%n", 
                cont, 
                d.getNomeDespesa(), 
                nomeCategoria,
                d.getValor());
            cont++;
        }

        System.out.print("Informe a posição (número) da despesa desejada: ");
        
        int posicao = lerInt(); 

        if (posicao >= 0 && posicao < despesas.size()) {
            return despesas.get(posicao).getIdDespesa(); 
        } else {
            System.out.println("Posição inválida. Operação cancelada.");
            return null;
        }
    }

  
    private void buscarDespesaPorId() {
        System.out.println("\n--- BUSCAR DESPESA ---");
        
        String id = selecionarDespesaPorPosicao(); 
        if (id == null) return;

        Despesa d = Despesa.buscarDespesaPorId(id); 

        if (d == null) {
            System.out.println("Despesa não encontrada.");
            return;
        }
        
        System.out.println("\n--- DETALHES DA DESPESA ---");
        System.out.println("ID: " + d.getIdDespesa());
        System.out.println("Nome: " + d.getNomeDespesa());
        System.out.println("Valor: R$ " + String.format("%.2f", d.getValor()));
        System.out.println("Data: " + dateFormat.format(d.getData()));
        
        
        Categoria categoria = new CategoriaDAO().buscarCategoria(d.getIdCategoria());
        if (categoria != null) {
            System.out.println("Categoria: " + categoria.getNomeCategoria());
        } else {
             System.out.println("Categoria: " + d.getIdCategoria() + " (N/D)");
        }
    }

    
    private void calcularTotalMensal() {
        System.out.println("\n--- CÁLCULO DE DESPESA MENSAL ---");
        
        System.out.print("Digite o Mês (1 a 12): ");
        int mes = lerInt();
        if (mes == -1 || mes < 1 || mes > 12) { return; }
        
        System.out.print("Digite o Ano (Ex: 2025): ");
        int ano = lerInt();
        if (ano == -1 || ano < 1900) { return; }

        
        String idUsuario = Sessao.getIdUsuarioLogado();
        
        double total = Despesa.calcularDespesaTotalMensal(mes, ano, idUsuario); 

        System.out.printf("\n Total de Despesas em %02d/%d: R$ %.2f%n", mes, ano, total);
    }


    private void editarDespesa() {
        System.out.println("\n--- EDITAR DESPESA ---");
        
        String id = selecionarDespesaPorPosicao(); 
        if (id == null) return;

        Despesa despesa = Despesa.buscarDespesaPorId(id);

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

        despesa.editarDespesa(); 
        System.out.println("Despesa atualizada com sucesso!");
    }


    private void excluirDespesa() {
        System.out.println("\n--- EXCLUIR DESPESA ---");
        
        String id = selecionarDespesaPorPosicao(); 
        if (id == null) return;

        Despesa d = Despesa.buscarDespesaPorId(id);

        if (d != null && d.getIdUsuario().equals(Sessao.getIdUsuarioLogado())) {
            System.out.print("Tem certeza que deseja excluir a despesa '" + d.getNomeDespesa() + "'? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                if (Despesa.excluirDespesa(id)) {
                     System.out.println("Despesa excluída com sucesso!");
                } else {
                     System.out.println("Falha ao excluir a despesa.");
                }
            } else {
                 System.out.println("Operação de exclusão cancelada.");
            }
        } else {
            System.out.println("Despesa não encontrada ou não pertence a você.");
        }
    }


    private String selecionarCategoria() {
        CategoriaDAO catDao = new CategoriaDAO(); 
        List<Categoria> categorias = catDao.listarCategorias(); 

        if (categorias.isEmpty()) {
            System.out.println(" ATENÇÃO: Não há categorias cadastradas no sistema!");
            System.out.println("Vá ao 'Módulo de Categorias' e cadastre algumas antes de lançar despesas.");
            return null;
        }

        System.out.println("\n--- CATEGORIAS DISPONÍVEIS ---");
        int cont = 0;
        for (Categoria c : categorias) {
            
            System.out.println("Posição: "+cont+" - ID: " + c.getIdCategoria() + " | Nome: " + c.getNomeCategoria());
            cont++;
        }
        System.out.print("Informe a posição da categoria: ");
        
        int pos = lerInt();
        if (pos >= 0 && pos < categorias.size()) {
            return categorias.get(pos).getIdCategoria();
        } else {
            System.out.println("Posição inválida. Seleção de categoria cancelada.");
            return null;
        }
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

    private int lerInt() {
        try {
             int num = Integer.parseInt(scanner.nextLine().trim());
             return num;
         } catch (NumberFormatException e) {
             System.out.println("Entrada inválida. Digite um número inteiro.");
             return -1;
         }
    }

    
    private Date lerData() {
        
        String entrada = scanner.nextLine().trim();

        try {
            return dateFormat.parse(entrada);
        } catch (ParseException e) {
            System.out.println("Data inválida. Use o formato dd/MM/yyyy (Ex: 25/11/2025).");
            return null;
        }
    }
}