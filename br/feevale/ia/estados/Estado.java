package br.feevale.ia.estados;

import java.awt.geom.QuadCurve2D;
import java.util.Objects;

public class Estado {
    private final String[][] quadrado;

    private final Vazio vazio;
    private int valor;

    public Estado(String[][] quadrado, Vazio vazio) {
        this.quadrado = quadrado;
        this.vazio = vazio;
        this.valor = 0;
    }

    public Vazio getVazio() {
        return vazio;
    }

    public String[][] getQuadrado() {
        return quadrado;
    }

    @Override
    public boolean equals(Object estado) {
        if (estado instanceof Estado) {
            String estadoComparado = QuebraCabeca.mostrarQuadrado((Estado) estado);
            String estadoAtual = QuebraCabeca.mostrarQuadrado(this);
            return estadoAtual.equals(estadoComparado);
        }
        return false;
    }

    public void calcularValorHeuristico(String objetivo) {
        int posicaoDoElementoParaTrocar = (3 * vazio.getLinha()) + vazio.getColuna();
        String elementoASerTrocado = Character.toString(objetivo.charAt(posicaoDoElementoParaTrocar));

        int linhaAtual = 0;
        int colunaAtual = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (Objects.equals(quadrado[i][j], elementoASerTrocado)) {
                    linhaAtual = i;
                    colunaAtual = j;
                }
            }
        }

        int diferencaLinha = Math.abs(linhaAtual - vazio.getLinha());
        int diferecaColuna = Math.abs(colunaAtual - vazio.getColuna());

        valor = diferencaLinha + diferecaColuna;
    }

    public int getValor() {
        return valor;
    }
}
