package br.feevale.ia.estados;

public class Vazio {

    private int linha;
    private int coluna;

    public Vazio(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getColuna() {
        return coluna;
    }

    public int getLinha() {
        return linha;
    }

    public void mover(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }
}
