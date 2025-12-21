/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.matheus.service;

import org.matheus.exception.SudokuException;

public class SudokuService {
    
    
    

    private Integer[][] tabuleiro = new Integer[9][9];
    private boolean[][] fixo = new boolean[9][9];

    private boolean iniciado = false;
    
    
    private int erros = 0;

public int getErros() {
    return erros;
}

    

    // ============================
    // 1. INICIAR NOVO JOGO
    // ============================
    public void iniciarJogo(String[] argsIniciais) {
        limparTudo();
        carregarValoresIniciais(argsIniciais);
        iniciado = true;
    }

    private void carregarValoresIniciais(String[] argsIniciais) {
        for (String arg : argsIniciais) {
            String[] partes = arg.split(",");
            int linha = Integer.parseInt(partes[0]);
            int coluna = Integer.parseInt(partes[1]);
            int valor = Integer.parseInt(partes[2]);

            tabuleiro[linha][coluna] = valor;
            fixo[linha][coluna] = true;
        }
    }

    // ============================
    // 2. COLOCAR NÚMERO
    // ============================
  public void colocarNumero(int linha, int coluna, int valor) {
    validarPosicao(linha, coluna);

    //  não pode alterar número fixo
    if (fixo[linha][coluna]) {
        throw new SudokuException("fixo");
    }

    //  não pode sobrescrever número já colocado
    if (tabuleiro[linha][coluna] != null) {
        throw new SudokuException("preenchido");
    }

    //  contabiliza erro, mas permite jogar
    if (!podeColocar(linha, coluna, valor)) {
        erros++;
    }

    //  só agora escreve o valor
    tabuleiro[linha][coluna] = valor;
}





    
    
    // ============================
    // 3. REMOVER NÚMERO
    // ============================
   public void removerNumero(int linha, int coluna) {
    validarPosicao(linha, coluna);

    if (fixo[linha][coluna]) {
        throw new SudokuException("Não é possível remover um número fixo.");
    }

    Integer valor = tabuleiro[linha][coluna];

    //  se não tem número, não faz nada
    if (valor == null) {
        return;
    }

    //  verifica se o número era um erro
    if (!podeColocarSemSeComparar(linha, coluna, valor)) {
        erros--;
        if (erros < 0) erros = 0; // segurança
    }

    //  agora remove
    tabuleiro[linha][coluna] = null;
}

    
    
    
    
    

    // ============================
    // 4. VERIFICAR JOGO (retorna matriz)
    // ============================
    public Integer[][] visualizarJogo() {
        return tabuleiro;
    }

    // ============================
    // 5. STATUS DO JOGO
    // ============================
    public String statusDoJogo() {
        if (!iniciado) {
            return "Não iniciado (sem erros)";
        }

        boolean completo = jogoCompleto();
        boolean erros = contemErros();

        if (!completo && !erros) return "Incompleto (sem erros)";
        if (!completo && erros) return "Incompleto (com erros)";
        if (completo && erros) return "Completo (com erros)";
        return "Completo (sem erros)";
    }

    private boolean contemErros() {
        for (int l = 0; l < 9; l++) {
            for (int c = 0; c < 9; c++) {
                Integer valor = tabuleiro[l][c];
                if (valor != null) {
                    if (!podeColocarSemSeComparar(l, c, valor)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // ============================
    // 6. LIMPAR (mantém fixos)
    // ============================
    public void limparNaoFixos() {
        for (int l = 0; l < 9; l++) {
            for (int c = 0; c < 9; c++) {
                if (!fixo[l][c]) {
                    tabuleiro[l][c] = null;
                }
            }
        }
        erros = 0;
    }

    // ============================
    // 7. FINALIZAR JOGO
    // ============================
    public void finalizarJogo() {
        if (!jogoCompleto()) {
            throw new SudokuException("O jogo não pode ser finalizado: ainda há espaços vazios.");
        }

        if (contemErros()) {
            throw new SudokuException("O jogo contém erros e não pode ser finalizado.");
        }
    }

    // ============================
    // MÉTODOS DE APOIO
    // ============================
    private void limparTudo() {
        tabuleiro = new Integer[9][9];
        fixo = new boolean[9][9];
    }

    private void validarPosicao(int linha, int coluna) {
        if (linha < 0 || linha > 8 || coluna < 0 || coluna > 8) {
            throw new SudokuException("Posição inválida.");
        }
    }

    private boolean jogoCompleto() {
        for (int l = 0; l < 9; l++) {
            for (int c = 0; c < 9; c++) {
                if (tabuleiro[l][c] == null) return false;
            }
        }
        return true;
    }

    private boolean podeColocar(int linha, int coluna, int valor) {
        return !existeNaLinha(linha, coluna, valor)
            && !existeNaColuna(linha, coluna, valor)
            && !existeNoBloco(linha, coluna, valor);
    }

    private boolean podeColocarSemSeComparar(int linha, int coluna, int valor) {
        return !existeNaLinha(linha, coluna, valor, true)
            && !existeNaColuna(linha, coluna, valor, true)
            && !existeNoBloco(linha, coluna, valor, true);
    }

    private boolean existeNaLinha(int linha, int coluna, int valor) {
        return existeNaLinha(linha, coluna, valor, false);
    }

    private boolean existeNaLinha(int linha, int coluna, int valor, boolean ignorarPosicaoAtual) {
        for (int c = 0; c < 9; c++) {
            if (ignorarPosicaoAtual && c == coluna) continue;
            if (valor == (tabuleiro[linha][c] != null ? tabuleiro[linha][c] : -1)) return true;
        }
        return false;
    }

    private boolean existeNaColuna(int linha, int coluna, int valor) {
        return existeNaColuna(linha, coluna, valor, false);
    }

    private boolean existeNaColuna(int linha, int coluna, int valor, boolean ignorarPosicaoAtual) {
        for (int l = 0; l < 9; l++) {
            if (ignorarPosicaoAtual && l == linha) continue;
            if (valor == (tabuleiro[l][coluna] != null ? tabuleiro[l][coluna] : -1)) return true;
        }
        return false;
    }

    private boolean existeNoBloco(int linha, int coluna, int valor) {
        return existeNoBloco(linha, coluna, valor, false);
    }

    private boolean existeNoBloco(int linha, int coluna, int valor, boolean ignorarPosicaoAtual) {
        int blocoLinha = (linha / 3) * 3;
        int blocoColuna = (coluna / 3) * 3;

        for (int l = blocoLinha; l < blocoLinha + 3; l++) {
            for (int c = blocoColuna; c < blocoColuna + 3; c++) {
                if (ignorarPosicaoAtual && l == linha && c == coluna) continue;
                if (valor == (tabuleiro[l][c] != null ? tabuleiro[l][c] : -1)) return true;
            }
        }
        return false;
    }

   
    
    public boolean isFixo(int linha, int coluna) {
    return fixo[linha][coluna];
}

    
    
}
