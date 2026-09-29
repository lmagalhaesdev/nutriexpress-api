package br.com.nutriexpress.demo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PratoRequestDTO(
    @NotBlank(message = "O nome do prato é obrigatório")
    String nome,

    String descricao,

    @NotNull(message = "O valor do prato é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    BigDecimal valor,

    @NotBlank(message = "A categoria é obrigatória")
    String categoria,

    @Positive(message = "A quantidade de calorias deve ser maior que zero")
    Integer calorias,

    @Positive(message = "A quantidade deve ser maior que zero")
    Double quantidade,

    @NotBlank(message = "A unidade de medida é obrigatória")
    String unidadeMedida
) {}
