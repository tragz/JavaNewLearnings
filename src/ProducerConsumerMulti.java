import java.util.*;
import java.util.concurrent.*;

public class ProducerConsumerMulti {
    public static void main(String args[]){
        BlockingQueue<Integer> sharedQueue = new LinkedBlockingQueue<Integer>(10);

        Thread prodThread  = new Thread(new Producer(sharedQueue,1));
        Thread consThread1 = new Thread(new Consumer(sharedQueue,1));
        //Thread consThread2 = new Thread(new Consumer(sharedQueue,2));

        prodThread.start();
        consThread1.start();
        //consThread2.start();
    }
}
class Producer implements Runnable {
    private final BlockingQueue<Integer> sharedQueue;
    private int threadNo;
    private Random rng;
    private int count = 100;
    public Producer(BlockingQueue<Integer> sharedQueue,int threadNo) {
        this.threadNo = threadNo;
        this.sharedQueue = sharedQueue;
        this.rng = new Random();
    }
    @Override
    public void run() {

        System.out.println("PRODUCER COUNT:" + count );
        while(count > 0){
            try {
                int number = count;
                System.out.println("Produced:" + number + ":by thread:"+ threadNo);
                sharedQueue.put(number );
                count--;
                Thread.sleep(100);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable{
    private final BlockingQueue<Integer> sharedQueue;
    private int threadNo;
    private int count;
    public Consumer (BlockingQueue<Integer> sharedQueue,int threadNo) {
        this.sharedQueue = sharedQueue;
        this.threadNo = threadNo;
        this.count = 100;
    }

    @Override
    public void run() {
        System.out.println("CONSUMER COUNT:" + count );
        while(count > 0){
            try {
                List<Integer> list = new ArrayList<Integer>();
                //Integer map = new HashMap<Integer, Integer>();
                sharedQueue.drainTo(list);
                System.out.println("SIZE:\t" + list.size() );
                for(Integer i: list) {
                    System.out.println("Consumed: "+ i + ":by thread:"+threadNo );
                }
                count -= list.size();
                System.out.println("\tRemaining capacity:\t" + sharedQueue.remainingCapacity() + "\t" + count );
                Thread.sleep(1000);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}

