package br.feevale.ia.estados;

import java.util.*;

import static br.feevale.ia.estados.Posicoes.BAIXO;
import static br.feevale.ia.estados.Posicoes.CIMA;
import static br.feevale.ia.estados.Posicoes.DIREITA;
import static br.feevale.ia.estados.Posicoes.ESQUERDA;
import static java.util.Comparator.comparing;

public class QuebraCabeca {

    private final String entrada;
    private final String objetivo;
    private final List<Estado> estadosAtuais = new ArrayList<>();


    public QuebraCabeca(String entrada, String objetivo) {
        this.entrada = entrada;
        this.objetivo = objetivo;
    }

    public void iniciar() {
        int contador = 0;
        String[] entradas = entrada.split("");
        String[][] quadrado = new String[3][3];
        Vazio vazio = null;
        for (int linha = 0; linha < 3; linha++) {
            for (int coluna = 0; coluna < 3; coluna++) {
                quadrado[linha][coluna] = entradas[contador];
                if (entradas[contador].equals("0")) {
                    vazio = new Vazio(linha, coluna);
                }
                contador++;
            }
        }
        estadosAtuais.add(new Estado(quadrado, vazio));
    }

    public static String[][] montarPosibilidade(int linha, int coluna, Estado estado) {
        String[][] posibilidade = new String[3][3];
        for (int linha2 = 0; linha2 < 3; linha2++) {
            System.arraycopy(estado.getQuadrado()[linha2], 0, posibilidade[linha2], 0, 3);
        }
        int linhaVazia = estado.getVazio().getLinha();
        int colunaVazia = estado.getVazio().getColuna();

        String antigo = posibilidade[linhaVazia][colunaVazia];
        posibilidade[linhaVazia][colunaVazia] = posibilidade[linha][coluna];
        posibilidade[linha][coluna] = antigo;

        return posibilidade;
    }

    public static String mostrarQuadrado(Estado estado) {
        StringBuilder montador = new StringBuilder();
        for (int linha = 0; linha < 3; linha++) {
            for (int coluna = 0; coluna < 3; coluna++) {
                montador.append(estado.getQuadrado()[linha][coluna]);
            }
        }
        return montador.toString();
    }

    public List<Estado> proximosEstados(List<Estado> estados) {
        List<Estado> estados2 = new LinkedList<>();
        for (Estado estado : estados)
            estados2.addAll(proximosEstados(estado));
        return estados2;
    }

    public List<Estado> proximosEstados(Estado estado) {
        int colunaAtual = estado.getVazio().getColuna();
        int linhaAtual = estado.getVazio().getLinha();

        int linhaAcima = CIMA.calcular(linhaAtual);
        int linhaAbaixo = BAIXO.calcular(linhaAtual);
        int colunaParaDireita = DIREITA.calcular(colunaAtual);
        int colunaParaEsquerda = ESQUERDA.calcular(colunaAtual);

        List<Estado> proximosEstados = new ArrayList<>();

        if (CIMA.valido(linhaAcima)) {
            String[][] possibilidade = montarPosibilidade(linhaAcima, colunaAtual, estado);
            proximosEstados.add(new Estado(possibilidade, new Vazio(linhaAcima, colunaAtual)));
        }
        if (BAIXO.valido(linhaAbaixo)) {
            String[][] possibilidade = montarPosibilidade(linhaAbaixo, colunaAtual, estado);
            proximosEstados.add(new Estado(possibilidade, new Vazio(linhaAbaixo, colunaAtual)));
        }
        if (ESQUERDA.valido(colunaParaEsquerda)) {
            String[][] possibilidade = montarPosibilidade(linhaAtual, colunaParaEsquerda, estado);
            proximosEstados.add(new Estado(possibilidade, new Vazio(linhaAtual, colunaParaEsquerda)));
        }
        if (DIREITA.valido(colunaParaDireita)) {
            String[][] possibilidade = montarPosibilidade(linhaAtual, colunaParaDireita, estado);
            proximosEstados.add(new Estado(possibilidade, new Vazio(linhaAtual, colunaParaDireita)));
        }
        return proximosEstados;
    }

    public void porLargura() {
        iniciar();
        do {
            String encontrado = buscar();
            if (encontrado.isBlank()) {
                atualizarEstados();
            } else {
                break;
            }
        } while (true);
    }

    private void atualizarEstados() {
        List<Estado> estados = proximosEstados(estadosAtuais);
        estadosAtuais.clear();
        estadosAtuais.addAll(estados);
    }

    private String buscar() {
        String encontrado = "";
        for (Estado estado : estadosAtuais) {
            String atual = mostrarQuadrado(estado);
            System.out.println(atual);
            if (objetivo.equals(atual)) {
                System.out.println("Encontrou: " + atual);
                encontrado = atual;
                break;
            }
        }
        return encontrado;
    }


    public List<Estado> buscaPorMelhor() throws Exception {
        iniciar();
        List<Estado> fechados = new ArrayList<>();
        List<Estado> caminho = new ArrayList<>();
        List<Estado> abertos = new ArrayList<>(estadosAtuais);
        do {
            Estado melhorEstado = abertos.getFirst();
            String quadradoMontado = mostrarQuadrado(melhorEstado);
            if (objetivo.equals(quadradoMontado)) {
                System.out.println("ACHOU");
                return caminho;
            } else {
                caminho.add(melhorEstado);
                List<Estado> proximosEstados = proximosEstados(melhorEstado);
                for (Estado estado: proximosEstados) {
                    if (!abertos.contains(estado) || !fechados.contains(estado)) {
                        estado.calcularValorHeuristico(objetivo);
                        abertos.add(estado);
                    } else if (abertos.contains(estado)) {
                        caminho = reordenarCaminho(caminho, estado);
                    } else if(fechados.contains(estado)) {
                        if (caminho.contains(estado)) {
                            fechados.remove(estado);
                            abertos.add(estado);
                        }
                    }
                }
                fechados.add(melhorEstado);
                abertos.removeAll(fechados);
                reordenarAbertosPorHeuristica(abertos);
            }

        } while (!abertos.isEmpty());

        throw new Exception();
    }

    private void reordenarAbertosPorHeuristica(List<Estado> abertos) {
        abertos.sort(comparing(Estado::getValor).reversed());
    }

    private List<Estado> reordenarCaminho(List<Estado> caminho, Estado estado) {
        int ultimaPosicao = caminho.indexOf(estado);
        return caminho.subList(0, ultimaPosicao);
    }
}
