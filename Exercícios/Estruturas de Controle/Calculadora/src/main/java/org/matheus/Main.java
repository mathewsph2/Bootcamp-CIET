/*
* LEMBRETES
*
* Scanner
* Interpolação com printf
* sc.close();
*
* Enunciado:
*
* Escreva um código onde o usuário entra com um número
* e seja gerada a tabuada de 1 até 10 desse número;
*
*
 */

package org.matheus;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("-------- TABUADA DE MULTIPLICAÇÃO ----------------");
        System.out.print("Informe um número: ");
        int numero = sc.nextInt();

        // Criando objeto já com o número informado
        Tabuada tabuada = new Tabuada(numero);

        // Chamando o método sem precisar passar parâmetro
        tabuada.calcularTabuada();

        System.out.println("--------------------------------------------------");

        sc.close();
    }
}
