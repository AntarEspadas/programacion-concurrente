package p3;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

class Productor implements Runnable {

    Semaphore semaforoProductor;
    Semaphore semaforoConsumidor;

    public Productor(Semaphore semaforoProductor, Semaphore semaforoConsumidor) {
        this.semaforoProductor = semaforoProductor;
        this.semaforoConsumidor = semaforoConsumidor;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(500);
                semaforoProductor.acquire();
                ProductorConsumidor.producto = i;
                semaforoConsumidor.release();
            }

        } catch (InterruptedException e) {

        }
    }

}

class Consumidor implements Runnable {

    Semaphore semaforoProductor;
    Semaphore semaforoConsumidor;

    public Consumidor(Semaphore semaforoProductor, Semaphore semaforoConsumidor) {
        this.semaforoProductor = semaforoProductor;
        this.semaforoConsumidor = semaforoConsumidor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                semaforoConsumidor.acquire();
                System.out.println("Consumiendo el producto " +
                        ProductorConsumidor.producto);
                semaforoProductor.release();
            }
        } catch (InterruptedException e) {
        }
    }

}

public class ProductorConsumidor {

    public static int producto;

    public static void main(String[] args) throws InterruptedException {
        int p = 1;
        int c = 1;

        var semaforoProductor = new Semaphore(1);
        var semaforoConsumidor = new Semaphore(1);
        semaforoConsumidor.drainPermits();

        var productores = new ArrayList<Thread>(p);
        var consumidores = new ArrayList<Thread>(c);

        for (int i = 0; i < p; i++) {
            var productor = new Productor(semaforoProductor, semaforoConsumidor);
            var thread = new Thread(productor, "productor-" + i);
            thread.start();
            productores.add(thread);
        }

        for (int i = 0; i < c; i++) {
            var consumidor = new Consumidor(semaforoProductor, semaforoConsumidor);
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
