package p4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    static final String HOST = "localhost";
    static final int PUERTO = 5000;

    static void muestraComandos(String[] comandos) {
        for (int i = 0; i < comandos.length; i++) {
            System.out.println((i + 1) + ": " + comandos[i]);
        }
    }

    public static void main(String[] args) throws IOException {
        try (var socket = new Socket(HOST, PUERTO); var scanner = new Scanner(System.in)) {

            var out = new PrintWriter(socket.getOutputStream(), true);
            var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String nickname = null;

            while (true) {
                System.out.print("Escoja un nombre: ");
                nickname = scanner.nextLine();
                out.println(nickname);
                var resultado = in.readLine();

                if (resultado.equals("ok"))
                    break;

                System.out.println("El nombre '" + nickname + "' ya está en uso");

            }

            var comandos = new String[] { "sumar", "multiplicar", "restar", "dividir", "salir" };
            while (true) {
                muestraComandos(comandos);
                var numComandoStr = scanner.nextLine();
                var numComando = -1;
                try {
                    numComando = Integer.parseInt(numComandoStr);
                } catch (NumberFormatException e) {
                }
                if (numComando == -1 || numComando > comandos.length) {
                    System.out.println(numComandoStr + " no es un comando válido");
                    continue;
                }

                var comando = comandos[numComando - 1];
                var arg = "";

                switch (comando) {
                    case "sumar":
                    case "multiplicar":
                        System.out.println("Escriba una lista de números separados por comas");
                        arg = scanner.nextLine();
                        break;
                    case "restar":
                    case "dividir":
                        System.out.print("Primer número: ");
                        var arg1 = scanner.nextLine();
                        System.out.print("Segundo número: ");
                        arg = arg1 + "," + scanner.nextLine();
                        break;
                    case "salir":
                        System.out.println("Adiós " + nickname);
                        return;
                    default:
                        break;
                }

                out.println(comando + ":" + arg);
                System.out.println("\n" + in.readLine() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
