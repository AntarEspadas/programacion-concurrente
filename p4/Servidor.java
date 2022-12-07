package p4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Servidor implements Runnable {

    static final int PUERTO = 5000;

    static final Set<String> clientes = new HashSet<String>();
    static final Lock candadoClientes = new ReentrantLock();

    private Socket cliente;

    public Servidor(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        String nickname = null;
        try {
            var out = new PrintWriter(cliente.getOutputStream(), true);
            var in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

            while (true) {
                nickname = in.readLine();
                candadoClientes.lock();
                if (!clientes.contains(nickname)) {
                    clientes.add(nickname);
                    candadoClientes.unlock();
                    out.println("ok");
                    break;
                }
                candadoClientes.unlock();

                out.println("nombre en uso");
            }

            while (true) {
                var mensaje = in.readLine();
                var tmp = mensaje.split(":", 1);
                var comando = mensaje = tmp[0];
                var argumento = "";
                if (tmp.length > 1) {
                    argumento = tmp[1];
                }

                System.out.println("comando = " + comando);

                switch (comando) {
                    case "sumar":

                        break;
                    case "multiplicar":
                        break;
                    case "restar":
                        break;
                    case "dividir":
                        break;
                    case "stop":
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {

        } finally {
            if (nickname != null) {
                candadoClientes.lock();
                clientes.remove(nickname);
                candadoClientes.unlock();
            }
            System.out.println("Se desconectó el cliente");
        }
    }

    public static void init() {
        try (var serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                var cliente = serverSocket.accept();
                var servidor = new Servidor(cliente);
                new Thread(servidor).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        init();
    }
}
