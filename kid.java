import java.util.concurrent.Semaphore;

public class kid extends Thread {
    private static int cesto, k;
    private boolean tem_bola;
    private int time_play;
    private int time_quiet;
    private Semaphore mutex;
    private Semaphore full;
    private Semaphore empty;

    public kid(String name, boolean tem_bola, int time_play, int time_quiet, Semaphore mutex, Semaphore full, Semaphore empty) {
        this.setName(name);
        this.tem_bola = tem_bola;
        this.time_play = time_play;
        this.time_quiet = time_quiet;
        this.mutex = mutex;
        this.full = full;
        this.empty = empty;
    }

    public boolean isTem_bola() {
        return tem_bola;
    }

    public void setTem_bola(boolean tem_bola) {
        this.tem_bola = tem_bola;
    }

    public int getTime_play() {
        return time_play;
    }

    public void setTime_play(int time_play) {
        this.time_play = time_play;
    }

    public int getTime_quiet() {
        return time_quiet;
    }

    public void setTime_quiet(int time_quiet) {
        this.time_quiet = time_quiet;
    }

    public void brincar(){
        System.out.println(this.getName() + " está brincando");

        try {
            sleep(this.getTime_play()*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(this.getName() + " Terminou a brincadeira");
    }

    public void sossegar(){
        System.out.println(this.getName() + " está parado");

        try {
            sleep(this.getTime_quiet()*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(this.getName() + " acordou");
    }

    public void pega_bola(){

        try {
            full.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(this.getName() + " está pegando uma Bola");

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cesto= cesto - 1;
        this.setTem_bola(true);
        System.out.println("\n" + cesto + " bolas disponíveis\n");
        mutex.release();
        empty.release();

    }

    public void devolve_bola(){

        try {
            empty.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(this.getName() + " está Devolvendo a Bola");

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cesto = cesto +1;
        System.out.println("\n" + cesto + " bolas disponíveis\n");
        mutex.release();
        full.release();

        this.setTem_bola(false);
    }

    public void vai_cesto(){
        System.out.println(this.getName() + " está indo ao cesto");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void volta_lugar(){
        System.out.println(this.getName() + " está voltando ao seu lugar");
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        while (true) {

            sossegar();
            if (this.isTem_bola()) {
                brincar();
            }
            else {
                vai_cesto();
                pega_bola();
                volta_lugar();
                brincar();
            }
            vai_cesto();
            devolve_bola();
            volta_lugar();
        }
    }

    public static void main(String args[]) {

        k = 1;
        cesto = 0;

        Semaphore mutex = new Semaphore(1);
        Semaphore full = new Semaphore(0);
        Semaphore empty = new Semaphore(k);


        kid k1 = new kid("Angelo",false,8,5, mutex, full, empty);
        kid k2 = new kid("Grazi",true,5,8, mutex, full, empty);
        kid k3 = new kid("Marcelo", true, 7, 4,mutex,full,empty);

        System.out.println(cesto + " bolas disponíveis\n");

        k1.start();
        k2.start();
        k3.start();

    }
}
