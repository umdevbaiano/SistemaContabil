package br.com.sistemacontabil.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmpresaSimplesNacional extends Empresa implements CalculoTributario {

    public static final BigDecimal LIMITE_FATURAMENTO_ANUAL = new BigDecimal("4800000.00");

    private String atividadePrincipal;
    private BigDecimal faturamentoAnual;
    private BigDecimal faturamentoMensal;
    private BigDecimal aliquotaEfetiva;
    private LocalDate dataInicioAtividade;
    private int numeroFuncionarios;

    public EmpresaSimplesNacional() {
    }

    public EmpresaSimplesNacional(String cnpj, String razaoSocial, String nomeFantasia, String municipio,
                                  String estado, String atividadePrincipal, BigDecimal faturamentoAnual,
                                  BigDecimal faturamentoMensal, BigDecimal aliquotaEfetiva,
                                  LocalDate dataInicioAtividade, int numeroFuncionarios) {
        super(cnpj, razaoSocial, nomeFantasia, municipio, estado);
        setAtividadePrincipal(atividadePrincipal);
        setFaturamentoAnual(faturamentoAnual);
        setFaturamentoMensal(faturamentoMensal);
        setAliquotaEfetiva(aliquotaEfetiva);
        setDataInicioAtividade(dataInicioAtividade);
        setNumeroFuncionarios(numeroFuncionarios);
    }

    public String getAtividadePrincipal() { return atividadePrincipal; }
    public void setAtividadePrincipal(String atividadePrincipal) { this.atividadePrincipal = atividadePrincipal; }

    public BigDecimal getFaturamentoAnual() { return faturamentoAnual; }
    public void setFaturamentoAnual(BigDecimal faturamentoAnual) {
        if (faturamentoAnual != null && faturamentoAnual.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Faturamento anual não pode ser negativo.");
        }
        this.faturamentoAnual = faturamentoAnual;
    }

    public BigDecimal getFaturamentoMensal() { return faturamentoMensal; }
    public void setFaturamentoMensal(BigDecimal faturamentoMensal) {
        if (faturamentoMensal != null && faturamentoMensal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Faturamento mensal não pode ser negativo.");
        }
        this.faturamentoMensal = faturamentoMensal;
    }

    public BigDecimal getAliquotaEfetiva() { return aliquotaEfetiva; }
    public void setAliquotaEfetiva(BigDecimal aliquotaEfetiva) {
        if (aliquotaEfetiva != null && aliquotaEfetiva.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Alíquota efetiva não pode ser negativa.");
        }
        this.aliquotaEfetiva = aliquotaEfetiva;
    }

    public LocalDate getDataInicioAtividade() { return dataInicioAtividade; }
    public void setDataInicioAtividade(LocalDate dataInicioAtividade) { this.dataInicioAtividade = dataInicioAtividade; }

    public int getNumeroFuncionarios() { return numeroFuncionarios; }
    public void setNumeroFuncionarios(int numeroFuncionarios) {
        if (numeroFuncionarios < 0) {
            throw new IllegalArgumentException("Número de funcionários não pode ser negativo.");
        }
        this.numeroFuncionarios = numeroFuncionarios;
    }

    public BigDecimal calcularTributoMensal() {
        if (faturamentoMensal == null || aliquotaEfetiva == null) {
            return BigDecimal.ZERO;
        }
        return faturamentoMensal.multiply(aliquotaEfetiva)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    public boolean estaNoLimiteSimples() {
        return faturamentoAnual != null && faturamentoAnual.compareTo(LIMITE_FATURAMENTO_ANUAL) <= 0;
    }

    @Override
    public BigDecimal calcularTributos() {
        return calcularTributoMensal();
    }

    @Override
    public String getRegimeTributario() {
        return "Simples Nacional";
    }

    @Override
    public void exibirDados() {
        System.out.println("--- Dados da Empresa Simples Nacional ---");
        System.out.println("CNPJ: " + getCnpj());
        System.out.println("Razão Social: " + getRazaoSocial());
        System.out.println("Nome Fantasia: " + (getNomeFantasia() != null ? getNomeFantasia() : "N/A"));
        System.out.println("Município/Estado: " + (getMunicipio() != null ? getMunicipio() : "N/A") + " - " + (getEstado() != null ? getEstado() : "N/A"));
        System.out.println("Atividade Principal: " + (atividadePrincipal != null ? atividadePrincipal : "N/A"));
        System.out.println("Faturamento Anual: " + (faturamentoAnual != null ? "R$ " + faturamentoAnual : "N/A"));
        System.out.println("Faturamento Mensal: " + (faturamentoMensal != null ? "R$ " + faturamentoMensal : "N/A"));
        System.out.println("Alíquota Efetiva: " + (aliquotaEfetiva != null ? aliquotaEfetiva + "%" : "N/A"));
        System.out.println("Data de Início de Atividade: " + (dataInicioAtividade != null ? dataInicioAtividade.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A"));
        System.out.println("Número de Funcionários: " + numeroFuncionarios);
        System.out.println("Tributo Mensal Calculado: R$ " + calcularTributoMensal());
        System.out.println("Está dentro do limite do Simples Nacional? " + (estaNoLimiteSimples() ? "Sim" : "Não"));
        System.out.println("-----------------------------------------");
    }

    @Override
    public String toString() {
        return "EmpresaSimplesNacional{" +
                "cnpj='" + getCnpj() + '\'' +
                ", razaoSocial='" + getRazaoSocial() + '\'' +
                ", nomeFantasia='" + getNomeFantasia() + '\'' +
                ", municipio='" + getMunicipio() + '\'' +
                ", estado='" + getEstado() + '\'' +
                ", atividadePrincipal='" + atividadePrincipal + '\'' +
                ", faturamentoAnual=" + faturamentoAnual +
                ", faturamentoMensal=" + faturamentoMensal +
                ", aliquotaEfetiva=" + aliquotaEfetiva +
                ", dataInicioAtividade=" + dataInicioAtividade +
                ", numeroFuncionarios=" + numeroFuncionarios +
                '}';
    }
}
