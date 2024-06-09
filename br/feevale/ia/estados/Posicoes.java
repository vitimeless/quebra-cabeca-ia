package br.feevale.ia.estados;

import java.util.function.Function;

public enum Posicoes {
    CIMA(linha -> linha - 1),
    BAIXO(linha -> linha + 1),
    DIREITA(coluna -> coluna + 1),
    ESQUERDA(coluna -> coluna - 1);

    Posicoes(Function<Integer, Integer> funcao) {
        this.funcao = funcao;
    }

    private final Function<Integer, Integer> funcao;

    public boolean valido(int entrada) {
        return entrada >= 0 && entrada <= 2;
    }

    public int calcular(int entrada) {
        return funcao.apply(entrada);
    }
}
