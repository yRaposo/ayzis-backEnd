package com.trust.ayzis.ayzis.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.trust.ayzis.ayzis.exception.ExceptionLogger;
import com.trust.ayzis.ayzis.model.IProdutoRepository;
import com.trust.ayzis.ayzis.model.Produto;

@Service
public class ProdutoService implements IProdutoService {

    Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    IProdutoRepository produtoRepository;

    @Override
    public Optional<Produto> buscarPorId(String id) {
        logger.info("Buscando produto por id" + id);

        Optional<Produto> produto = produtoRepository.findById(id);
        return produto;
    }

    @Override
    public List<Produto> buscarPorNome(String nome) {
        logger.info("Buscando produto por nome: " + nome);

        List<Produto> produto = produtoRepository.findByNomeContainingIgnoreCase(nome);

        if (produto.isEmpty()) {
            throw new ExceptionLogger("Produto não encontrado com o nome: " + nome);
        }

        return produto;
    }

    @Override
    public List<Produto> buscarPorProdutosCompostos(Produto produtosCompostos) {
        logger.info("Buscando produtos por produtos compostos: " + produtosCompostos.getId());

        List<Produto> produtos = produtoRepository.findByProdutosComposicao_ProdutoComposto(produtosCompostos);
        return produtos;
    }

    @Override
    public List<Produto> buscarTodosProdutos(Pageable pageable) {
        logger.info("Buscando todos os produtos");

        List<Produto> produtos = produtoRepository.findAll(pageable).getContent();
        return produtos;
    }

    public List<Produto> buscarTodosProdutos() {
        logger.info("Buscando todos os produtos");

        List<Produto> produtos = produtoRepository.findAll();
        return produtos;
    }

    @Override
    public List<Produto> salvarProdutosInMass(List<Produto> produtos) {
        logger.info("Salvando produtos pack");

        Set<String> ids = new HashSet<>();
        List<Produto> produtosUnicos = new ArrayList<>();

        for (Produto produto : produtos) {
            if (ids.add(produto.getId())) {
                produtosUnicos.add(produto);
            } else {
                logger.warn("ID duplicado ignorado: " + produto.getId());
            }
        }

        List<Produto> produtosSalvos = produtoRepository.saveAll(produtosUnicos);
        return produtosSalvos;
    }

    @Override
    public Optional<Produto> salvarProduto(Produto produto) {
        logger.info("Salvando produto: " + produto.getId());

        Optional<Produto> produtoSalvo = Optional.of(produtoRepository.save(produto));
        return produtoSalvo;
    }

    @Override
    public Optional<Produto> atualizarProduto(Produto newProduto) {
        logger.info("Atualizando produto: " + newProduto.getId());

        return produtoRepository.findById(newProduto.getId()).map(produto -> {
            produto.setNome(newProduto.getNome());
            produto.setDescricao(newProduto.getDescricao());
            produto.setMarca(newProduto.getMarca());
            produto.setTipo(newProduto.getTipo());
            produto.setCondicao(newProduto.getCondicao());
            produto.setPreco(newProduto.getPreco());
            produto.setLargura(newProduto.getLargura());
            produto.setAltura(newProduto.getAltura());
            produto.setProfundidade(newProduto.getProfundidade());
            produto.setPeso(newProduto.getPeso());
            produto.setUnidade(newProduto.getUnidade());
            produto.setProdutosComposicao(newProduto.getProdutosComposicao());

            return produtoRepository.save(produto);
        });
    }

    @Override
    public void deletarProdutoPorId(String id) {
        logger.info("Deletando produto por id: " + id);

        produtoRepository.deleteById(id);
    }

}
