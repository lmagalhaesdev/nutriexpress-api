package br.com.nutriexpress.demo.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pratos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private String categoria;

    private Integer calorias;

    private Double quantidade;

    private String unidadeMedida;

    public Prato(String nome, String descricao, BigDecimal valor, String categoria, Integer calorias, Double quantidade, String unidadeMedida) {
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.calorias = calorias;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
    }
}
