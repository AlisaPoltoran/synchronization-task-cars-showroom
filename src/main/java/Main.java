import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<String> cars = new ArrayList<>();

        new Thread(() -> {
            synchronized (cars) {
                for (int i = 0; i < 10; i++) {
                    cars.add("Новый автомобиль  " + (i + 1));
                    cars.notifyAll();
                    System.out.println("Производитель Toyota выпустил " + cars.get(i));
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (cars) {
                    while (cars.isEmpty()) {
                        System.out.println("Машин нет");
                        try {
                            cars.wait(100);
                            System.out.println("Недовольный покупатель ушел домой без машины");
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    System.out.println(cars.remove(0) + "забрал довольный покупатель");
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                synchronized (cars) {
                    while (cars.isEmpty()) {
                        try {
                            cars.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    System.out.println("Обслужили покупателя " + cars.remove(0));
                }
            }
        }).start();
    }
}
