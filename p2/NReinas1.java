import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NReinas1 implements Callable<Integer> {
  public static final int N = 25;

  private int n;
  private int c;

  public NReinas1(int n, int c) {
    this.n = n;
    this.c = c;
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
    var tablero = new int[n][n];
    tablero[0][c] = 1;
    return backtrackReinas(tablero, 1);
  }

  public static void main(String[] arg) throws InterruptedException, ExecutionException {

    int total = 0;

    var pool = Executors.newFixedThreadPool(N);
    var resultados = new ArrayList<Future<Integer>>(N);

    for (int c = 0; c < N; c++) {
      var callable = new NReinas1(N, c);
      resultados.add(pool.submit(callable));
    }

    for (var future : resultados) {
      total += future.get();
    }

    System.out.println("Total: " + total);

    // if (total > 0) {

    // for (int c = 0; c < tablero.length; c++) {
    // System.out.println(Arrays.toString(tablero[c]));

    // }

    // }
  }
}
