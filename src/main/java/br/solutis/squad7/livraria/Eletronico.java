package br.solutis.squad7.livraria;

public class Eletronico extends Livro{
    private int tamanho;

    public Eletronico(String titulo, String autores, String editora, double preco, int tamanho) {
        super(titulo, autores, editora, preco);
        this.tamanho = tamanho;
    }

    //setters e getters abaixo

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }


    @Override
    public String toString() {
        return super.toString() +
                "\nTamanho: " + tamanho + " KB";
    }
}
