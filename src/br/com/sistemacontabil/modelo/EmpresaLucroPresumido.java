package br.com.sistemacontabil.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmpresaLucroPresumido extends Empresa implements CalculoTributario {

    private String atividadePrincipal;
    private BigDecimal receitaBrutaAnual;
    private BigDecimal receitaBrutaTrimestral;
    private BigDecimal percentualPresuncao;
    private BigDecimal aliquotaIRPJ;
    private BigDecimal aliquotaCSLL;
    private BigDecimal valorPIS;
    private BigDecimal valorCOFINS;
    private LocalDate dataInicioAtividade;

    public EmpresaLucroPresumido() {
    }

    public EmpresaLucroPresumido(String cnpj, String razaoSocial, String nomeFantasia, String municipio,
                                 String estado, String atividadePrincipal, BigDecimal receitaBrutaAnual,
                                 BigDecimal receitaBrutaTrimestral, BigDecimal percentualPresuncao,
                                 BigDecimal aliquotaIRPJ, BigDecimal aliquotaCSLL, BigDecimal valorPIS,
                                 BigDecimal valorCOFINS, LocalDate dataInicioAtividade) {
        super(cnpj, razaoSocial, nomeFantasia, municipio, estado);
        setAtividadePrincipal(atividadePrincipal);
        setReceitaBrutaAnual(receitaBrutaAnual);
        setReceitaBrutaTrimestral(receitaBrutaTrimestral);
        setPercentualPresuncao(percentualPresuncao);
        setAliquotaIRPJ(aliquotaIRPJ);
        setAliquotaCSLL(aliquotaCSLL);
        setValorPIS(valorPIS);
        setValorCOFINS(valorCOFINS);
        setDataInicioAtividade(dataInicioAtividade);
    }

    public String getAtividadePrincipal() {
        return atividadePrincipal;
    }

    public void setAtividadePrincipal(String atividadePrincipal) {
        this.atividadePrincipal = atividadePrincipal;
    }

    public BigDecimal getReceitaBrutaAnual() {
        return receitaBrutaAnual;
    }

    public void setReceitaBrutaAnual(BigDecimal receitaBrutaAnual) {
        if (receitaBrutaAnual != null && receitaBrutaAnual.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Receita Bruta Anual não pode ser negativa.");
        }
        this.receitaBrutaAnual = receitaBrutaAnual;
    }

    public BigDecimal getReceitaBrutaTrimestral() {
        return receitaBrutaTrimestral;
    }

    public void setReceitaBrutaTrimestral(BigDecimal receitaBrutaTrimestral) {
        if (receitaBrutaTrimestral != null && receitaBrutaTrimestral.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Receita Bruta Trimestral não pode ser negativa.");
        }
        this.receitaBrutaTrimestral = receitaBrutaTrimestral;
    }

    public BigDecimal getPercentualPresuncao() {
        return percentualPresuncao;
    }

    public void setPercentualPresuncao(BigDecimal percentualPresuncao) {
        if (percentualPresuncao == null ||
            percentualPresuncao.compareTo(BigDecimal.ZERO) < 0 ||
            percentualPresuncao.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Percentual de Presunção deve estar entre 0 e 100.");
        }
        this.percentualPresuncao = percentualPresuncao;
    }

    public BigDecimal getAliquotaIRPJ() {
        return aliquotaIRPJ;
    }

    public void setAliquotaIRPJ(BigDecimal aliquotaIRPJ) {
        if (aliquotaIRPJ != null && aliquotaIRPJ.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Alíquota IRPJ não pode ser negativa.");
        }
        this.aliquotaIRPJ = aliquotaIRPJ;
    }

    public BigDecimal getAliquotaCSLL() {
        return aliquotaCSLL;
    }

    public void setAliquotaCSLL(BigDecimal aliquotaCSLL) {
        if (aliquotaCSLL != null && aliquotaCSLL.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Alíquota CSLL não pode ser negativa.");
        }
        this.aliquotaCSLL = aliquotaCSLL;
    }

    public BigDecimal getValorPIS() {
        return valorPIS;
    }

    public void setValorPIS(BigDecimal valorPIS) {
        if (valorPIS != null && valorPIS.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor PIS não pode ser negativo.");
        }
        this.valorPIS = valorPIS;
    }

    public BigDecimal getValorCOFINS() {
        return valorCOFINS;
    }

    public void setValorCOFINS(BigDecimal valorCOFINS) {
        if (valorCOFINS != null && valorCOFINS.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor COFINS não pode ser negativo.");
        }
        this.valorCOFINS = valorCOFINS;
    }

    public LocalDate getDataInicioAtividade() {
        return dataInicioAtividade;
    }

    public void setDataInicioAtividade(LocalDate dataInicioAtividade) {
        this.dataInicioAtividade = dataInicioAtividade;
    }

    public BigDecimal calcularBasePresumida() {
        if (receitaBrutaTrimestral == null || percentualPresuncao == null) {
            return BigDecimal.ZERO;
        }
        return receitaBrutaTrimestral.multiply(percentualPresuncao)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularTributosTrimestrais() {
        if (aliquotaIRPJ == null || aliquotaCSLL == null || valorPIS == null || valorCOFINS == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal base = calcularBasePresumida();

        BigDecimal irpj = base.multiply(aliquotaIRPJ).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal csll = base.multiply(aliquotaCSLL).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        return irpj.add(csll).add(valorPIS).add(valorCOFINS).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calcularTributos() {
        return calcularTributosTrimestrais();
    }

    @Override
    public String getRegimeTributario() {
        return "Lucro Presumido";
    }

    @Override
    public void exibirDados() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = (dataInicioAtividade != null) ? dataInicioAtividade.format(formatter) : "Não informada";

        System.out.println("--- Dados da Empresa (Lucro Presumido) ---");
        System.out.println("CNPJ: " + getCnpj());
        System.out.println("Razão Social: " + getRazaoSocial());
        System.out.println("Nome Fantasia: " + (getNomeFantasia() != null ? getNomeFantasia() : "N/A"));
        System.out.println("Município: " + (getMunicipio() != null ? getMunicipio() : "N/A") + " - UF: " + (getEstado() != null ? getEstado() : "N/A"));
        System.out.println("Atividade Principal: " + (atividadePrincipal != null ? atividadePrincipal : "N/A"));
        System.out.println("Data de Início da Atividade: " + dataFormatada);
        System.out.println("Receita Bruta Anual: R$ " + (receitaBrutaAnual != null ? receitaBrutaAnual : "0.00"));
        System.out.println("Receita Bruta Trimestral: R$ " + (receitaBrutaTrimestral != null ? receitaBrutaTrimestral : "0.00"));
        System.out.println("Percentual de Presunção: " + (percentualPresuncao != null ? percentualPresuncao : "0.00") + "%");
        System.out.println("Alíquota IRPJ: " + (aliquotaIRPJ != null ? aliquotaIRPJ : "0.00") + "%");
        System.out.println("Alíquota CSLL: " + (aliquotaCSLL != null ? aliquotaCSLL : "0.00") + "%");
        System.out.println("Valor PIS Trimestral: R$ " + (valorPIS != null ? valorPIS : "0.00"));
        System.out.println("Valor COFINS Trimestral: R$ " + (valorCOFINS != null ? valorCOFINS : "0.00"));
        System.out.println("Base Presumida Calculada: R$ " + calcularBasePresumida());
        System.out.println("Total de Tributos Trimestrais: R$ " + calcularTributosTrimestrais());
        System.out.println("------------------------------------------");
    }

    @Override
    public String toString() {
        return "EmpresaLucroPresumido{" +
                "cnpj='" + getCnpj() + '\'' +
                ", razaoSocial='" + getRazaoSocial() + '\'' +
                ", percentualPresuncao=" + percentualPresuncao +
                ", receitaBrutaTrimestral=" + receitaBrutaTrimestral +
                ", basePresumida=" + calcularBasePresumida() +
                ", totalTributos=" + calcularTributosTrimestrais() +
                '}';
    }
}
