package p3;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Barbero implements Runnable {

    private Lock candadoSillas = new ReentrantLock();
    private LinkedList<Cliente> clientes = new LinkedList<>();
    private int numSillas;
    public Semaphore semaforoDormir = new Semaphore(0);

    public Barbero(int numSillas) {
        this.numSillas = numSillas;
    }

    public boolean sentar(Cliente cliente) {
        boolean resultado = false;
        candadoSillas.lock();

        if (clientes.size() < numSillas) {
            clientes.add(cliente);
            resultado = true;
        }

        candadoSillas.unlock();
        return resultado;
    }

    @Override
    public void run() {
        try {
            while (true) {
                semaforoDormir.acquire();
                candadoSillas.lock();
                var cliente = clientes.remove();
                candadoSillas.unlock();
                System.out.println("Barbero: Atendiendo al cliente " + cliente.id);
                Thread.sleep(1000);
                cliente.semaforo.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Cliente implements Runnable {
    private Barbero barbero;

    public int id;
    public Semaphore semaforo = new Semaphore(0);

    public Cliente(int id, Barbero barbero) {
        this.id = id;
        this.barbero = barbero;
    }

    @Override
    public void run() {
        try {
            if (barbero.sentar(this)) {
                barbero.semaforoDormir.release();
                semaforo.acquire();
                System.out.println("Cliente " + id + ": Me acaba de atender el barbero");
            } else {
                System.out.println("Cliente " + id + ": Sala de espera llena");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

public class BarberoDormilon {
    public static void main(String[] args) throws InterruptedException {
        var barbero = new Barbero(5);
        var hiloBarbero = new Thread(barbero, "barbero");
        hiloBarbero.start();

        var random = new Random();
        for (int i = 0;; i++) {
            if (random.nextInt(7) == 0)
                Thread.sleep(10000);
            System.out.println("Main: Creando cliente con id " + i);
            var cliente = new Cliente(i, barbero);
            var hiloCliente = new Thread(cliente);
            hiloCliente.start();
        }
    }
}