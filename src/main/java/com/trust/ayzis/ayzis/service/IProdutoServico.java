package com.trust.ayzis.ayzis.service;

import java.util.List;
import java.util.Optional;

import com.trust.ayzis.ayzis.model.Produto;

public interface IProdutoServico {
    public Optional<Produto> buscarPorId(String id);

    public Optional<Produto> buscarPorNome(String nome);

    public List<Produto> buscarPorProdutosCompostos(Produto produtosCompostos);

    public List<Produto> buscarTodos();

    public Optional<Produto> salvarProduto(Produto produto);

    public Optional<Produto> atualizar(Produto produto);

    public void deletarPorId(String id);
}
