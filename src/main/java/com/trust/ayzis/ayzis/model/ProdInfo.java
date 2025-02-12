package com.trust.ayzis.ayzis.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class ProdInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date mesAno;

    private int vendaIndividual;
    private int vendaComposta;
    private int vendaDireta;
    private int vendaTotal;

    private int pendenteIndividual;
    private int pendenteComposta;
    private int pendenteDireta;
    private int pendenteTotal;

    private int cancelamentoIndividual;
    private int cancelamentoComposta;
    private int cancelamentoDireta;
    private int cancelamentoTotal;

    @ManyToOne
    private Produto produto;

    @OneToMany(mappedBy = "prodInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venda> vendas;

    public ProdInfo() {
    }

    public ProdInfo(int id, Date mesAno, int vendaIndividual, int vendaComposta, int vendaDireta, int vendaTotal,
            int cancelamentoIndividual, int cancelamentoComposta, int cancelamentoDireta, int cancelamentoTotal,
            int pendenteComposta, int pendenteDireta, int pendenteIndividual, int pendenteTotal,
            Produto produto) {
        this.id = id;
        this.mesAno = mesAno;
        this.vendaIndividual = vendaIndividual;
        this.vendaComposta = vendaComposta;
        this.vendaDireta = vendaDireta;
        this.vendaTotal = vendaTotal;
        this.cancelamentoIndividual = cancelamentoIndividual;
        this.cancelamentoComposta = cancelamentoComposta;
        this.cancelamentoDireta = cancelamentoDireta;
        this.cancelamentoTotal = cancelamentoTotal;
        this.produto = produto;
        this.pendenteComposta = pendenteComposta;
    }

    // Getters and Setters
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getMesAno() {
        return this.mesAno;
    }

    public void setMesAno(Date mesAno) {
        this.mesAno = mesAno;
    }

    public int getVendaIndividual() {
        return this.vendaIndividual;
    }

    public void setVendaIndividual(int vendaIndividual) {
        this.vendaIndividual = vendaIndividual;
    }

    public int getVendaComposta() {
        return this.vendaComposta;
    }

    public void setVendaComposta(int vendaComposta) {
        this.vendaComposta = vendaComposta;
    }

    public int getVendaDireta() {
        return this.vendaDireta;
    }

    public void setVendaDireta(int vendaDireta) {
        this.vendaDireta = vendaDireta;
    }

    public int getVendaTotal() {
        return this.vendaTotal;
    }

    public void setVendaTotal(int vendaTotal) {
        this.vendaTotal = vendaTotal;
    }

    public int getCancelamentoIndividual() {
        return this.cancelamentoIndividual;
    }

    public void setCancelamentoIndividual(int cancelamentoIndividual) {
        this.cancelamentoIndividual = cancelamentoIndividual;
    }

    public int getCancelamentoComposta() {
        return this.cancelamentoComposta;
    }

    public void setCancelamentoComposta(int cancelamentoComposta) {
        this.cancelamentoComposta = cancelamentoComposta;
    }

    public int getCancelamentoDireta() {
        return this.cancelamentoDireta;
    }

    public void setCancelamentoDireta(int cancelamentoDireta) {
        this.cancelamentoDireta = cancelamentoDireta;
    }

    public int getCancelamentoTotal() {
        return this.cancelamentoTotal;
    }

    public void setCancelamentoTotal(int cancelamentoTotal) {
        this.cancelamentoTotal = cancelamentoTotal;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getPendenteComposta() {
        return this.pendenteComposta;
    }

    public void setPendenteComposta(int pendenteComposta) {
        this.pendenteComposta = pendenteComposta;
    }

    public int getPendenteDireta() {
        return this.pendenteDireta;
    }

    public void setPendenteDireta(int pendenteDireta) {
        this.pendenteDireta = pendenteDireta;
    }

    public int getPendenteIndividual() {
        return this.pendenteIndividual;
    }

    public void setPendenteIndividual(int pendenteIndividual) {
        this.pendenteIndividual = pendenteIndividual;
    }

    public int getPendenteTotal() {
        return this.pendenteTotal;
    }

    public void setPendenteTotal(int pendenteTotal) {
        this.pendenteTotal = pendenteTotal;
    }

    public List<Venda> getVendas() {
        return this.vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }
}