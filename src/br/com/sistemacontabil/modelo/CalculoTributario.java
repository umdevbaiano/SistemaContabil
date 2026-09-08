package br.com.sistemacontabil.modelo;

import java.math.BigDecimal;

public interface CalculoTributario {

    BigDecimal calcularTributos();

    String getRegimeTributario();
}
