package br.solutis.squad7.livraria.service;

import br.solutis.squad7.livraria.entity.Eletronico;
import br.solutis.squad7.livraria.entity.Impresso;
import br.solutis.squad7.livraria.entity.Livro;
import br.solutis.squad7.livraria.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    private final int MAX_IMPRESSOS = 10;
    private final int MAX_ELETRONICOS = 20;

    @Autowired
    private LivroRepository livroRepository;

    public void cadastrarLivroImpresso(Impresso livroImpresso) {
        if (listarLivrosImpressos().size() >= MAX_IMPRESSOS) {
            throw new IllegalStateException("Não é possível cadastrar mais livros impressos. Capacidade máxima atingida.");
        }
        livroRepository.save(livroImpresso);
    }

    public void cadastrarLivroEletronico(Eletronico livroEletronico) {
        if (listarLivrosEletronicos().size() >= MAX_ELETRONICOS) {
            throw new IllegalStateException("Não é possível cadastrar mais livros eletrônicos. Capacidade máxima atingida.");
        }
        livroRepository.save(livroEletronico);
    }

    public List<Livro> listarLivrosImpressos() {
        return livroRepository.findImpressosBy(); // Supondo que você tenha um campo "tipo" no banco de dados para distinguir os tipos
    }
    public List<Livro> listarTodosTipos(){
        List<Livro> listacompleta = livroRepository.findImpressosBy();
        listacompleta.addAll(livroRepository.findEletronicosBy());
        return listacompleta;
    }

    public List<Livro> listarLivrosEletronicos() {
        return livroRepository.findEletronicosBy(); // Supondo que você tenha um campo "tipo" no banco de dados para distinguir os tipos
    }

}
