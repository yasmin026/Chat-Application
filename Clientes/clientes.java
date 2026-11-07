package Clientes;

import java.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class clientes extends JFrame {
    private JTextField txtMensagem;
    private JTextArea txtAreaChat;
    private PrintWriter out;
    private String nomeUsuario;

    private final Color BG_DARK = new Color(43, 42, 42); // #2B2A2A
    private final Color BG_CARD = new Color(55, 54, 54);
    private final Color COLOR_ACCENT = new Color(255, 106, 40); // #FF6A28
    private final Color COLOR_TEXT = new Color(230, 230, 230);
}
