package br.feevale.ia.main;

import br.feevale.ia.estados.QuebraCabeca;

public class Main {
    public static void main(String[] args) throws Exception {
        QuebraCabeca quebraCabeca = new QuebraCabeca("123456078", "203451786");
//        quebraCabeca.porLargura();
        quebraCabeca.buscaPorMelhor();

    }
}
