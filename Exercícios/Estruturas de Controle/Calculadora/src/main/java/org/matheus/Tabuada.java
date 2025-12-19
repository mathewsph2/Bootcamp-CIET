package org.matheus;

public class Tabuada {

    // 1 - Atributo
    private int numero;

    // 2 - Construtor vazio
    public Tabuada() {
    }

    // 3 - Construtor com atributo
    public Tabuada(int numero) {
        this.numero = numero;
    }

    // 4 - Getters e Setters
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    // 5 - Método
    public void calcularTabuada() {
        System.out.println("Tabuada do " + numero + ":");
        for (int i = 1; i <= 10; i++) {
            System.out.printf("%d x %d = %d%n", i, numero, i * numero);
        }
    }
}
