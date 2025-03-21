package com.trust.ayzis.ayzis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Componentes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int quantidade;

    @ManyToOne
    @JsonBackReference
    private Produto produtoComposto;

    @OneToOne
    private Produto produtoComponente;

    public Componentes() {
    }

    public Componentes(Produto produtoComposto, Produto produtoComponente, int quantidade) {
        this.produtoComposto = produtoComposto;
        this.produtoComponente = produtoComponente;
        this.quantidade = quantidade;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProdutoComposto() {
        return produtoComposto;
    }

    public void setProdutoComposto(Produto produtoComposto) {
        this.produtoComposto = produtoComposto;
    }

    public Produto getProdutoComponente() {
        return produtoComponente;
    }

    public void setProdutoComponente(Produto produto) {
        this.produtoComponente = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}