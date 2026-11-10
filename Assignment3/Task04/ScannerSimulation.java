// Task 4: Parallel Answer-Script Scanning (Thread & Runnable)
class ScannerThread extends Thread {
    private final String stationName;
    private final int scripts;

    public ScannerThread(String stationName, int scripts) {
        this.stationName = stationName;
        this.scripts = scripts;
    }

    @Override
    public void run() {
        System.out.println(stationName + " started scanning " + scripts + " scripts.");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(stationName + " finished scanning.");
    }
}

public class ScannerSimulation {
    public static void main(String[] args) throws InterruptedException {
        ScannerThread s1 = new ScannerThread("Station-1", 50);
        ScannerThread s2 = new ScannerThread("Station-2", 75);

        s1.start();
        s2.start();

        s1.join();
        s2.join();

        System.out.println("All scanner stations completed. Final report generated.");
    }
}
