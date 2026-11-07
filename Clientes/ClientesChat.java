package Clientes;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ClientesChat extends JFrame {
    private JTextField txtMensagem;
    private JTextArea areaChat;
    private PrintWriter out;
    private String nomeUsuario;

    private final Color BG_DARK = new Color(43, 42, 42); // #2B2A2A
    private final Color BG_CARD = new Color(55, 54, 54);
    private final Color COLOR_ACCENT = new Color(255, 106, 40); // #FF6A28
    private final Color COLOR_TEXT = new Color(230, 230, 230);

    public ClientesChat() {
        nomeUsuario = JOptionPane.showInputDialog(this, "Digite seu apelido para entrar no chat:", "Identificação",
                JOptionPane.PLAIN_MESSAGE);
        if (nomeUsuario == null || nomeUsuario.trim().isEmpty()) {
            nomeUsuario = "Anônimo";
        }

        setTitle("Chat Application - " + nomeUsuario);
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBackground(BG_DARK);
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Área de exibição de mensagens
        areaChat = new JTextArea();
        areaChat.setEditable(false);
        areaChat.setBackground(BG_CARD);
        areaChat.setForeground(COLOR_TEXT);
        areaChat.setFont(new Font("SansSerif", Font.PLAIN, 14));
        areaChat.setLineWrap(true);
        areaChat.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(areaChat);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Painel inferior (Campo de texto + Botão Enviar)
        JPanel painelInferior = new JPanel(new BorderLayout(5, 5));
        painelInferior.setBackground(BG_DARK);

        txtMensagem = new JTextField();
        txtMensagem.setBackground(BG_CARD);
        txtMensagem.setForeground(COLOR_TEXT);
        txtMensagem.setCaretColor(COLOR_TEXT);
        txtMensagem.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtMensagem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setBackground(COLOR_ACCENT);
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEnviar.setFocusPainted(false);
        btnEnviar.setBorderPainted(false);

        painelInferior.add(txtMensagem, BorderLayout.CENTER);
        painelInferior.add(btnEnviar, BorderLayout.EAST);
        painelPrincipal.add(painelInferior, BorderLayout.SOUTH);

        add(painelPrincipal);

        // Ações de envio
        btnEnviar.addActionListener(e -> enviarMensagem());
        txtMensagem.addActionListener(e -> enviarMensagem());

        conectarServidor();
    }

    private void conectarServidor() {
        try {
            Socket socket = new Socket("127.0.0.1", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Thread separada para escutar o servidor em background
            new Thread(() -> {
                try {
                    String mensagem;
                    while ((mensagem = in.readLine()) != null) {
                        areaChat.append(mensagem + "\n");
                        areaChat.setCaretPosition(areaChat.getDocument().getLength());
                    }
                } catch (IOException e) {
                    areaChat.append("[Conexão encerrada com o servidor]\n");
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Não foi possível conectar ao servidor. Inicie o ServidorChat primeiro!", "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enviarMensagem() {
        String texto = txtMensagem.getText().trim();
        if (!texto.isEmpty() && out != null) {
            out.println(nomeUsuario + ": " + texto);
            txtMensagem.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientesChat().setVisible(true));
    }
}