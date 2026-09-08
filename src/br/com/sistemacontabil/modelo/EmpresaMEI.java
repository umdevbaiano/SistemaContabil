package br.com.sistemacontabil.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmpresaMEI extends Empresa implements CalculoTributario {

    public static final BigDecimal LIMITE_FATURAMENTO_ANUAL_MEI = new BigDecimal("81000.00");
    public static final BigDecimal VALOR_REFERENCIA_DAS_COMERCIO = new BigDecimal("71.60");
    public static final BigDecimal VALOR_REFERENCIA_DAS_SERVICO = new BigDecimal("75.60");
    public static final BigDecimal VALOR_REFERENCIA_DAS_MISTO = new BigDecimal("76.60");

    private String ocupacaoPrincipal;
    private BigDecimal faturamentoAnual;
    private BigDecimal faturamentoMensal;
    private BigDecimal valorMensalDAS;
    private boolean possuiFuncionario;
    private LocalDate dataAbertura;

    public EmpresaMEI() {
    }

    public EmpresaMEI(String cnpj, String razaoSocial, String nomeFantasia, String municipio,
                      String estado, String ocupacaoPrincipal, BigDecimal faturamentoAnual,
                      BigDecimal faturamentoMensal, BigDecimal valorMensalDAS,
                      boolean possuiFuncionario, LocalDate dataAbertura) {
        super(cnpj, razaoSocial, nomeFantasia, municipio, estado);
        setOcupacaoPrincipal(ocupacaoPrincipal);
        setFaturamentoAnual(faturamentoAnual);
        setFaturamentoMensal(faturamentoMensal);
        setValorMensalDAS(valorMensalDAS);
        setPossuiFuncionario(possuiFuncionario);
        setDataAbertura(dataAbertura);
    }

    public String getOcupacaoPrincipal() {
        return ocupacaoPrincipal;
    }

    public void setOcupacaoPrincipal(String ocupacaoPrincipal) {
        this.ocupacaoPrincipal = ocupacaoPrincipal;
    }

    public BigDecimal getFaturamentoAnual() {
        return faturamentoAnual;
    }

    public void setFaturamentoAnual(BigDecimal faturamentoAnual) {
        if (faturamentoAnual != null && faturamentoAnual.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O faturamento anual não pode ser negativo.");
        }
        this.faturamentoAnual = faturamentoAnual;
    }

    public BigDecimal getFaturamentoMensal() {
        return faturamentoMensal;
    }

    public void setFaturamentoMensal(BigDecimal faturamentoMensal) {
        if (faturamentoMensal != null && faturamentoMensal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O faturamento mensal não pode ser negativo.");
        }
        this.faturamentoMensal = faturamentoMensal;
    }

    public BigDecimal getValorMensalDAS() {
        return valorMensalDAS;
    }

    public void setValorMensalDAS(BigDecimal valorMensalDAS) {
        if (valorMensalDAS != null && valorMensalDAS.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor mensal do DAS não pode ser negativo.");
        }
        this.valorMensalDAS = valorMensalDAS;
    }

    public boolean isPossuiFuncionario() {
        return possuiFuncionario;
    }

    public void setPossuiFuncionario(boolean possuiFuncionario) {
        this.possuiFuncionario = possuiFuncionario;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public BigDecimal calcularValorDAS() {
        if (this.valorMensalDAS != null) {
            return this.valorMensalDAS;
        }
        return VALOR_REFERENCIA_DAS_MISTO;
    }

    public boolean estaNoLimiteMEI() {
        return faturamentoAnual != null && faturamentoAnual.compareTo(LIMITE_FATURAMENTO_ANUAL_MEI) <= 0;
    }

    @Override
    public BigDecimal calcularTributos() {
        return calcularValorDAS();
    }

    @Override
    public String getRegimeTributario() {
        return "MEI";
    }

    @Override
    public void exibirDados() {
        System.out.println("--- Dados da Empresa MEI ---");
        System.out.println("CNPJ: " + (getCnpj() != null ? getCnpj() : "N/D"));
        System.out.println("Razão Social: " + (getRazaoSocial() != null ? getRazaoSocial() : "N/D"));
        System.out.println("Nome Fantasia: " + (getNomeFantasia() != null ? getNomeFantasia() : "N/D"));
        System.out.println("Localidade: " + (getMunicipio() != null ? getMunicipio() : "N/D") + " - " + (getEstado() != null ? getEstado() : "N/D"));
        System.out.println("Ocupação Principal: " + (ocupacaoPrincipal != null ? ocupacaoPrincipal : "N/D"));
        System.out.println("Data de Abertura: " + (dataAbertura != null ? dataAbertura.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/D"));
        System.out.println("Faturamento Anual: " + (faturamentoAnual != null ? "R$ " + faturamentoAnual : "N/D"));
        System.out.println("Faturamento Mensal: " + (faturamentoMensal != null ? "R$ " + faturamentoMensal : "N/D"));
        System.out.println("Valor Mensal DAS a pagar: R$ " + calcularValorDAS());
        System.out.println("Possui Funcionário: " + (possuiFuncionario ? "Sim" : "Não"));
        System.out.println("Está dentro do limite do MEI? " + (estaNoLimiteMEI() ? "Sim" : "Não"));
        System.out.println("----------------------------");
    }

    @Override
    public String toString() {
        return "EmpresaMEI{" +
                "cnpj='" + getCnpj() + '\'' +
                ", razaoSocial='" + getRazaoSocial() + '\'' +
                ", nomeFantasia='" + getNomeFantasia() + '\'' +
                ", municipio='" + getMunicipio() + '\'' +
                ", estado='" + getEstado() + '\'' +
                ", ocupacaoPrincipal='" + ocupacaoPrincipal + '\'' +
                ", faturamentoAnual=" + faturamentoAnual +
                ", faturamentoMensal=" + faturamentoMensal +
                ", valorMensalDAS=" + valorMensalDAS +
                ", possuiFuncionario=" + possuiFuncionario +
                ", dataAbertura=" + dataAbertura +
                '}';
    }
}
