package p3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ProductorAlmacen implements Runnable {

    Semaphore semaforoProductor;
    Semaphore semaforoConsumidor;
    Queue<Integer> productos;
    Lock candado;

    public ProductorAlmacen(Semaphore semaforoProductor, Semaphore semaforoConsumidor, Queue<Integer> productos,
            Lock candado) {
        this.semaforoProductor = semaforoProductor;
        this.semaforoConsumidor = semaforoConsumidor;
        this.productos = productos;
        this.candado = candado;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                semaforoProductor.acquire();

                System.out.println("Produciendo el producto " + i);
                candado.lock();
                productos.add(i);
                candado.unlock();
                semaforoConsumidor.release();
            }

        } catch (InterruptedException e) {

        }
    }

}

class ConsumidorAlmacen implements Runnable {

    Semaphore semaforoProductor;
    Semaphore semaforoConsumidor;
    Queue<Integer> productos;
    Lock candado;

    public ConsumidorAlmacen(Semaphore semaforoProductor, Semaphore semaforoConsumidor, Queue<Integer> productos,
            Lock candado) {
        this.semaforoProductor = semaforoProductor;
        this.semaforoConsumidor = semaforoConsumidor;
        this.productos = productos;
        this.candado = candado;
    }

    @Override
    public void run() {
        try {
            while (true) {
                semaforoConsumidor.acquire();
                candado.lock();
                int producto = productos.remove();
                System.out.println("Consumiendo el producto " + producto);
                candado.unlock();
                semaforoProductor.release();
            }
        } catch (InterruptedException e) {
        }
    }

}

public class ProductorConsumidorAlmacen {

    public static void main(String[] args) throws InterruptedException {
        int p = 4;
        int c = 1;

        int n = 5;

        Queue<Integer> productos = new LinkedList<>();

        var semaforoProductor = new Semaphore(n);
        var semaforoConsumidor = new Semaphore(n);
        semaforoConsumidor.drainPermits();

        var candado = new ReentrantLock();

        var productores = new ArrayList<Thread>(p);
        var consumidores = new ArrayList<Thread>(c);

        for (int i = 0; i < p; i++) {
            var productor = new ProductorAlmacen(semaforoProductor, semaforoConsumidor, productos, candado);
            var thread = new Thread(productor, "productor-" + i);
            thread.start();
            productores.add(thread);
        }

        for (int i = 0; i < c; i++) {
            var consumidor = new ConsumidorAlmacen(semaforoProductor, semaforoConsumidor, productos, candado);
            var thread = new Thread(consumidor, "consumidor-" + i);
            thread.start();
            consumidores.add(thread);
        }

        for (var productor : productores) {
            productor.join();
        }

        semaforoProductor.acquire();

        for (var consumidor : consumidores) {
            consumidor.interrupt();
        }

    }
}
