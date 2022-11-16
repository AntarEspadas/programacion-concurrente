import java.util.Arrays;
import java.util.Stack;

class Datos {

    public int[] reinas;
    public int colulmna;

    public Datos(int[] reinas, int columna) {
        this.reinas = reinas;
        this.colulmna = columna;
    }
}

public class NReinas2 implements Runnable {

    static final int N = 10;

    public static boolean esComida(int[] reinas, int columna) {
        int x1 = columna;
        int y1 = reinas[x1];

        for (int x2 = 0; x2 < columna; x2++) {
            int y2 = reinas[x2];
            float m = (y2 - y1) / (float) (x2 - x1);
            if (Math.abs(m) == 1 || m == 0)
                return true;
        }

        return false;
    }

    public static int backtrackReinas(int[] reinas, int columna) {
        int totalSoluciones = 0;

        var stack = new Stack<Datos>();

        stack.push(new Datos(reinas, columna));

        while (!stack.isEmpty()) {
            var actual = stack.pop();
            reinas = actual.reinas;
            columna = actual.colulmna;

            if (columna == reinas.length) {
                totalSoluciones += 1;
                continue;
            }

            for (int fila = 0; fila < reinas.length; fila++) {
                var reinas2 = Arrays.copyOf(reinas, reinas.length);
                reinas2[columna] = fila;
                if (!esComida(reinas2, columna))
                    stack.push(new Datos(reinas2, columna + 1));
            }
        }
        return totalSoluciones;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

    public static void main(String[] args) {
        var reinas = new int[N];
        int total = backtrackReinas(reinas, 0);
        System.out.println("Total: " + total);
    }
}