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

    public static void main(String[] args) throws IOException {
        var socket = new Socket(HOST, PUERTO);

        var out = new PrintWriter(socket.getOutputStream(), true);
        var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        var scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Escoja un nombre: ");
            var nickname = scanner.nextLine();
            out.println(nickname);
            var resultado = in.readLine();

            if (resultado.equals("ok"))
                break;

            System.out.println("El nombre '" + nickname + "' ya está en uso");
        }
    }
}
