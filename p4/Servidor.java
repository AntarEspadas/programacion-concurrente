package p4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
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

    static double sumar(double[] nums) {
        double resultado = 0;
        for (var num : nums) {
            resultado += num;
        }
        return resultado;
    }

    static double multiplicar(double[] nums) {
        double resultado = 1;
        for (var num : nums) {
            resultado *= num;
        }
        return resultado;
    }

    static double[] parse(String args) {
        var tmpArr = args.split(",");
        var resultado = new double[tmpArr.length];
        for (int i = 0; i < resultado.length; i++) {
            resultado[i] = Double.parseDouble(tmpArr[i]);
        }
        return resultado;
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
                var tmp = mensaje.split(":", 2);
                var comando = mensaje = tmp[0];
                var argumento = new double[0];
                if (tmp.length > 1) {
                    try {
                        argumento = parse(tmp[1]);
                    } catch (NumberFormatException e) {
                        out.println("Formato inválido");
                        continue;
                    }
                }

                System.out.println("comando = " + comando);
                System.out.println("Argumento = " + Arrays.toString(argumento));

                var resultado = "";

                switch (comando) {
                    case "sumar":
                        resultado = String.valueOf(sumar(argumento));
                        break;
                    case "multiplicar":
                        resultado = String.valueOf(multiplicar(argumento));
                        break;
                    case "restar":
                        if (argumento.length != 2) {
                            resultado = "Se esperaban dos números, se encontraron " + argumento.length;
                        } else {
                            resultado = String.valueOf(argumento[0] - argumento[1]);
                        }
                        break;
                    case "dividir":
                        if (argumento.length != 2) {
                            resultado = "Se esperaban dos números, se encontraron " + argumento.length;
                        } else {
                            resultado = String.valueOf(argumento[0] / argumento[1]);
                        }
                        break;
                    case "salir":
                        break;
                    default:
                        break;
                }

                out.println(resultado);
            }

        } catch (Exception e) {

        } finally {
            if (nickname != null) {
                candadoClientes.lock();
                clientes.remove(nickname);
                candadoClientes.unlock();
            }
            System.out.println("Se desconectó el cliente" + nickname);
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
