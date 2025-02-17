package com.trust.ayzis.ayzis.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trust.ayzis.ayzis.model.Componentes;
import com.trust.ayzis.ayzis.model.IInfoMesRepository;
import com.trust.ayzis.ayzis.model.IProdutoRepository;
import com.trust.ayzis.ayzis.model.IVendaRepository;
import com.trust.ayzis.ayzis.model.InfoMes;
import com.trust.ayzis.ayzis.model.Produto;
import com.trust.ayzis.ayzis.model.Venda;

@Service
public class InfoMesService implements IInfoMesService {

    private static final Logger logger = LogManager.getLogger(InfoMesService.class);

    private static final String STATUS_ENTREGUE = "Entregue";
    private static final String STATUS_VENDA_ENTREGUE = "Venda entregue";
    private static final String STATUS_MEDIACAO_FINALIZADA = "Mediação finalizada. Te demos o dinheiro.";
    private static final String STATUS_A_CAMINHO = "A caminho";

    private final IInfoMesRepository infoMesRepository;
    private final IProdutoRepository produtoRepository;
    private final IVendaRepository vendaRepository;

    @Autowired
    public InfoMesService(IInfoMesRepository infoMesRepository, IProdutoRepository produtoRepository,
            IVendaRepository vendaRepository) {
        this.infoMesRepository = infoMesRepository;
        this.produtoRepository = produtoRepository;
        this.vendaRepository = vendaRepository;
    }

    @Override
    public Optional<InfoMes> buscarPorId(Long id) {
        logger.info("Buscando infoMes por id: {}", id);
        return infoMesRepository.findById(id);
    }

    @Override
    public List<InfoMes> buscarTodos() {
        logger.info("Buscando todos os infoMes");
        return infoMesRepository.findAll();
    }

    @Override
    public List<InfoMes> buscarPorProduto(Produto produto) {
        logger.info("Buscando infoMes por produto: {}", produto.getId());
        return infoMesRepository.findByProduto(produto);
    }

    @Override
    public List<InfoMes> buscarPorMesAno(Date mesAno) {
        logger.info("Buscando infoMes por mesAno: {}", mesAno);
        return infoMesRepository.findByMonthYear(mesAno);
    }

    @Override
    public List<InfoMes> buscarPorMesAnoEntre(Date inicio, Date fim) {
        logger.info("Buscando infoMes por mesAno entre: {} e {}", inicio, fim);
        return infoMesRepository.findByMonthYearBetween(inicio, fim);
    }

    @Override
    public List<InfoMes> buscarPorProdutoMesAnoEntre(Produto produto, Date inicio, Date fim) {
        logger.info("Buscando infoMes por produto e mesAno entre: {} e {} e {}", produto.getId(), inicio, fim);
        return infoMesRepository.findByProdutoAndMonthYearBetween(produto, inicio, fim);
    }

    @Override
    public void calcInfoMes() {
        logger.info("Calculando infoMes");
        List<Produto> produtos = produtoRepository.findAll();
        logger.info("Obtendo {} Produtos", produtos.size());

        logger.info("Iterando sobre produtos");
        for (Produto produto : produtos) {
            logger.info("Produto: {}", produto.getId());

            List<Venda> vendas = vendaRepository.findByProduto(produto);
            logger.info("Obtendo {} Vendas", vendas.size());

            logger.info("Iterando sobre Vendas produtos");
            for (Venda venda : vendas) {
                logger.info("Venda: {}", venda.getId());
                if (produto.getProdutosComposicao() == null || produto.getProdutosComposicao().stream().noneMatch(componente -> componente != null)) {
                    processarProdutoComComponentes(produto, venda);
                } else {
                    processarProdutoSemComponentes(produto, venda);
                }
            }
        }
    }

    private void processarProdutoComComponentes(Produto produto, Venda venda) {
        logger.info("Iterando sobre Produto com Componentes: {}", produto.getId());

        for (Componentes componente : produto.getProdutosComposicao()) {
            logger.info("Componente: {}", componente.getId());

            InfoMes infoMes = createInfoMesByComp(componente, venda);
            logger.info("Criado InfoMes para Componente: {}", infoMes.getId());

            atualizarInfoMes(infoMes, venda, componente.getQuantidade());
            infoMesRepository.save(infoMes);
            logger.info("InfoMes salvo: {}", infoMes.getId());
        }
    }

    private void processarProdutoSemComponentes(Produto produto, Venda venda) {
        logger.info("Processando Produto sem Componentes: {}", produto.getId());
        InfoMes infoMes = createInfoMes(produto, venda);
        logger.info("Criado InfoMes: {}", infoMes.getId());

        atualizarInfoMes(infoMes, venda, 1);
        infoMesRepository.save(infoMes);
        logger.info("InfoMes salvo: {}", infoMes.getId());
    }

    private void atualizarInfoMes(InfoMes infoMes, Venda venda, int quantidade) {
        logger.info("Atualizando InfoMes: {}", infoMes.getId());
        logger.info("Venda: {}, Quantidade: {}", venda.getId(), quantidade);

        if (venda.getOrigem().equals("ML")) {
            if (isVendaEntregue(venda)) {
                infoMes.setComponente(infoMes.getComponente() + (venda.getQuantidade() * quantidade));
                logger.info("Venda entregue, atualizando componente: {}", infoMes.getComponente());
            } else if (venda.getStatus().equals(STATUS_A_CAMINHO)) {
                infoMes.setPendenteComponente(infoMes.getComponente() + (venda.getQuantidade() * quantidade));
                logger.info("Venda a caminho, atualizando pendente componente: {}", infoMes.getPendenteComponente());
            } else {
                infoMes.setCanceladoComponente(infoMes.getComponente() + (venda.getQuantidade() * quantidade));
                logger.info("Venda cancelada, atualizando cancelado componente: {}", infoMes.getCanceladoComponente());
            }
        } else {
            if (isVendaEntregue(venda)) {
                infoMes.setDireta(infoMes.getDireta() + (venda.getQuantidade() * quantidade));
                logger.info("Venda direta entregue, atualizando direta: {}", infoMes.getDireta());
            }
        }

        infoMes.setTotal(infoMes.getTotal() + infoMes.getIndividual() + infoMes.getComponente() + infoMes.getDireta());
        infoMes.setPendenteTotal(
                infoMes.getCanceladoTotal() + infoMes.getPendenteIndividual() + infoMes.getPendenteComponente());
        infoMes.setCanceladoTotal(
                infoMes.getCanceladoTotal() + infoMes.getCanceladoIndividual() + infoMes.getCanceladoComponente());

        logger.info("InfoMes atualizado: Total: {}, Pendente Total: {}, Cancelado Total: {}",
                infoMes.getTotal(), infoMes.getPendenteTotal(), infoMes.getCanceladoTotal());
    }

    private boolean isVendaEntregue(Venda venda) {
        return venda.getStatus().equals(STATUS_ENTREGUE) || venda.getStatus().equals(STATUS_VENDA_ENTREGUE)
                || venda.getStatus().equals(STATUS_MEDIACAO_FINALIZADA);
    }

    public InfoMes createInfoMesByComp(Componentes componente, Venda venda) {
        return infoMesRepository
                .findByProdutoAndMonthYear(componente.getProdutoComponente(),
                        venda.getDataVenda().toLocalDate().getMonthValue(),
                        venda.getDataVenda().toLocalDate().getYear())
                .orElseGet(() -> {
                    InfoMes infoMesNovo = new InfoMes();
                    infoMesNovo.setMonthYear(venda.getDataVenda());
                    infoMesNovo.setProduto(componente.getProdutoComponente());
                    infoMesNovo.setVenda(List.of(venda));
                    logger.info("InfoMesVenda: {}", infoMesNovo.getId());
                    logger.info("Data: {}-{}", venda.getDataVenda().toLocalDate().getMonthValue(),
                            venda.getDataVenda().toLocalDate().getYear());
                    return infoMesNovo;
                });
    }

    public InfoMes createInfoMes(Produto produto, Venda venda) {
        return infoMesRepository
                .findByProdutoAndMonthYear(produto,
                        venda.getDataVenda().toLocalDate().getMonthValue(),
                        venda.getDataVenda().toLocalDate().getYear())
                .orElseGet(() -> {
                    InfoMes infoMesNovo = new InfoMes();
                    infoMesNovo.setMonthYear(venda.getDataVenda());
                    infoMesNovo.setProduto(produto);
                    infoMesNovo.setVenda(List.of(venda));
                    logger.info("InfoMesVenda: {}", infoMesNovo.getId());
                    logger.info("Data: {}-{}", venda.getDataVenda().toLocalDate().getMonthValue(),
                            venda.getDataVenda().toLocalDate().getYear());
                    return infoMesNovo;
                });
    }

    @Override
    public void deletarPorId(Long id) {
        logger.info("Deletando infoMes por id: {}", id);
        infoMesRepository.deleteById(id);
    }
}