package com.company;

import java.util.concurrent.*;

class SuperWorker implements Runnable{ //runnable to interfejs i jest juz zdefiniowany w paczkach sdk

    private int counter = 0;
    private int countTo = 0;
    private String name;

    public SuperWorker(int countTo, String name){

        this.countTo = countTo;
        this.name = name;
    }

    @Override
    public void run(){//metoda nadpisana z interfejsu runnablu

        System.out.println("Start wątku: " + name);
        System.out.flush();//metoda powiazana ze strumykami

        try{
            while (counter < countTo){
                counter++;
                System.out.println("Wątek " + name + " liczy " + counter);
                System.out.flush();
                Thread.sleep(100);// WAZNE on usypia watek jezeli wpiszesz thread to odwolujesz sie do aktualnego watku ta petla co 1/10 sekundy
            }
        } catch (InterruptedException e){//zlapany blad wyjatku
            e.printStackTrace();// e nazwa zmienna  a print stract trase to wyswietlenie trasy wyjatku
        }

        System.out.println("Koniec wątku: " + name);
        System.out.flush();// on ma dopilnowac przy zakonczeniu struniemia informacje sie zgadzaja
    }
}

public class Main {

    public static void main(String[] args) {
        System.out.println("--- Start pojedynczego egzekutora -------------");
        System.out.flush();

        final ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();// stworzenie tego egzekutora to executor serwive jest jak tablica dla watkow ale tu new siglete to metoda wywolana na klasie executor to zrobienie pojedynczego executora
        newSingleThreadExecutor.execute(new SuperWorker(10, "SingleThread"));//wywolujedz na es superworker implementuje ten interfejs runable zeby pokazac executorowi ze ma wykonac

        //
        // Zakończenie pracy wątku
        //
        newSingleThreadExecutor.shutdown();//zakonczenie pracy watku
        try{
            newSingleThreadExecutor.awaitTermination(10, TimeUnit.SECONDS);//asynchronicznie ze ma poczekac 10s
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        newSingleThreadExecutor.shutdown();

        System.out.println("--- Koniec pojedynczego egzekutora -------------");
        System.out.flush();

        //-------------------------------------------------
        // Przykład uruchamiania pojedyńczego
        // opóżnionego wątku
        //-------------------------------------------------
        System.out.println("--- Start pojedynczego opóżnionego egzekutora -------------");
        System.out.flush();

        final ScheduledExecutorService singleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        singleThreadScheduledExecutor.schedule(new SuperWorker(10, "SingleScheduledThread"), 1, TimeUnit.SECONDS);

        singleThreadScheduledExecutor.shutdown();
        try{
            singleThreadScheduledExecutor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        singleThreadScheduledExecutor.shutdown();

        System.out.println("--- Koniec pojedynczego opóżnionego egzekutora -------------");
        System.out.flush();


        //-------------------------------------------------
        // Przykład uruchamiania puli wątków,
        // gdzie uruchamiamy więcej wątków niż wielkość puli
        // Widać ze wątki powyżej
        // 5 startują wtedy gdy "stare" zakończą swoją pracę
        //-------------------------------------------------
        System.out.println("--- Start egzekutora o stałej liczbie wątków -------------");
        System.out.flush();

        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(5);//ile ma ich byc 5 ustawianie puli watkow
        for (int i = 0; i < 10; i++){//przeleci 10 razy
            newFixedThreadPool.execute(new SuperWorker(5, "newFixedThreadPool_" + i));//5 watkow zarezerwuje executory i musi na nie czekac
        }

        newFixedThreadPool.shutdown();
        try{
            newFixedThreadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        newFixedThreadPool.shutdown();

        System.out.println("--- Koniec egzekutora o stałej liczbie wątków -------------");
        System.out.flush();



    }
}
// Przykład pobrania rezultatów pracy wątku przy pomocy interfejsu Callable i Future:

/**
 * Klasa przetwarzająca dane i zwracająca je do głównego wątku
 * poprzez wywoałnie metody get()
 */
class SuperFutureWorker implements Callable<Integer>{//chce miec swoj typ generyczny

    private int counter = 0;
    private int countTo = 0;
    private int result = 1;
    private String name;

    public SuperFutureWorker(int countTo, String name){

        this.countTo = countTo;
        this.name = name;
    }

    @Override
    public Integer call(){

        System.out.println("Start wątku: " + name);
        System.out.flush();

        try{
            while (counter < countTo){
                counter++;
                this.result = this.result + this.result * counter;
                System.out.println("Wątek " + name + " liczy " + counter);
                System.out.flush();
                Thread.sleep(100);
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("Koniec wątku: " + name + " zwracam " + result);
        System.out.flush();

        return result;
    }
}
