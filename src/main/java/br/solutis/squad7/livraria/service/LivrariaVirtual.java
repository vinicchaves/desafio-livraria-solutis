package br.solutis.squad7.livraria.service;
import br.solutis.squad7.livraria.entity.Eletronico;
import br.solutis.squad7.livraria.entity.Impresso;
import br.solutis.squad7.livraria.entity.Livro;
import br.solutis.squad7.livraria.entity.Venda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LivrariaVirtual {

    private final int MAX_IMPRESSOS = 10;
    private final int MAX_ELETRONICOS = 20;
    private final int MAX_VENDAS = 50;
    private List<Venda> vendas;

    @Autowired
    private LivroService livroService;

    @Autowired
    private VendaService vendaService;


    public void cadastrarLivro() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Selecione o tipo de livro a ser cadastrado:\n1. Livro Impresso\n2. Livro Eletrônico\n3. Ambos\nEscolha uma opção: ");
        int opcao = sc.nextInt();

        if (opcao < 1 || opcao > 3) {
            System.out.println("Opção inválida.");
            return;
        }

        if ((opcao == 1 && livroService.listarLivrosImpressos().size() >= MAX_IMPRESSOS) ||
                (opcao == 2 && livroService.listarLivrosEletronicos().size() >= MAX_ELETRONICOS)) {
            System.out.println("Não é possível cadastrar mais livros. Capacidade máxima atingida.");
            return;
        }

        if ((opcao == 1 || opcao == 3) && !cadastrarLivroImpresso(sc)) {
            return;
        }

        if ((opcao == 2 || opcao == 3) && !cadastrarLivroEletronico(sc)) {
            return;
        }
    }


    private boolean cadastrarLivroImpresso(Scanner sc) {
        sc.nextLine(); // limpar buffer
        System.out.print("Digite o título do livro impresso: ");
        String titulo = sc.nextLine();
        System.out.print("Digite o autor(es) do livro impresso: ");
        String autores = sc.nextLine();
        System.out.print("Digite a editora do livro impresso: ");
        String editora = sc.nextLine();
        System.out.print("Digite o preço do livro impresso: ");
        float preco = sc.nextFloat();
        System.out.print("Digite o frete do livro impresso: ");
        float frete = sc.nextFloat();
        System.out.print("Digite o estoque do livro impresso: ");
        int estoque = sc.nextInt();

        Impresso novoImpresso = new Impresso(titulo, autores, editora, preco, frete, estoque);
        livroService.cadastrarLivroImpresso(novoImpresso);
        System.out.println("Livro impresso cadastrado com sucesso!");
        return true;
    }

    private boolean cadastrarLivroEletronico(Scanner sc) {
        sc.nextLine(); // limpar buffer
        System.out.print("Digite o título do livro eletrônico: ");
        String titulo = sc.nextLine();
        System.out.print("Digite o autor(es) do livro eletrônico: ");
        String autores = sc.nextLine();
        System.out.print("Digite a editora do livro eletrônico: ");
        String editora = sc.nextLine();
        System.out.print("Digite o preço do livro eletrônico: ");
        float preco = sc.nextFloat();
        System.out.print("Digite o tamanho do livro eletrônico: ");
        float tamanho = sc.nextFloat();

        Eletronico novoEletronico = new Eletronico(titulo, autores, editora, preco, tamanho);
        livroService.cadastrarLivroEletronico(novoEletronico);
        System.out.println("Livro eletrônico cadastrado com sucesso!");
        return true;
    }

    public void realizarVenda() {

        Scanner sc = new Scanner(System.in);
        System.out.print("Digite o nome do cliente: ");
        String cliente = sc.nextLine();
        System.out.print("Quantidade de livros a ser comprado: ");
        int qtdLivros = sc.nextInt();
        System.out.print("Digite a opção de venda:\n1. Livro Impresso\n2. Livro Eletrônico\n3. Ambos\nEscolha uma opção: ");
        int opcao = sc.nextInt();

        if (opcao != 1 && opcao != 2 && opcao != 3) {
            System.out.println("Opção inválida.");
            return;
        }

        if (opcao < 1 || opcao > 3) {
            System.out.println("Opção inválida.");
            return;
        }

        List<Livro> livrosDisponiveis = new ArrayList<>();
        if (opcao == 1 || opcao == 3) {
            livrosDisponiveis.addAll(livroService.listarLivrosImpressos());
        }
        if (opcao == 2 || opcao == 3) {
            livrosDisponiveis.addAll(livroService.listarLivrosEletronicos());
        }

        if (livrosDisponiveis.isEmpty()) {
            System.out.println("Não há livros disponíveis para venda.");
            return;
        }

        System.out.println("Livros disponíveis:");
        for (Livro livro : livrosDisponiveis) {
            System.out.printf("[%d] %s\n", livro.getId(), livro.getTitulo());
        }

        Venda venda = new Venda();
        venda.setCliente(cliente);

        for (int i = 0; i < qtdLivros; i++) {
            System.out.print("Digite o ID do livro: ");
            int livroId = sc.nextInt();

            Livro livroEscolhido = null;
            for (Livro livro : livrosDisponiveis) {
                if (livro.getId() == livroId) {
                    livroEscolhido = livro;
                    break;
                }
            }

            if (livroEscolhido == null) {
                System.out.println("Livro não encontrado.");
                i--; // Tentar novamente
            } else {
                if (livroEscolhido instanceof Impresso) {
                    Impresso livroImpresso = (Impresso) livroEscolhido;
                    if (livroImpresso.getEstoque() <= 0) {
                        System.out.println("Livro impresso sem estoque.");
                        i--; // Tentar novamente
                    } else {
                        venda.addLivro(livroImpresso, i);
                        livroImpresso.atualizarEstoque();
                    }
                } else if (livroEscolhido instanceof Eletronico) {
                    venda.addLivro((Eletronico) livroEscolhido, i);
                }
            }
        }

        if (!venda.listarLivros().isEmpty()) {
            Venda vendaSalva = vendaService.salvarVenda(venda);
            vendas.add(vendaSalva);
            System.out.println("Venda realizada com sucesso!");
        }
    }
    public void listarLivrosConsole() {
        listarLivros(livroService.listarTodosTipos());
    }
    public void listarLivrosImpressos() {
        listarLivros(livroService.listarLivrosImpressos());
    }

    public void listarLivrosEletronicos() {
        listarLivros(livroService.listarLivrosEletronicos());
    }

    public void listarVendas() {
        System.out.println("Lista de Vendas:");
        System.out.println("+-------+---------+-------------+---------------+");
        System.out.println("| Venda | Cliente | Valor Total | Número Livros |");
        System.out.println("+-------+---------+-------------+---------------+");
        List<Venda> vendas = vendaService.listarVendas();
        for (Venda venda : vendas) {
            System.out.printf("| %-4d | %-7s | %-10.2f | %-12d |%n",
                    venda.getId(), venda.getCliente(), venda.getValor(), venda.listarLivros().size());
        }
        System.out.println("+-------+---------+-------------+---------------+");
    }

    private void listarLivros(List<Livro> livros) {
        System.out.println("Listagem de Livros:");
        System.out.println("+--------+---------------------+----------------------+-------+---------+---------+");
        System.out.println("| Tipo   |       Título        |       Autores        | Preço | Estoque | Tamanho (KB) |");
        System.out.println("+--------+---------------------+----------------------+-------+---------+---------+");

        for (Livro livro : livros) {
            if (livro instanceof Impresso) {
                Impresso impresso = (Impresso) livro;
                System.out.printf("| Impresso | %-19s | %-20s | %-5.2f | %-7d |         |%n",
                        impresso.getTitulo(), impresso.getAutores(), impresso.getPreco(), impresso.getEstoque());
            } else if (livro instanceof Eletronico) {
                Eletronico eletronico = (Eletronico) livro;
                System.out.printf("| Eletrônico | %-17s | %-20s | %-5.2f |         | %-7.2f |%n",
                        eletronico.getTitulo(), eletronico.getAutores(), eletronico.getPreco(), eletronico.getTamanho());
            }
        }

        System.out.println("+--------+---------------------+----------------------+-------+---------+---------+");

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro disponível.");
        }
    }

}
