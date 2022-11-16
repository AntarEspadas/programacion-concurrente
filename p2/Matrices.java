import java.util.ArrayList;
import java.util.Arrays;

class SumaMatriz implements Runnable {

    private int id;
    private int h;
    private int n;

    private int[][] m1;
    private int[][] m2;
    private int[][] resultado;

    public SumaMatriz(int id, int h, int[][] m1, int[][] m2, int[][] resultado) {
        this.id = id;
        this.h = h;
        n = m1.length;

        this.m1 = m1;
        this.m2 = m2;
        this.resultado = resultado;
    }

    @Override
    public void run() {
        int len = n * n;

        for (int k = id; k < len; k += h) {
            int i = k % n;
            int j = k / n;

            resultado[j][i] = m1[j][i] + m2[j][i];
        }
    }

}

public class Matrices {

    public static void main(String[] args) throws InterruptedException {
        int[][] m1 = {
                { 5, 5, 5, 5, 5 },
                { 5, 5, 5, 5, 5 },
                { 5, 5, 5, 5, 5 },
                { 5, 5, 5, 5, 5 },
                { 5, 5, 5, 5, 5 }
        };
        int[][] m2 = {
                { 5, 5, 5, 5, 5 },
                { 5, 5, 5, 5, 5 },
                { 5, 5, 5, 5, 5 },
                { 5, 5, 5, 5, 5 },
                { 5, 5, 5, 5, 5 }
        };
        var resultado = new int[m1.length][m2.length];
        int h = 4;
        var hilos = new ArrayList<Thread>(h);

        for (int i = 0; i < h; i++) {
            var hilo = new Thread(new SumaMatriz(i, h, m1, m2, resultado));
            hilos.add(hilo);
            hilo.start();
        }

        for (var hilo : hilos) {
            hilo.join();
        }

        System.out.println(MatrixToString(resultado));
    }

    static String MatrixToString(int[][] m) {
        ArrayList<String> arreglos = new ArrayList<>();
        for (int[] arr : m) {
            arreglos.add(Arrays.toString(arr));
        }
        return String.join("\n", arreglos);
    }

}