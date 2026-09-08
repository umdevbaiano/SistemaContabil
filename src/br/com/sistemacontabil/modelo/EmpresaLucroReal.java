package br.com.sistemacontabil.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmpresaLucroReal extends Empresa implements CalculoTributario {

    private static final BigDecimal LIMITE_ADICIONAL_IRPJ_TRIMESTRAL = new BigDecimal("60000.00");
    private static final BigDecimal ALIQUOTA_ADICIONAL_IRPJ = new BigDecimal("10");

    private String atividadePrincipal;
    private BigDecimal receitaBrutaAnual;
    private BigDecimal despesasDedutiveis;
    private BigDecimal lucroContabil;
    private BigDecimal lucroAjustado;
    private BigDecimal prejuizoFiscal;
    private BigDecimal aliquotaIRPJ;
    private BigDecimal aliquotaCSLL;
    private LocalDate dataInicioAtividade;

    public EmpresaLucroReal() {
    }

    public EmpresaLucroReal(String cnpj, String razaoSocial, String nomeFantasia, String municipio,
                            String estado, String atividadePrincipal, BigDecimal receitaBrutaAnual,
                            BigDecimal despesasDedutiveis, BigDecimal lucroContabil,
                            BigDecimal lucroAjustado, BigDecimal prejuizoFiscal,
                            BigDecimal aliquotaIRPJ, BigDecimal aliquotaCSLL,
                            LocalDate dataInicioAtividade) {
        super(cnpj, razaoSocial, nomeFantasia, municipio, estado);
        setAtividadePrincipal(atividadePrincipal);
        setReceitaBrutaAnual(receitaBrutaAnual);
        setDespesasDedutiveis(despesasDedutiveis);
        setLucroContabil(lucroContabil);
        setLucroAjustado(lucroAjustado);
        setPrejuizoFiscal(prejuizoFiscal);
        setAliquotaIRPJ(aliquotaIRPJ);
        setAliquotaCSLL(aliquotaCSLL);
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

    public BigDecimal getDespesasDedutiveis() {
        return despesasDedutiveis;
    }

    public void setDespesasDedutiveis(BigDecimal despesasDedutiveis) {
        if (despesasDedutiveis != null && despesasDedutiveis.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Despesas Dedutíveis não podem ser negativas.");
        }
        this.despesasDedutiveis = despesasDedutiveis;
    }

    public BigDecimal getLucroContabil() {
        return lucroContabil;
    }

    public void setLucroContabil(BigDecimal lucroContabil) {
        this.lucroContabil = lucroContabil;
    }

    public BigDecimal getLucroAjustado() {
        return lucroAjustado;
    }

    public void setLucroAjustado(BigDecimal lucroAjustado) {
        this.lucroAjustado = lucroAjustado;
    }

    public BigDecimal getPrejuizoFiscal() {
        return prejuizoFiscal;
    }

    public void setPrejuizoFiscal(BigDecimal prejuizoFiscal) {
        if (prejuizoFiscal != null && prejuizoFiscal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Prejuízo Fiscal não pode ser negativo.");
        }
        this.prejuizoFiscal = prejuizoFiscal;
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

    public LocalDate getDataInicioAtividade() {
        return dataInicioAtividade;
    }

    public void setDataInicioAtividade(LocalDate dataInicioAtividade) {
        this.dataInicioAtividade = dataInicioAtividade;
    }

    public BigDecimal calcularLucroReal() {
        BigDecimal base = lucroContabil != null ? lucroContabil : BigDecimal.ZERO;

        if (despesasDedutiveis != null) {
            base = base.subtract(despesasDedutiveis);
        }

        if (base.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal prejuizo = prejuizoFiscal != null ? prejuizoFiscal : BigDecimal.ZERO;
            BigDecimal limiteCompensacao = base.multiply(new BigDecimal("0.30"));
            BigDecimal compensacao = prejuizo.min(limiteCompensacao);
            base = base.subtract(compensacao);
        } else {
            base = BigDecimal.ZERO;
        }

        setLucroAjustado(base);
        return base;
    }

    public BigDecimal calcularIRPJ() {
        BigDecimal lucroReal = calcularLucroReal();
        if (lucroReal.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal aliquotaBase = aliquotaIRPJ != null ? aliquotaIRPJ : BigDecimal.ZERO;
        BigDecimal irpjNormal = lucroReal.multiply(aliquotaBase).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        BigDecimal adicional = BigDecimal.ZERO;
        if (lucroReal.compareTo(LIMITE_ADICIONAL_IRPJ_TRIMESTRAL) > 0) {
            BigDecimal excesso = lucroReal.subtract(LIMITE_ADICIONAL_IRPJ_TRIMESTRAL);
            adicional = excesso.multiply(ALIQUOTA_ADICIONAL_IRPJ).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }

        return irpjNormal.add(adicional).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularCSLL() {
        BigDecimal lucroReal = calcularLucroReal();
        if (lucroReal.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal aliquotaBase = aliquotaCSLL != null ? aliquotaCSLL : BigDecimal.ZERO;
        return lucroReal.multiply(aliquotaBase).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calcularTributos() {
        return calcularIRPJ().add(calcularCSLL());
    }

    @Override
    public String getRegimeTributario() {
        return "Lucro Real";
    }

    @Override
    public void exibirDados() {
        System.out.println("--- Dados da Empresa (Lucro Real) ---");
        System.out.println("CNPJ: " + getCnpj());
        System.out.println("Razão Social: " + getRazaoSocial());
        System.out.println("Nome Fantasia: " + (getNomeFantasia() != null ? getNomeFantasia() : "N/A"));
        System.out.println("Município/Estado: " + (getMunicipio() != null ? getMunicipio() : "N/A") + " / " + (getEstado() != null ? getEstado() : "N/A"));
        System.out.println("Atividade Principal: " + (atividadePrincipal != null ? atividadePrincipal : "N/A"));
        System.out.println("Data Início de Atividades: " + (dataInicioAtividade != null ? dataInicioAtividade.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A"));
        System.out.println("Receita Bruta Anual: R$ " + (receitaBrutaAnual != null ? receitaBrutaAnual : "0.00"));
        System.out.println("Despesas Dedutíveis: R$ " + (despesasDedutiveis != null ? despesasDedutiveis : "0.00"));
        System.out.println("Lucro Contábil: R$ " + (lucroContabil != null ? lucroContabil : "0.00"));
        System.out.println("Prejuízo Fiscal Acumulado: R$ " + (prejuizoFiscal != null ? prejuizoFiscal : "0.00"));
        System.out.println("Alíquota IRPJ (%): " + (aliquotaIRPJ != null ? aliquotaIRPJ : "0.00"));
        System.out.println("Alíquota CSLL (%): " + (aliquotaCSLL != null ? aliquotaCSLL : "0.00"));
        System.out.println("Lucro Real Calculado: R$ " + calcularLucroReal());
        System.out.println("Valor do IRPJ: R$ " + calcularIRPJ());
        System.out.println("Valor da CSLL: R$ " + calcularCSLL());
        System.out.println("-------------------------------------");
    }

    @Override
    public String toString() {
        return "EmpresaLucroReal{" +
                "cnpj='" + getCnpj() + '\'' +
                ", razaoSocial='" + getRazaoSocial() + '\'' +
                ", nomeFantasia='" + getNomeFantasia() + '\'' +
                ", municipio='" + getMunicipio() + '\'' +
                ", estado='" + getEstado() + '\'' +
                ", atividadePrincipal='" + atividadePrincipal + '\'' +
                ", receitaBrutaAnual=" + receitaBrutaAnual +
                ", despesasDedutiveis=" + despesasDedutiveis +
                ", lucroContabil=" + lucroContabil +
                ", lucroAjustado=" + lucroAjustado +
                ", prejuizoFiscal=" + prejuizoFiscal +
                ", aliquotaIRPJ=" + aliquotaIRPJ +
                ", aliquotaCSLL=" + aliquotaCSLL +
                ", dataInicioAtividade=" + dataInicioAtividade +
                '}';
    }
}
