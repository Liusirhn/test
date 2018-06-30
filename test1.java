黑客帝国



、方式三：BlockingQueue                                   

复制代码
 1 package producerConsumer;
 2 
 3 import java.util.concurrent.BlockingQueue;
 4 import java.util.concurrent.LinkedBlockingQueue;
 5 
 6 //使用阻塞队列BlockingQueue解决生产者消费者
 7 public class BlockingQueueConsumerProducer {
 8     public static void main(String[] args) {
 9         Resource3 resource = new Resource3();
10         //生产者线程
11         ProducerThread3 p = new ProducerThread3(resource);
12         //多个消费者
13         ConsumerThread3 c1 = new ConsumerThread3(resource);
14         ConsumerThread3 c2 = new ConsumerThread3(resource);
15         ConsumerThread3 c3 = new ConsumerThread3(resource);
16  
17         p.start();
18         c1.start();
19         c2.start();
20         c3.start();
21     }
22 }
23 /**
24  * 消费者线程
25  * @author tangzhijing
26  *
27  */
28 class ConsumerThread3 extends Thread {
29     private Resource3 resource3;
30  
31     public ConsumerThread3(Resource3 resource) {
32         this.resource3 = resource;
33         //setName("消费者");
34     }
35  
36     public void run() {
37         while (true) {
38             try {
39                 Thread.sleep((long) (1000 * Math.random()));
40             } catch (InterruptedException e) {
41                 e.printStackTrace();
42             }
43             resource3.remove();
44         }
45     }
46 }
47 /**
48  * 生产者线程
49  * @author tangzhijing
50  *
51  */
52 class ProducerThread3 extends Thread{
53     private Resource3 resource3;
54     public ProducerThread3(Resource3 resource) {
55         this.resource3 = resource;
56         //setName("生产者");
57     }
58  
59     public void run() {
60         while (true) {
61             try {
62                 Thread.sleep((long) (1000 * Math.random()));
63             } catch (InterruptedException e) {
64                 e.printStackTrace();
65             }
66             resource3.add();
67         }
68     }
69 }
70 class Resource3{
71     private BlockingQueue resourceQueue = new LinkedBlockingQueue(10);
72     /**
73      * 向资源池中添加资源
74      */
75     public void add(){
76         try {
77             resourceQueue.put(1);
78             System.out.println("生产者" + Thread.currentThread().getName()
79                     + "生产一件资源," + "当前资源池有" + resourceQueue.size() + 
80                     "个资源");
81         } catch (InterruptedException e) {
82             e.printStackTrace();
83         }
84     }
85     /**
86      * 向资源池中移除资源
87      */
88     public void remove(){
89         try {
90             resourceQueue.take();
91             System.out.println("消费者" + Thread.currentThread().getName() + 
92                     "消耗一件资源," + "当前资源池有" + resourceQueue.size() 
93                     + "个资源");
94         } catch (InterruptedException e) {
95             e.printStackTrace();
96         }
97     }
98 }





2、方式一：synchronized、wait和notify                

复制代码
  1 package producerConsumer;
  2 //wait 和 notify
  3 public class ProducerConsumerWithWaitNofity {
  4     public static void main(String[] args) {
  5         Resource resource = new Resource();
  6         //生产者线程
  7         ProducerThread p1 = new ProducerThread(resource);
  8         ProducerThread p2 = new ProducerThread(resource);
  9         ProducerThread p3 = new ProducerThread(resource);
 10         //消费者线程
 11         ConsumerThread c1 = new ConsumerThread(resource);
 12         //ConsumerThread c2 = new ConsumerThread(resource);
 13         //ConsumerThread c3 = new ConsumerThread(resource);
 14     
 15         p1.start();
 16         p2.start();
 17         p3.start();
 18         c1.start();
 19         //c2.start();
 20         //c3.start();
 21     }
 22     
 23     
 24     
 25 }
 26 /**
 27  * 公共资源类
 28  * @author 
 29  *
 30  */
 31 class Resource{//重要
 32     //当前资源数量
 33     private int num = 0;
 34     //资源池中允许存放的资源数目
 35     private int size = 10;
 36 
 37     /**
 38      * 从资源池中取走资源
 39      */
 40     public synchronized void remove(){
 41         if(num > 0){
 42             num--;
 43             System.out.println("消费者" + Thread.currentThread().getName() +
 44                     "消耗一件资源，" + "当前线程池有" + num + "个");
 45             notifyAll();//通知生产者生产资源
 46         }else{
 47             try {
 48                 //如果没有资源，则消费者进入等待状态
 49                 wait();
 50                 System.out.println("消费者" + Thread.currentThread().getName() + "线程进入等待状态");
 51             } catch (InterruptedException e) {
 52                 e.printStackTrace();
 53             }
 54         }
 55     }
 56     /**
 57      * 向资源池中添加资源
 58      */
 59     public synchronized void add(){
 60         if(num < size){
 61             num++;
 62             System.out.println(Thread.currentThread().getName() + "生产一件资源，当前资源池有" 
 63             + num + "个");
 64             //通知等待的消费者
 65             notifyAll();
 66         }else{
 67             //如果当前资源池中有10件资源
 68             try{
 69                 wait();//生产者进入等待状态，并释放锁
 70                 System.out.println(Thread.currentThread().getName()+"线程进入等待");
 71             }catch(InterruptedException e){
 72                 e.printStackTrace();
 73             }
 74         }
 75     }
 76 }
 77 /**
 78  * 消费者线程
 79  */
 80 class ConsumerThread extends Thread{
 81     private Resource resource;
 82     public ConsumerThread(Resource resource){
 83         this.resource = resource;
 84     }
 85     @Override
 86     public void run() {
 87         while(true){
 88             try {
 89                 Thread.sleep(1000);
 90             } catch (InterruptedException e) {
 91                 e.printStackTrace();
 92             }
 93             resource.remove();
 94         }
 95     }
 96 }
 97 /**
 98  * 生产者线程
 99  */
100 class ProducerThread extends Thread{
101     private Resource resource;
102     public ProducerThread(Resource resource){
103         this.resource = resource;
104     }
105     @Override
106     public void run() {
107         //不断地生产资源
108         while(true){
109             try {
110                 Thread.sleep(1000);
111             } catch (InterruptedException e) {
112                 e.printStackTrace();
113             }
114             resource.add();
115         }
116     }
117     
118 }




