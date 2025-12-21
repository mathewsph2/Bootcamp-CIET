/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */




package org.matheus.view;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import org.matheus.exception.SudokuException;
import org.matheus.service.SudokuService;




/**
 *
 * @author mathe
 */
public class tabuleiro extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(tabuleiro.class.getName());
    
    private SudokuService sudokuService = new SudokuService();

    
    public tabuleiro(String[] argsIniciais) {
    initComponents();
    this.setExtendedState(this.MAXIMIZED_BOTH);

    mapearBotoes();
    limparTodosOsBotoes();
    aplicarBordasGrossas();

    sudokuService.iniciarJogo(argsIniciais); // ✅ inicia o jogo no service
    preencherComArgs(argsIniciais);          // ✅ preenche a UI

    adicionarEventosDeClique();              // ✅ ativa cliques

    this.setVisible(true);
}

    
    
    
    
    private JButton[][] botoes = new JButton[9][9];

    
    
    
/* ---------------------------------------------------------------------------- */
// Limpa os valores inseridos inicialmente para formatação 
// 
/* ---------------------------------------------------------------------------- */
    private void limparTodosOsBotoes() {
    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            botoes[i][j].setText("");              // remove texto
            botoes[i][j].setEnabled(true);         // habilita para edição
            botoes[i][j].setBackground(null);      // cor padrão
            botoes[i][j].setForeground(null);      // cor padrão
        }
    }
}
  
    
/* ---------------------------------------------------------------------------- */
// Mapeamento dos Botões 
// 
/* ---------------------------------------------------------------------------- */
    private void mapearBotoes() {
        try {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {

                    int numero = i * 9 + j + 1; // 1 a 81
                    String nomeBotao = "Botao" + numero;

                    // pega o campo da classe pelo nome
                    java.lang.reflect.Field campo = this.getClass().getDeclaredField(nomeBotao);

                    // obtém o JButton
                    JButton botao = (JButton) campo.get(this);

                    botoes[i][j] = botao;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
/* ---------------------------------------------------------------------------- */
// Preenche os valores iniciais passado no args 
// 
/* ---------------------------------------------------------------------------- */
    private void preencherComArgs(String[] argsIniciais) {
        for (String arg : argsIniciais) {
            String[] partes = arg.split(",");
            int linha = Integer.parseInt(partes[0]);
            int coluna = Integer.parseInt(partes[1]);
            String valor = partes[2];

            botoes[linha][coluna].setText(valor);
            botoes[linha][coluna].setEnabled(false); // número fixo
            
            
        }
    }
    
    
    
    
/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */
    private void adicionarEventosDeClique() {
    for (int linha = 0; linha < 9; linha++) {
        for (int coluna = 0; coluna < 9; coluna++) {

            final int l = linha;
            final int c = coluna;

            botoes[l][c].addActionListener(e -> {
                if (botoes[l][c].isEnabled()) {
                    preencherCelula(l, c);
                }
            });
        }
    }
}
    
    
    
    
/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */
    
    private void preencherCelula(int linha, int coluna) {
    String valorStr = javax.swing.JOptionPane.showInputDialog(
        this,
        "Digite um número de 1 a 9:",
        "Inserir número",
        javax.swing.JOptionPane.PLAIN_MESSAGE
    );

    if (valorStr == null) return;

    if (!valorStr.matches("[1-9]")) {
        javax.swing.JOptionPane.showMessageDialog(this, "Digite apenas números de 1 a 9.");
        return;
    }

    int valor = Integer.parseInt(valorStr);

    try {
        sudokuService.colocarNumero(linha, coluna, valor);
        ErrosJogo.setText(String.valueOf(sudokuService.getErros()));


        botoes[linha][coluna].setText(valorStr);
        botoes[linha][coluna].setForeground(Color.BLACK);

    } catch (SudokuException ex) {
        
       if (ex.getMessage().equals("preenchido")) {

        int opcao = JOptionPane.showConfirmDialog(
            this,
            "A posição já está preenchida.\nDeseja remover o número existente?",
            "Confirmar remoção",
            JOptionPane.YES_NO_OPTION
        );

        if (opcao == JOptionPane.YES_OPTION) {
            //  remove o número
            sudokuService.removerNumero(linha, coluna);
            botoes[linha][coluna].setText("");

            //  agora tenta inserir novamente
            preencherCelula(linha, coluna);
        }

        return;
    }

    // outros erros (como número fixo)
    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    
    }
}

    
    
/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */
    
    private void removerCelula(int linha, int coluna) {
    try {
        sudokuService.removerNumero(linha, coluna);
        botoes[linha][coluna].setText("");
    } catch (SudokuException ex) {
        javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

    

/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */
    private void limparJogo() {
    sudokuService.limparNaoFixos();

    for (int l = 0; l < 9; l++) {
        for (int c = 0; c < 9; c++) {
            if (!sudokuService.isFixo(l, c)) {
                botoes[l][c].setText("");
                botoes[l][c].setEnabled(true);
                botoes[l][c].setForeground(Color.BLACK);
                botoes[l][c].setBackground(Color.WHITE);
            }
        }
    }
    ErrosJogo.setText(String.valueOf(sudokuService.getErros()));
    atualizarStatus();

}

    
    
    
    
 /* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */   
private void atualizarStatus() {
    String status = sudokuService.statusDoJogo();
    StatusJogo.setText(status);
}

    
    

/* ---------------------------------------------------------------------------- */
// 
// 
/* ---------------------------------------------------------------------------- */ 
private void finalizarJogo() {
    try {
        sudokuService.finalizarJogo();
        javax.swing.JOptionPane.showMessageDialog(this, "Parabéns! Jogo finalizado com sucesso.");
    } catch (SudokuException ex) {
        javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
}

    
    
    
    
/* ---------------------------------------------------------------------------- */
// Aplicado Bordas de separação
// 
/* ---------------------------------------------------------------------------- */
    private void aplicarBordasGrossas() {
    for (int linha = 0; linha < 9; linha++) {
        for (int coluna = 0; coluna < 9; coluna++) {

            JButton botao = botoes[linha][coluna];

            int top = 1;
            int left = 1;
            int bottom = 1;
            int right = 1;

            // Bordas grossas entre blocos 3x3
            if (linha % 3 == 0) top = 4;        // linha superior do bloco
            if (coluna % 3 == 0) left = 4;      // coluna esquerda do bloco
            if (linha == 8) bottom = 4;         // última linha
            if (coluna == 8) right = 4;         // última coluna

            botao.setBorder(javax.swing.BorderFactory.createMatteBorder(
                top, left, bottom, right, java.awt.Color.BLACK
            ));
        }
    }
}

    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Titulo = new javax.swing.JLabel();
        painelInferior = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        StatusJogo = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ErrosJogo = new javax.swing.JLabel();
        LimparJogo = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        Botao19 = new javax.swing.JButton();
        Botao10 = new javax.swing.JButton();
        Botao28 = new javax.swing.JButton();
        Botao37 = new javax.swing.JButton();
        Botao46 = new javax.swing.JButton();
        Botao55 = new javax.swing.JButton();
        Botao64 = new javax.swing.JButton();
        Botao73 = new javax.swing.JButton();
        Botao1 = new javax.swing.JButton();
        Botao2 = new javax.swing.JButton();
        Botao11 = new javax.swing.JButton();
        Botao20 = new javax.swing.JButton();
        Botao29 = new javax.swing.JButton();
        Botao38 = new javax.swing.JButton();
        Botao47 = new javax.swing.JButton();
        Botao56 = new javax.swing.JButton();
        Botao65 = new javax.swing.JButton();
        Botao74 = new javax.swing.JButton();
        Botao3 = new javax.swing.JButton();
        Botao4 = new javax.swing.JButton();
        Botao13 = new javax.swing.JButton();
        Botao12 = new javax.swing.JButton();
        Botao21 = new javax.swing.JButton();
        Botao22 = new javax.swing.JButton();
        Botao31 = new javax.swing.JButton();
        Botao30 = new javax.swing.JButton();
        Botao39 = new javax.swing.JButton();
        Botao40 = new javax.swing.JButton();
        Botao49 = new javax.swing.JButton();
        Botao48 = new javax.swing.JButton();
        Botao57 = new javax.swing.JButton();
        Botao58 = new javax.swing.JButton();
        Botao67 = new javax.swing.JButton();
        Botao66 = new javax.swing.JButton();
        Botao75 = new javax.swing.JButton();
        Botao76 = new javax.swing.JButton();
        Botao5 = new javax.swing.JButton();
        Botao6 = new javax.swing.JButton();
        Botao7 = new javax.swing.JButton();
        Botao8 = new javax.swing.JButton();
        Botao17 = new javax.swing.JButton();
        Botao16 = new javax.swing.JButton();
        Botao15 = new javax.swing.JButton();
        Botao14 = new javax.swing.JButton();
        Botao23 = new javax.swing.JButton();
        Botao24 = new javax.swing.JButton();
        Botao25 = new javax.swing.JButton();
        Botao26 = new javax.swing.JButton();
        Botao35 = new javax.swing.JButton();
        Botao34 = new javax.swing.JButton();
        Botao33 = new javax.swing.JButton();
        Botao32 = new javax.swing.JButton();
        Botao41 = new javax.swing.JButton();
        Botao42 = new javax.swing.JButton();
        Botao43 = new javax.swing.JButton();
        Botao44 = new javax.swing.JButton();
        Botao53 = new javax.swing.JButton();
        Botao52 = new javax.swing.JButton();
        Botao51 = new javax.swing.JButton();
        Botao50 = new javax.swing.JButton();
        Botao59 = new javax.swing.JButton();
        Botao60 = new javax.swing.JButton();
        Botao61 = new javax.swing.JButton();
        Botao62 = new javax.swing.JButton();
        Botao71 = new javax.swing.JButton();
        Botao70 = new javax.swing.JButton();
        Botao69 = new javax.swing.JButton();
        Botao68 = new javax.swing.JButton();
        Botao77 = new javax.swing.JButton();
        Botao78 = new javax.swing.JButton();
        Botao79 = new javax.swing.JButton();
        Botao80 = new javax.swing.JButton();
        Botao9 = new javax.swing.JButton();
        Botao18 = new javax.swing.JButton();
        Botao27 = new javax.swing.JButton();
        Botao36 = new javax.swing.JButton();
        Botao45 = new javax.swing.JButton();
        Botao54 = new javax.swing.JButton();
        Botao63 = new javax.swing.JButton();
        Botao72 = new javax.swing.JButton();
        Botao81 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        BtRascunhoSuperior = new javax.swing.JButton();
        BtRascunhoDireita = new javax.swing.JButton();
        BtRascunhocentral = new javax.swing.JButton();
        BtRascunhoInferior = new javax.swing.JButton();
        BtRascunhoEsquerda = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Desafio de Projeto Sudoku");

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        Titulo.setBackground(new java.awt.Color(255, 255, 255));
        Titulo.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        Titulo.setForeground(new java.awt.Color(255, 255, 255));
        Titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Titulo.setText("Desafio de Projeto Sudoku");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(Titulo)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        painelInferior.setBackground(new java.awt.Color(102, 102, 102));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Status:");

        StatusJogo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        StatusJogo.setText("Não iniciado");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Erros:");

        ErrosJogo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ErrosJogo.setText("0");

        LimparJogo.setText("Limpar Jogo");
        LimparJogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LimparJogoActionPerformed(evt);
            }
        });

        jButton1.setText("Concluir Jogo");

        javax.swing.GroupLayout painelInferiorLayout = new javax.swing.GroupLayout(painelInferior);
        painelInferior.setLayout(painelInferiorLayout);
        painelInferiorLayout.setHorizontalGroup(
            painelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelInferiorLayout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(StatusJogo)
                .addGap(71, 71, 71)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(ErrosJogo)
                .addGap(135, 135, 135)
                .addComponent(LimparJogo)
                .addGap(59, 59, 59)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        painelInferiorLayout.setVerticalGroup(
            painelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelInferiorLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(painelInferiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(StatusJogo)
                    .addComponent(jLabel4)
                    .addComponent(ErrosJogo)
                    .addComponent(LimparJogo)
                    .addComponent(jButton1))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        Botao19.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao19.setText("9");

        Botao10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao10.setText("9");

        Botao28.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao28.setText("9");

        Botao37.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao37.setText("9");

        Botao46.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao46.setText("9");

        Botao55.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao55.setText("9");

        Botao64.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao64.setText("9");

        Botao73.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao73.setText("9");

        Botao1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao1.setText("9");

        Botao2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao2.setText("9");

        Botao11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao11.setText("9");

        Botao20.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao20.setText("9");

        Botao29.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao29.setText("9");

        Botao38.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao38.setText("9");

        Botao47.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao47.setText("9");

        Botao56.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao56.setText("9");

        Botao65.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao65.setText("9");

        Botao74.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao74.setText("9");

        Botao3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao3.setText("9");

        Botao4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao4.setText("9");

        Botao13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao13.setText("9");

        Botao12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao12.setText("9");

        Botao21.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao21.setText("9");

        Botao22.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao22.setText("9");

        Botao31.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao31.setText("9");

        Botao30.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao30.setText("9");

        Botao39.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao39.setText("9");

        Botao40.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao40.setText("9");

        Botao49.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao49.setText("9");

        Botao48.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao48.setText("9");

        Botao57.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao57.setText("9");

        Botao58.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao58.setText("9");

        Botao67.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao67.setText("9");

        Botao66.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao66.setText("9");

        Botao75.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao75.setText("9");

        Botao76.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao76.setText("9");

        Botao5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao5.setText("9");

        Botao6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao6.setText("9");

        Botao7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao7.setText("9");

        Botao8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao8.setText("9");

        Botao17.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao17.setText("9");

        Botao16.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao16.setText("9");

        Botao15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao15.setText("9");

        Botao14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao14.setText("9");

        Botao23.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao23.setText("9");

        Botao24.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao24.setText("9");

        Botao25.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao25.setText("9");

        Botao26.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao26.setText("9");

        Botao35.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao35.setText("9");

        Botao34.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao34.setText("9");

        Botao33.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao33.setText("9");

        Botao32.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao32.setText("9");

        Botao41.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao41.setText("9");

        Botao42.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao42.setText("9");

        Botao43.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao43.setText("9");

        Botao44.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao44.setText("9");

        Botao53.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao53.setText("9");

        Botao52.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao52.setText("9");

        Botao51.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao51.setText("9");

        Botao50.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao50.setText("9");

        Botao59.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao59.setText("9");

        Botao60.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao60.setText("9");

        Botao61.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao61.setText("9");

        Botao62.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao62.setText("9");

        Botao71.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao71.setText("9");

        Botao70.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao70.setText("9");

        Botao69.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao69.setText("9");

        Botao68.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao68.setText("9");

        Botao77.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao77.setText("9");

        Botao78.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao78.setText("9");

        Botao79.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao79.setText("9");

        Botao80.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao80.setText("9");

        Botao9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao9.setText("9");

        Botao18.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao18.setText("9");

        Botao27.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao27.setText("9");

        Botao36.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao36.setText("9");

        Botao45.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao45.setText("9");

        Botao54.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao54.setText("9");

        Botao63.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao63.setText("9");

        Botao72.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao72.setText("9");

        Botao81.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Botao81.setText("9");

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("Rascunho:");

        BtRascunhoSuperior.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        BtRascunhoSuperior.setText("9");

        BtRascunhoDireita.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        BtRascunhoDireita.setText("9");

        BtRascunhocentral.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        BtRascunhocentral.setText("9");

        BtRascunhoInferior.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        BtRascunhoInferior.setText("9");

        BtRascunhoEsquerda.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        BtRascunhoEsquerda.setText("9");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(painelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BtRascunhoEsquerda, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtRascunhoSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(BtRascunhocentral, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BtRascunhoDireita, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(BtRascunhoInferior, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 180, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Botao1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao46, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao64, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao55, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao73, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao10, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao28, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao19, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao37, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Botao2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao47, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao65, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao56, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao74, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao11, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao29, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao20, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao38, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Botao3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao48, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao66, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao57, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao75, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao12, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao30, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao21, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao39, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Botao4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao49, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao67, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao58, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao76, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao13, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao31, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao22, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao40, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Botao5, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao50, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao68, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao59, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao77, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao14, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao32, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao23, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao41, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Botao6, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao51, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao69, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao60, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao78, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao15, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao33, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao24, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao42, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Botao7, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao52, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao70, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao61, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao79, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao16, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao34, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao25, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao43, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Botao8, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao53, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao71, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao62, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao80, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao17, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao35, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao26, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao44, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Botao9, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao54, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao72, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao63, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao81, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao18, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao36, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao27, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Botao45, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(394, 394, 394))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Botao1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Botao10, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Botao19, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Botao28, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Botao37, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Botao46, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Botao55, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Botao64, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Botao73, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(BtRascunhoSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(BtRascunhoDireita, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BtRascunhoEsquerda, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(BtRascunhocentral, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BtRascunhoInferior, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Botao2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao11, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao20, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao29, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao38, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao47, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao56, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao65, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao74, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Botao3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao12, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao21, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao30, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao39, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao48, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao57, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao66, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao75, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Botao4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao13, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao22, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao31, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao40, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao49, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao58, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao67, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao76, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Botao5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao14, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao23, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao32, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao41, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao50, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao59, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao68, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao77, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Botao6, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao15, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao24, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao33, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao42, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao51, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao60, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao69, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao78, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Botao7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao16, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao25, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao34, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao43, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao52, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao61, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao70, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao79, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Botao8, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao17, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao26, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao35, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao44, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao53, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao62, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao71, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao80, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Botao9, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao18, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao27, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao36, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao45, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao54, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao63, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao72, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Botao81, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addComponent(painelInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LimparJogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LimparJogoActionPerformed
        limparJogo();
         atualizarStatus();
    }//GEN-LAST:event_LimparJogoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String[] valoresIniciais = {
            // Linha 0
            "0,0,9", "0,1,5", "0,2,8", "0,7,2",
            // Linha 1
            "1,3,2", "1,4,5", "1,5,6", "1,7,4",
            // Linha 2
            "2,2,6", "2,6,5", "2,7,1", "2,8,7",
            // Linha 3
            "3,0,6", "3,3,3", "3,4,7", "3,5,8",
            // Linha 4
            "4,0,7", "4,1,8", "4,2,4", "4,6,9", "4,7,3", "4,8,2",
            // Linha 5
            "5,2,4", "5,3,2", "5,4,9", "5,8,8",
            // Linha 6
            "6,0,4", "6,1,9", "6,2,2", "6,6,1",
            // Linha 7
            "7,1,6", "7,3,5", "7,4,8", "7,5,1",
            // Linha 8
            "8,1,1", "8,6,7", "8,7,6", "8,8,3"

        };

        java.awt.EventQueue.invokeLater(() -> new tabuleiro(valoresIniciais));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Botao1;
    private javax.swing.JButton Botao10;
    private javax.swing.JButton Botao11;
    private javax.swing.JButton Botao12;
    private javax.swing.JButton Botao13;
    private javax.swing.JButton Botao14;
    private javax.swing.JButton Botao15;
    private javax.swing.JButton Botao16;
    private javax.swing.JButton Botao17;
    private javax.swing.JButton Botao18;
    private javax.swing.JButton Botao19;
    private javax.swing.JButton Botao2;
    private javax.swing.JButton Botao20;
    private javax.swing.JButton Botao21;
    private javax.swing.JButton Botao22;
    private javax.swing.JButton Botao23;
    private javax.swing.JButton Botao24;
    private javax.swing.JButton Botao25;
    private javax.swing.JButton Botao26;
    private javax.swing.JButton Botao27;
    private javax.swing.JButton Botao28;
    private javax.swing.JButton Botao29;
    private javax.swing.JButton Botao3;
    private javax.swing.JButton Botao30;
    private javax.swing.JButton Botao31;
    private javax.swing.JButton Botao32;
    private javax.swing.JButton Botao33;
    private javax.swing.JButton Botao34;
    private javax.swing.JButton Botao35;
    private javax.swing.JButton Botao36;
    private javax.swing.JButton Botao37;
    private javax.swing.JButton Botao38;
    private javax.swing.JButton Botao39;
    private javax.swing.JButton Botao4;
    private javax.swing.JButton Botao40;
    private javax.swing.JButton Botao41;
    private javax.swing.JButton Botao42;
    private javax.swing.JButton Botao43;
    private javax.swing.JButton Botao44;
    private javax.swing.JButton Botao45;
    private javax.swing.JButton Botao46;
    private javax.swing.JButton Botao47;
    private javax.swing.JButton Botao48;
    private javax.swing.JButton Botao49;
    private javax.swing.JButton Botao5;
    private javax.swing.JButton Botao50;
    private javax.swing.JButton Botao51;
    private javax.swing.JButton Botao52;
    private javax.swing.JButton Botao53;
    private javax.swing.JButton Botao54;
    private javax.swing.JButton Botao55;
    private javax.swing.JButton Botao56;
    private javax.swing.JButton Botao57;
    private javax.swing.JButton Botao58;
    private javax.swing.JButton Botao59;
    private javax.swing.JButton Botao6;
    private javax.swing.JButton Botao60;
    private javax.swing.JButton Botao61;
    private javax.swing.JButton Botao62;
    private javax.swing.JButton Botao63;
    private javax.swing.JButton Botao64;
    private javax.swing.JButton Botao65;
    private javax.swing.JButton Botao66;
    private javax.swing.JButton Botao67;
    private javax.swing.JButton Botao68;
    private javax.swing.JButton Botao69;
    private javax.swing.JButton Botao7;
    private javax.swing.JButton Botao70;
    private javax.swing.JButton Botao71;
    private javax.swing.JButton Botao72;
    private javax.swing.JButton Botao73;
    private javax.swing.JButton Botao74;
    private javax.swing.JButton Botao75;
    private javax.swing.JButton Botao76;
    private javax.swing.JButton Botao77;
    private javax.swing.JButton Botao78;
    private javax.swing.JButton Botao79;
    private javax.swing.JButton Botao8;
    private javax.swing.JButton Botao80;
    private javax.swing.JButton Botao81;
    private javax.swing.JButton Botao9;
    private javax.swing.JButton BtRascunhoDireita;
    private javax.swing.JButton BtRascunhoEsquerda;
    private javax.swing.JButton BtRascunhoInferior;
    private javax.swing.JButton BtRascunhoSuperior;
    private javax.swing.JButton BtRascunhocentral;
    private javax.swing.JLabel ErrosJogo;
    private javax.swing.JButton LimparJogo;
    private javax.swing.JLabel StatusJogo;
    private javax.swing.JLabel Titulo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel painelInferior;
    // End of variables declaration//GEN-END:variables
}
