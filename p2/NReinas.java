import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NReinas implements Callable<Integer> {
  public static final int N = 15;

  private int id;
  private int n;
  private int h;

  public NReinas(int id, int n, int h) {
    this.id = id;
    this.n = n;
    this.h = h;
  }

  public static boolean esComida(int[][] tablero, int r, int c) {

    int auxR, auxC;

    auxR = r - 1;
    auxC = c;
    while (auxR >= 0) {
      if (tablero[auxR][auxC] == 1) {
        return true;
      }
      auxR--;
    }
    auxR = r - 1;
    auxC = c + 1;
    while (auxR >= 0 && auxC < tablero.length) {
      if (tablero[auxR][auxC] == 1) {
        return true;
      }
      auxR--;
      auxC++;
    }

    auxR = r - 1;
    auxC = c - 1;
    while (auxR >= 0 && auxC >= 0) {
      if (tablero[auxR][auxC] == 1) {
        return true;
      }
      auxR--;
      auxC--;
    }
    return false;

  }

  public static int backtrackReinas(int[][] tablero, int r) {
    int c;

    int totalSoluciones = 0;

    if (r == tablero.length)
      return 1;
    for (c = 0; c < tablero.length; c++) {

      if (!esComida(tablero, r, c)) {

        tablero[r][c] = 1;
        totalSoluciones += backtrackReinas(tablero, r + 1);
        tablero[r][c] = 0;
      }

    }
    return totalSoluciones;
  }

  @Override
  public Integer call() {

    int total = 0;

    for (int c = id; c < n; c += h) {
      var tablero = new int[n][n];

      tablero[0][c] = 1;
      total += backtrackReinas(tablero, 1);
    }

    return total;
  }

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    int total = 0;

    if (args.length < 1) {
      System.out.println("Falta argumento para número de hilos");
      return;
    }

    int h;

    try {
      h = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      System.out.println(args[0] + "no es un número válido");
      return;
    }

    var pool = Executors.newFixedThreadPool(N);
    var resultados = new ArrayList<Future<Integer>>(N);

    for (int i = 0; i < h; i++) {
      var callable = new NReinas(i, N, h);
      resultados.add(pool.submit(callable));
    }

    for (var future : resultados) {
      total += future.get();
    }

    System.out.println("Total: " + total);

    pool.shutdown();
  }
}
