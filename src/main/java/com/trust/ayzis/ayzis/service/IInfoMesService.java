package com.trust.ayzis.ayzis.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.trust.ayzis.ayzis.model.InfoMes;
import com.trust.ayzis.ayzis.model.Produto;
import com.trust.ayzis.ayzis.model.Venda;

public interface IInfoMesService {
    public Optional<InfoMes> buscarPorId(Long id);

    public List<InfoMes> buscarTodos();

    public List<InfoMes> buscarPorProduto(Produto produto);

    public List<InfoMes> buscarPorMesAno(Date mesAno);

    public List<InfoMes> buscarPorMesAnoEntre(Date inicio, Date fim);

    public List<InfoMes> buscarPorProdutoMesAnoEntre(Produto produto, Date inicio, Date fim);

    public List<Venda> bucarVendasPorInfoMes(Long id);

    public void calcAllInfoMes();

    public void recalcInfoMes(Venda venda);

    public void calcByDataVendaBetween(Date inicio, Date fim);

    public void recalcByDelete(Venda venda);

    public void deletarPorId(Long id);
}
