package Servidor;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorChat {
    private static final int PORTA = 12345;
    private static Set<PrintWriter> clientes = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Servidor de Chat iniciado na porta " + PORTA + "...");
        try (ServerSocket servidorSocket = new ServerSocket(PORTA)) {
            while (true) {
                Socket socket = servidorSocket.accept();
                System.out.println("Novo cliente conectado: " + socket.getInetAddress());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                synchronized (clientes) {
                    clientes.add(out);
                }
                new Thread(new ManipuladorCliente(socket, out)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void transmitir(String mensagem) {
        synchronized (clientes) {
            for (PrintWriter cliente : clientes) {
                cliente.println(mensagem);
            }
        }
    }

    private static class ManipuladorCliente implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ManipuladorCliente(Socket socket, PrintWriter out) {
            this.socket = socket;
            this.out = out;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String mensagem;
                while ((mensagem = in.readLine()) != null) {
                    if (mensagem.trim().isEmpty()) {
                        continue;
                    }
                    ServidorChat.transmitir(mensagem);
                }
            } catch (IOException e) {
                System.out.println("Cliente desconectado.");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientes) {
                    clientes.remove(out);
                }
            }
        }
    }
}
