import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final int MANUFACTURING_TIME = 3000;
        final int CUSTOMERS_PATIENCE = 2900;

        List<String> cars = new ArrayList<>();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                synchronized (cars) {
                    String car = "Новый автомобиль " + (i + 1);
                    cars.add(car);
                    System.out.println("Производитель Toyota выпустил " + car);
                    cars.notifyAll();
                }
                    try {
                        Thread.sleep(MANUFACTURING_TIME);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (cars) {
                    while (cars.isEmpty()) {
                        System.out.println("Машин нет (из 1ого потока)");
                        try {
                            cars.wait(CUSTOMERS_PATIENCE);
                            System.out.println("Недовольный покупатель ушел домой без машины");
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    System.out.println(cars.remove(0) + " забрал довольный покупатель из 1ого потока");
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (cars) {
                    while (cars.isEmpty()) {
                        System.out.println("Машин нет (из 2ого потока)");
                        try {
                            cars.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    System.out.println(cars.remove(0) + " забрал довольный покупатель из 2ого потока");
                }
            }
        }).start();
    }
}