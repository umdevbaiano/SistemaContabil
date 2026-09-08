package br.com.sistemacontabil.principal;

import br.com.sistemacontabil.modelo.Empresa;
import br.com.sistemacontabil.modelo.CalculoTributario;
import br.com.sistemacontabil.modelo.EmpresaSimplesNacional;
import br.com.sistemacontabil.modelo.EmpresaMEI;
import br.com.sistemacontabil.modelo.EmpresaLucroPresumido;
import br.com.sistemacontabil.modelo.EmpresaLucroReal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProgramaPrincipal {

    private static List<Empresa> empresas = new ArrayList<>();
    private static List<String> logAlteracoes = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static DateTimeFormatter formatoLog = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static void main(String[] args) {
        carregarEmpresasIniciais();

        int opcao = -1;
        while (opcao != 0) {
            exibirMenu();
            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\nEntrada invalida. Digite um numero.\n");
                continue;
            }

            System.out.println();

            switch (opcao) {
                case 1:
                    cadastrarEmpresa();
                    break;
                case 2:
                    listarEmpresas();
                    break;
                case 3:
                    buscarPorCnpj();
                    break;
                case 4:
                    filtrarPorRegime();
                    break;
                case 5:
                    alterarEmpresa();
                    break;
                case 6:
                    excluirEmpresa();
                    break;
                case 7:
                    compararFaturamentos();
                    break;
                case 8:
                    compararTributos();
                    break;
                case 9:
                    executarTestes();
                    break;
                case 10:
                    exibirLog();
                    break;
                case 0:
                    System.out.println("Encerrando o sistema. Ate logo!");
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
                    break;
            }
        }

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("--- SISTEMA CONTABIL ---");
        System.out.println();
        System.out.println(" [1] Cadastrar empresa");
        System.out.println(" [2] Listar empresas");
        System.out.println(" [3] Buscar por CNPJ");
        System.out.println(" [4] Filtrar por regime");
        System.out.println(" [5] Alterar dados de empresa");
        System.out.println(" [6] Excluir empresa");
        System.out.println(" [7] Comparar faturamentos");
        System.out.println(" [8] Comparar tributos");
        System.out.println(" [9] Executar testes de validacao");
        System.out.println("[10] Ver log de alteracoes");
        System.out.println(" [0] Sair");
        System.out.println();
        System.out.print("Escolha uma opcao: ");
    }

    private static void carregarEmpresasIniciais() {
        empresas.add(new EmpresaSimplesNacional(
                "12.345.678/0001-90", "Comércio Rápido Ltda", "Rápido Store",
                "Itabuna", "BA", "Comércio varejista de artigos diversos",
                new BigDecimal("3600000.00"), new BigDecimal("300000.00"),
                new BigDecimal("7.30"), LocalDate.of(2018, 3, 15), 12));

        EmpresaMEI mei = new EmpresaMEI();
        mei.setCnpj("98.765.432/0001-10");
        mei.setRazaoSocial("Maria Luisa Design MEI");
        mei.setNomeFantasia("MS Design");
        mei.setMunicipio("Ilheus");
        mei.setEstado("BA");
        mei.setOcupacaoPrincipal("Designer gráfico independente");
        mei.setFaturamentoAnual(new BigDecimal("72000.00"));
        mei.setFaturamentoMensal(new BigDecimal("6000.00"));
        mei.setValorMensalDAS(new BigDecimal("75.60"));
        mei.setPossuiFuncionario(false);
        mei.setDataAbertura(LocalDate.of(2020, 7, 1));
        empresas.add(mei);

        empresas.add(new EmpresaLucroPresumido(
                "11.222.333/0001-44", "Distribuidora Central Ltda", "Central Dist",
                "Feira de Santana", "BA", "Comércio atacadista de produtos alimentícios",
                new BigDecimal("2000000.00"), new BigDecimal("500000.00"),
                new BigDecimal("8.00"), new BigDecimal("15.00"), new BigDecimal("9.00"),
                new BigDecimal("3250.00"), new BigDecimal("15000.00"),
                LocalDate.of(2015, 1, 10)));

        EmpresaLucroReal lucroReal = new EmpresaLucroReal();
        lucroReal.setCnpj("55.666.777/0001-88");
        lucroReal.setRazaoSocial("Indústria Tecnológica S.A.");
        lucroReal.setNomeFantasia("TechInd");
        lucroReal.setMunicipio("Jequié");
        lucroReal.setEstado("BA");
        lucroReal.setAtividadePrincipal("Desenvolvimento de software e consultoria");
        lucroReal.setReceitaBrutaAnual(new BigDecimal("12000000.00"));
        lucroReal.setDespesasDedutiveis(new BigDecimal("200000.00"));
        lucroReal.setLucroContabil(new BigDecimal("800000.00"));
        lucroReal.setPrejuizoFiscal(new BigDecimal("50000.00"));
        lucroReal.setAliquotaIRPJ(new BigDecimal("15.00"));
        lucroReal.setAliquotaCSLL(new BigDecimal("9.00"));
        lucroReal.setDataInicioAtividade(LocalDate.of(2010, 6, 20));
        empresas.add(lucroReal);

        empresas.add(new EmpresaSimplesNacional(
                "99.888.777/0001-55", "Mega Comércio Ltda", "MegaShop",
                "Rio de Janeiro", "RJ", "Comércio varejista de eletrônicos",
                new BigDecimal("5200000.00"), new BigDecimal("433333.33"),
                new BigDecimal("11.50"), LocalDate.of(2012, 11, 5), 45));

        empresas.add(new EmpresaMEI(
                "44.555.666/0001-22", "Lebron James Serviços MEI", "LJ Serviços",
                "Salvador", "BA", "Encanador independente",
                new BigDecimal("95000.00"), new BigDecimal("7916.67"),
                new BigDecimal("76.60"), true, LocalDate.of(2019, 4, 12)));

        empresas.add(new EmpresaLucroReal(
                "77.888.999/0001-33", "Vetta HUB Startup Inovação Ltda", "InovaStart",
                "Salvador", "BA", "Pesquisa e desenvolvimento tecnológico",
                new BigDecimal("1500000.00"), new BigDecimal("300000.00"),
                new BigDecimal("-150000.00"), null, new BigDecimal("80000.00"),
                new BigDecimal("15.00"), new BigDecimal("9.00"),
                LocalDate.of(2022, 9, 1)));

        EmpresaLucroPresumido presumidoServicos = new EmpresaLucroPresumido();
        presumidoServicos.setCnpj("22.333.444/0001-66");
        presumidoServicos.setRazaoSocial("Consultoria Alpha Ltda");
        presumidoServicos.setNomeFantasia("Alpha Consulting");
        presumidoServicos.setMunicipio("Salvador");
        presumidoServicos.setEstado("BA");
        presumidoServicos.setAtividadePrincipal("Consultoria empresarial");
        presumidoServicos.setReceitaBrutaAnual(new BigDecimal("960000.00"));
        presumidoServicos.setReceitaBrutaTrimestral(new BigDecimal("240000.00"));
        presumidoServicos.setPercentualPresuncao(new BigDecimal("32.00"));
        presumidoServicos.setAliquotaIRPJ(new BigDecimal("15.00"));
        presumidoServicos.setAliquotaCSLL(new BigDecimal("9.00"));
        presumidoServicos.setValorPIS(new BigDecimal("1560.00"));
        presumidoServicos.setValorCOFINS(new BigDecimal("7200.00"));
        presumidoServicos.setDataInicioAtividade(LocalDate.of(2017, 5, 20));
        empresas.add(presumidoServicos);

        logAlteracoes.add("[" + LocalDateTime.now().format(formatoLog) + "] Sistema inicializado com " + empresas.size() + " empresas pre-cadastradas.");
    }

    private static void cadastrarEmpresa() {
        System.out.println("Escolha o regime tributario:");
        System.out.println("[1] Simples Nacional");
        System.out.println("[2] MEI");
        System.out.println("[3] Lucro Presumido");
        System.out.println("[4] Lucro Real");
        System.out.print("Opcao: ");

        int regime;
        try {
            regime = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Opcao invalida.\n");
            return;
        }

        if (regime < 1 || regime > 4) {
            System.out.println("Opcao invalida.\n");
            return;
        }

        try {
            System.out.print("CNPJ: ");
            String cnpj = scanner.nextLine().trim();
            System.out.print("Razao Social: ");
            String razaoSocial = scanner.nextLine().trim();
            System.out.print("Nome Fantasia: ");
            String nomeFantasia = scanner.nextLine().trim();
            System.out.print("Municipio: ");
            String municipio = scanner.nextLine().trim();
            System.out.print("Estado (UF): ");
            String estado = scanner.nextLine().trim();

            Empresa novaEmpresa = null;

            switch (regime) {
                case 1:
                    novaEmpresa = cadastrarSimplesNacional(cnpj, razaoSocial, nomeFantasia, municipio, estado);
                    break;
                case 2:
                    novaEmpresa = cadastrarMEI(cnpj, razaoSocial, nomeFantasia, municipio, estado);
                    break;
                case 3:
                    novaEmpresa = cadastrarLucroPresumido(cnpj, razaoSocial, nomeFantasia, municipio, estado);
                    break;
                case 4:
                    novaEmpresa = cadastrarLucroReal(cnpj, razaoSocial, nomeFantasia, municipio, estado);
                    break;
            }

            if (novaEmpresa != null) {
                empresas.add(novaEmpresa);
                logAlteracoes.add("[" + LocalDateTime.now().format(formatoLog) + "] Empresa cadastrada: " + novaEmpresa.getRazaoSocial() + " (CNPJ: " + novaEmpresa.getCnpj() + ")");
                System.out.println("\nEmpresa cadastrada com sucesso!\n");
            }
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage() + "\n");
        }
    }

    private static EmpresaSimplesNacional cadastrarSimplesNacional(String cnpj, String razaoSocial,
            String nomeFantasia, String municipio, String estado) {
        System.out.print("Atividade Principal: ");
        String atividade = scanner.nextLine().trim();
        System.out.print("Faturamento Anual: ");
        BigDecimal fatAnual = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Faturamento Mensal: ");
        BigDecimal fatMensal = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Aliquota Efetiva (%): ");
        BigDecimal aliquota = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Data Inicio Atividade (dd/MM/yyyy): ");
        LocalDate data = LocalDate.parse(scanner.nextLine().trim(), formatoData);
        System.out.print("Numero de Funcionarios: ");
        int funcionarios = Integer.parseInt(scanner.nextLine().trim());

        return new EmpresaSimplesNacional(cnpj, razaoSocial, nomeFantasia, municipio, estado,
                atividade, fatAnual, fatMensal, aliquota, data, funcionarios);
    }

    private static EmpresaMEI cadastrarMEI(String cnpj, String razaoSocial,
            String nomeFantasia, String municipio, String estado) {
        System.out.print("Ocupacao Principal: ");
        String ocupacao = scanner.nextLine().trim();
        System.out.print("Faturamento Anual: ");
        BigDecimal fatAnual = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Faturamento Mensal: ");
        BigDecimal fatMensal = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Valor Mensal DAS: ");
        BigDecimal das = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Possui funcionario? (S/N): ");
        boolean possuiFuncionario = scanner.nextLine().trim().equalsIgnoreCase("S");
        System.out.print("Data de Abertura (dd/MM/yyyy): ");
        LocalDate data = LocalDate.parse(scanner.nextLine().trim(), formatoData);

        return new EmpresaMEI(cnpj, razaoSocial, nomeFantasia, municipio, estado,
                ocupacao, fatAnual, fatMensal, das, possuiFuncionario, data);
    }

    private static EmpresaLucroPresumido cadastrarLucroPresumido(String cnpj, String razaoSocial,
            String nomeFantasia, String municipio, String estado) {
        System.out.print("Atividade Principal: ");
        String atividade = scanner.nextLine().trim();
        System.out.print("Receita Bruta Anual: ");
        BigDecimal recAnual = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Receita Bruta Trimestral: ");
        BigDecimal recTrimestral = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Percentual de Presuncao (%): ");
        BigDecimal presuncao = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Aliquota IRPJ (%): ");
        BigDecimal irpj = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Aliquota CSLL (%): ");
        BigDecimal csll = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Valor PIS Trimestral: ");
        BigDecimal pis = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Valor COFINS Trimestral: ");
        BigDecimal cofins = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Data Inicio Atividade (dd/MM/yyyy): ");
        LocalDate data = LocalDate.parse(scanner.nextLine().trim(), formatoData);

        return new EmpresaLucroPresumido(cnpj, razaoSocial, nomeFantasia, municipio, estado,
                atividade, recAnual, recTrimestral, presuncao, irpj, csll, pis, cofins, data);
    }

    private static EmpresaLucroReal cadastrarLucroReal(String cnpj, String razaoSocial,
            String nomeFantasia, String municipio, String estado) {
        System.out.print("Atividade Principal: ");
        String atividade = scanner.nextLine().trim();
        System.out.print("Receita Bruta Anual: ");
        BigDecimal recAnual = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Despesas Dedutiveis: ");
        BigDecimal despesas = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Lucro Contabil: ");
        BigDecimal lucroContabil = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Prejuizo Fiscal: ");
        BigDecimal prejuizo = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Aliquota IRPJ (%): ");
        BigDecimal irpj = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Aliquota CSLL (%): ");
        BigDecimal csll = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Data Inicio Atividade (dd/MM/yyyy): ");
        LocalDate data = LocalDate.parse(scanner.nextLine().trim(), formatoData);

        return new EmpresaLucroReal(cnpj, razaoSocial, nomeFantasia, municipio, estado,
                atividade, recAnual, despesas, lucroContabil, null, prejuizo, irpj, csll, data);
    }

    private static void listarEmpresas() {
        if (empresas.isEmpty()) {
            System.out.println("Nenhuma empresa cadastrada.\n");
            return;
        }

        System.out.printf("%-5s %-20s %-24s %-30s%n", "No.", "Regime", "CNPJ", "Razao Social");
        System.out.println("--------------------------------------------------------------------------------");

        for (int i = 0; i < empresas.size(); i++) {
            Empresa e = empresas.get(i);
            String regime = "N/A";
            if (e instanceof CalculoTributario) {
                CalculoTributario ct = (CalculoTributario) e;
                regime = ct.getRegimeTributario();
            }
            System.out.printf("%-5d %-20s %-24s %-30s%n", (i + 1), regime, e.getCnpj(), e.getRazaoSocial());
        }

        System.out.println();
        System.out.print("Ver detalhes de uma empresa? (numero ou 0 para voltar): ");
        try {
            int escolha = Integer.parseInt(scanner.nextLine().trim());
            if (escolha > 0 && escolha <= empresas.size()) {
                System.out.println();
                empresas.get(escolha - 1).exibirDados();
                System.out.println();
            }
        } catch (NumberFormatException e) {
            System.out.println();
        }
    }

    private static void buscarPorCnpj() {
        System.out.print("Digite o CNPJ para buscar: ");
        String cnpj = scanner.nextLine().trim();

        boolean encontrada = false;
        for (Empresa e : empresas) {
            if (e.getCnpj().equals(cnpj)) {
                System.out.println();
                e.exibirDados();
                encontrada = true;
                break;
            }
        }

        if (!encontrada) {
            System.out.println("Empresa nao encontrada com o CNPJ: " + cnpj);
        }
        System.out.println();
    }

    private static void filtrarPorRegime() {
        System.out.println("Escolha o regime:");
        System.out.println("[1] Simples Nacional");
        System.out.println("[2] MEI");
        System.out.println("[3] Lucro Presumido");
        System.out.println("[4] Lucro Real");
        System.out.print("Opcao: ");

        int opcao;
        try {
            opcao = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Opcao invalida.\n");
            return;
        }

        String regimeBusca;
        switch (opcao) {
            case 1: regimeBusca = "Simples Nacional"; break;
            case 2: regimeBusca = "MEI"; break;
            case 3: regimeBusca = "Lucro Presumido"; break;
            case 4: regimeBusca = "Lucro Real"; break;
            default:
                System.out.println("Opcao invalida.\n");
                return;
        }

        System.out.println("\nEmpresas do regime '" + regimeBusca + "':\n");
        boolean encontrou = false;
        for (Empresa e : empresas) {
            if (e instanceof CalculoTributario) {
                CalculoTributario ct = (CalculoTributario) e;
                if (ct.getRegimeTributario().equals(regimeBusca)) {
                    System.out.println("  - " + e.getRazaoSocial() + " (CNPJ: " + e.getCnpj() + ")");
                    encontrou = true;
                }
            }
        }

        if (!encontrou) {
            System.out.println("  Nenhuma empresa encontrada neste regime.");
        }
        System.out.println();
    }

    private static void alterarEmpresa() {
        System.out.print("Digite o CNPJ da empresa a alterar: ");
        String cnpj = scanner.nextLine().trim();

        Empresa empresa = null;
        for (Empresa e : empresas) {
            if (e.getCnpj().equals(cnpj)) {
                empresa = e;
                break;
            }
        }

        if (empresa == null) {
            System.out.println("Empresa nao encontrada.\n");
            return;
        }

        System.out.println("\nEmpresa encontrada: " + empresa.getRazaoSocial());
        System.out.println("\nCampos disponiveis para alteracao:");
        System.out.println("[1] Nome Fantasia (atual: " + (empresa.getNomeFantasia() != null ? empresa.getNomeFantasia() : "N/A") + ")");
        System.out.println("[2] Municipio (atual: " + (empresa.getMunicipio() != null ? empresa.getMunicipio() : "N/A") + ")");
        System.out.println("[3] Estado (atual: " + (empresa.getEstado() != null ? empresa.getEstado() : "N/A") + ")");
        System.out.print("Escolha o campo: ");

        int campo;
        try {
            campo = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Opcao invalida.\n");
            return;
        }

        System.out.print("Novo valor: ");
        String novoValor = scanner.nextLine().trim();

        String nomeCampo;
        String valorAntigo;

        switch (campo) {
            case 1:
                valorAntigo = empresa.getNomeFantasia() != null ? empresa.getNomeFantasia() : "N/A";
                nomeCampo = "Nome Fantasia";
                empresa.setNomeFantasia(novoValor);
                break;
            case 2:
                valorAntigo = empresa.getMunicipio() != null ? empresa.getMunicipio() : "N/A";
                nomeCampo = "Municipio";
                empresa.setMunicipio(novoValor);
                break;
            case 3:
                valorAntigo = empresa.getEstado() != null ? empresa.getEstado() : "N/A";
                nomeCampo = "Estado";
                empresa.setEstado(novoValor);
                break;
            default:
                System.out.println("Campo invalido.\n");
                return;
        }

        logAlteracoes.add("[" + LocalDateTime.now().format(formatoLog) + "] CNPJ: " + cnpj +
                " - '" + nomeCampo + "' alterado de '" + valorAntigo + "' para '" + novoValor + "'");
        System.out.println("Campo '" + nomeCampo + "' alterado com sucesso!\n");
    }

    private static void excluirEmpresa() {
        System.out.print("Digite o CNPJ da empresa a excluir: ");
        String cnpj = scanner.nextLine().trim();

        Empresa empresaRemover = null;
        for (Empresa e : empresas) {
            if (e.getCnpj().equals(cnpj)) {
                empresaRemover = e;
                break;
            }
        }

        if (empresaRemover == null) {
            System.out.println("Empresa nao encontrada.\n");
            return;
        }

        System.out.println("Empresa encontrada: " + empresaRemover.getRazaoSocial());
        System.out.print("Confirma exclusao? (S/N): ");
        String confirmacao = scanner.nextLine().trim();

        if (confirmacao.equalsIgnoreCase("S")) {
            empresas.remove(empresaRemover);
            logAlteracoes.add("[" + LocalDateTime.now().format(formatoLog) + "] Empresa excluida: " +
                    empresaRemover.getRazaoSocial() + " (CNPJ: " + cnpj + ")");
            System.out.println("Empresa excluida com sucesso!\n");
        } else {
            System.out.println("Exclusao cancelada.\n");
        }
    }

    private static void compararFaturamentos() {
        if (empresas.isEmpty()) {
            System.out.println("Nenhuma empresa cadastrada.\n");
            return;
        }

        System.out.printf("%-20s %-24s %-30s %18s%n", "Regime", "CNPJ", "Razao Social", "Fat/Receita Anual");
        System.out.println("-----------------------------------------------------------------------------------------------");

        BigDecimal maiorValor = null;
        BigDecimal menorValor = null;
        Empresa empresaMaior = null;
        Empresa empresaMenor = null;

        for (Empresa e : empresas) {
            BigDecimal faturamento = obterFaturamentoAnual(e);
            if (faturamento == null) continue;

            String regime = "N/A";
            if (e instanceof CalculoTributario) {
                CalculoTributario ct = (CalculoTributario) e;
                regime = ct.getRegimeTributario();
            }
            System.out.printf("%-20s %-24s %-30s R$ %14s%n", regime, e.getCnpj(), e.getRazaoSocial(), faturamento.toPlainString());

            if (maiorValor == null || faturamento.compareTo(maiorValor) > 0) {
                maiorValor = faturamento;
                empresaMaior = e;
            }
            if (menorValor == null || faturamento.compareTo(menorValor) < 0) {
                menorValor = faturamento;
                empresaMenor = e;
            }
        }

        System.out.println();
        if (empresaMaior != null) {
            System.out.println("Maior faturamento: " + empresaMaior.getRazaoSocial() + " - R$ " + maiorValor.toPlainString());
        }
        if (empresaMenor != null) {
            System.out.println("Menor faturamento: " + empresaMenor.getRazaoSocial() + " - R$ " + menorValor.toPlainString());
        }
        System.out.println();
    }

    private static void compararTributos() {
        if (empresas.isEmpty()) {
            System.out.println("Nenhuma empresa cadastrada.\n");
            return;
        }

        System.out.printf("%-20s %-30s %18s%n", "Regime", "Razao Social", "Tributo Calculado");
        System.out.println("----------------------------------------------------------------------");

        for (Empresa e : empresas) {
            if (e instanceof CalculoTributario) {
                CalculoTributario ct = (CalculoTributario) e;
                System.out.printf("%-20s %-30s R$ %14s%n",
                        ct.getRegimeTributario(), e.getRazaoSocial(),
                        ct.calcularTributos().toPlainString());
            }
        }
        System.out.println();
    }

    private static void executarTestes() {
        int total = 0;
        int passou = 0;

        System.out.println("Executando testes de validacao...\n");

        total++;
        System.out.print("Teste 1: CNPJ vazio no Simples Nacional... ");
        try {
            EmpresaSimplesNacional invalida = new EmpresaSimplesNacional();
            invalida.setCnpj("");
            System.out.println("FALHOU");
        } catch (IllegalArgumentException e) {
            System.out.println("OK - " + e.getMessage());
            passou++;
        }

        total++;
        System.out.print("Teste 2: Razao Social nula no MEI... ");
        try {
            EmpresaMEI meiInvalido = new EmpresaMEI();
            meiInvalido.setRazaoSocial(null);
            System.out.println("FALHOU");
        } catch (IllegalArgumentException e) {
            System.out.println("OK - " + e.getMessage());
            passou++;
        }

        total++;
        System.out.print("Teste 3: Faturamento anual negativo... ");
        try {
            EmpresaSimplesNacional temp = new EmpresaSimplesNacional();
            temp.setCnpj("00.000.000/0001-00");
            temp.setRazaoSocial("Teste");
            temp.setFaturamentoAnual(new BigDecimal("-100.00"));
            System.out.println("FALHOU");
        } catch (IllegalArgumentException e) {
            System.out.println("OK - " + e.getMessage());
            passou++;
        }

        total++;
        System.out.print("Teste 4: Funcionarios negativo... ");
        try {
            EmpresaSimplesNacional temp = new EmpresaSimplesNacional();
            temp.setCnpj("00.000.000/0001-00");
            temp.setRazaoSocial("Teste");
            temp.setNumeroFuncionarios(-5);
            System.out.println("FALHOU");
        } catch (IllegalArgumentException e) {
            System.out.println("OK - " + e.getMessage());
            passou++;
        }

        total++;
        System.out.print("Teste 5: Percentual presuncao > 100... ");
        try {
            EmpresaLucroPresumido temp = new EmpresaLucroPresumido();
            temp.setCnpj("00.000.000/0001-00");
            temp.setRazaoSocial("Teste");
            temp.setPercentualPresuncao(new BigDecimal("150.00"));
            System.out.println("FALHOU");
        } catch (IllegalArgumentException e) {
            System.out.println("OK - " + e.getMessage());
            passou++;
        }

        total++;
        System.out.print("Teste 6: Valor DAS negativo... ");
        try {
            EmpresaMEI temp = new EmpresaMEI();
            temp.setCnpj("00.000.000/0001-00");
            temp.setRazaoSocial("Teste");
            temp.setValorMensalDAS(new BigDecimal("-10.00"));
            System.out.println("FALHOU");
        } catch (IllegalArgumentException e) {
            System.out.println("OK - " + e.getMessage());
            passou++;
        }

        total++;
        System.out.print("Teste 7: Prejuizo fiscal negativo... ");
        try {
            EmpresaLucroReal temp = new EmpresaLucroReal();
            temp.setCnpj("00.000.000/0001-00");
            temp.setRazaoSocial("Teste");
            temp.setPrejuizoFiscal(new BigDecimal("-20000.00"));
            System.out.println("FALHOU");
        } catch (IllegalArgumentException e) {
            System.out.println("OK - " + e.getMessage());
            passou++;
        }

        total++;
        System.out.print("Teste 8: Construtor completo com CNPJ vazio... ");
        try {
            new EmpresaLucroReal("", "Teste", null, null, null, null,
                    null, null, null, null, null, null, null, null);
            System.out.println("FALHOU");
        } catch (IllegalArgumentException e) {
            System.out.println("OK - " + e.getMessage());
            passou++;
        }

        total++;
        System.out.print("Teste 9: Heranca - Simples Nacional eh instancia de Empresa... ");
        EmpresaSimplesNacional testeHeranca = new EmpresaSimplesNacional();
        testeHeranca.setCnpj("00.000.000/0001-00");
        testeHeranca.setRazaoSocial("Teste");
        if (testeHeranca instanceof Empresa) {
            System.out.println("OK");
            passou++;
        } else {
            System.out.println("FALHOU");
        }

        total++;
        System.out.print("Teste 10: Interface - MEI implementa CalculoTributario... ");
        EmpresaMEI testeInterface = new EmpresaMEI();
        testeInterface.setCnpj("00.000.000/0001-00");
        testeInterface.setRazaoSocial("Teste");
        if (testeInterface instanceof CalculoTributario) {
            System.out.println("OK");
            passou++;
        } else {
            System.out.println("FALHOU");
        }

        total++;
        System.out.print("Teste 11: Polimorfismo - calcularTributos via interface... ");
        try {
            CalculoTributario ct = new EmpresaSimplesNacional(
                    "00.000.000/0001-00", "Teste Polimorfismo", null, null, null, null,
                    null, new BigDecimal("10000.00"), new BigDecimal("5.00"), null, 0);
            BigDecimal tributo = ct.calcularTributos();
            if (tributo.compareTo(new BigDecimal("500.00")) == 0) {
                System.out.println("OK - R$ " + tributo);
                passou++;
            } else {
                System.out.println("FALHOU - esperado 500.00, obteve " + tributo);
            }
        } catch (Exception e) {
            System.out.println("FALHOU - " + e.getMessage());
        }

        total++;
        System.out.print("Teste 12: Encapsulamento - atributos acessados via getters... ");
        EmpresaMEI testeEncap = new EmpresaMEI();
        testeEncap.setCnpj("00.000.000/0001-00");
        testeEncap.setRazaoSocial("Teste Encapsulamento");
        testeEncap.setNomeFantasia("Fantasia");
        if ("Teste Encapsulamento".equals(testeEncap.getRazaoSocial()) &&
            "Fantasia".equals(testeEncap.getNomeFantasia())) {
            System.out.println("OK");
            passou++;
        } else {
            System.out.println("FALHOU");
        }

        System.out.println("\nResultado: " + passou + "/" + total + " testes passaram.\n");
    }

    private static void exibirLog() {
        if (logAlteracoes.isEmpty()) {
            System.out.println("Nenhuma alteracao registrada.\n");
            return;
        }

        System.out.println("Log de alteracoes:\n");
        for (String registro : logAlteracoes) {
            System.out.println("  " + registro);
        }
        System.out.println();
    }

    private static BigDecimal obterFaturamentoAnual(Empresa empresa) {
        if (empresa instanceof EmpresaSimplesNacional) {
            return ((EmpresaSimplesNacional) empresa).getFaturamentoAnual();
        } else if (empresa instanceof EmpresaMEI) {
            return ((EmpresaMEI) empresa).getFaturamentoAnual();
        } else if (empresa instanceof EmpresaLucroPresumido) {
            return ((EmpresaLucroPresumido) empresa).getReceitaBrutaAnual();
        } else if (empresa instanceof EmpresaLucroReal) {
            return ((EmpresaLucroReal) empresa).getReceitaBrutaAnual();
        }
        return null;
    }
}
