import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

class Datos {

    public int[] reinas;
    public int colulmna;

    public Datos(int[] reinas, int columna) {
        this.reinas = reinas;
        this.colulmna = columna;
    }
}

public class NReinas2 implements Runnable {

    static final int N = 15;

    private static AtomicInteger total = new AtomicInteger(0);

    public BlockingQueue<Datos> cola;

    public NReinas2(BlockingQueue<Datos> cola) {
        this.cola = cola;
    }

    private static boolean esComida(int[] reinas, int columna) {
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

    private void backtrackReinas(int[] reinas, int columna) throws InterruptedException {

        if (columna == reinas.length) {
            // System.out.println(total.incrementAndGet());
            total.incrementAndGet();
            return;
        }

        for (int fila = 0; fila < reinas.length; fila++) {
            reinas[columna] = fila;
            if (esComida(reinas, columna))
                continue;

            var datos = new Datos(Arrays.copyOf(reinas, reinas.length), columna + 1);
            if (!cola.offer(datos)) {
                backtrackReinas(reinas, columna + 1);
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                var datos = cola.take();
                backtrackReinas(datos.reinas, datos.colulmna);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        int h = 12;

        var datos = new Datos(new int[N], 0);

        var cola = new LinkedBlockingDeque<Datos>(h);
        cola.push(datos);

        var hilos = new ArrayList<Thread>(h);
        for (int i = 0; i < h; i++) {

            var thread = new Thread(new NReinas2(cola));
            thread.start();
            hilos.add(thread);
        }

        for (var hilo : hilos) {
            hilo.join();
        }

        System.out.println("Total: " + total);
    }
}