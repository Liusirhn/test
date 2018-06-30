�ڿ͵۹�



����ʽ����BlockingQueue                                   

���ƴ���
 1 package producerConsumer;
 2 
 3 import java.util.concurrent.BlockingQueue;
 4 import java.util.concurrent.LinkedBlockingQueue;
 5 
 6 //ʹ����������BlockingQueue���������������
 7 public class BlockingQueueConsumerProducer {
 8     public static void main(String[] args) {
 9         Resource3 resource = new Resource3();
10         //�������߳�
11         ProducerThread3 p = new ProducerThread3(resource);
12         //���������
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
24  * �������߳�
25  * @author tangzhijing
26  *
27  */
28 class ConsumerThread3 extends Thread {
29     private Resource3 resource3;
30  
31     public ConsumerThread3(Resource3 resource) {
32         this.resource3 = resource;
33         //setName("������");
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
48  * �������߳�
49  * @author tangzhijing
50  *
51  */
52 class ProducerThread3 extends Thread{
53     private Resource3 resource3;
54     public ProducerThread3(Resource3 resource) {
55         this.resource3 = resource;
56         //setName("������");
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
73      * ����Դ���������Դ
74      */
75     public void add(){
76         try {
77             resourceQueue.put(1);
78             System.out.println("������" + Thread.currentThread().getName()
79                     + "����һ����Դ," + "��ǰ��Դ����" + resourceQueue.size() + 
80                     "����Դ");
81         } catch (InterruptedException e) {
82             e.printStackTrace();
83         }
84     }
85     /**
86      * ����Դ�����Ƴ���Դ
87      */
88     public void remove(){
89         try {
90             resourceQueue.take();
91             System.out.println("������" + Thread.currentThread().getName() + 
92                     "����һ����Դ," + "��ǰ��Դ����" + resourceQueue.size() 
93                     + "����Դ");
94         } catch (InterruptedException e) {
95             e.printStackTrace();
96         }
97     }
98 }





2����ʽһ��synchronized��wait��notify                

���ƴ���
  1 package producerConsumer;
  2 //wait �� notify
  3 public class ProducerConsumerWithWaitNofity {
  4     public static void main(String[] args) {
  5         Resource resource = new Resource();
  6         //�������߳�
  7         ProducerThread p1 = new ProducerThread(resource);
  8         ProducerThread p2 = new ProducerThread(resource);
  9         ProducerThread p3 = new ProducerThread(resource);
 10         //�������߳�
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
 27  * ������Դ��
 28  * @author 
 29  *
 30  */
 31 class Resource{//��Ҫ
 32     //��ǰ��Դ����
 33     private int num = 0;
 34     //��Դ���������ŵ���Դ��Ŀ
 35     private int size = 10;
 36 
 37     /**
 38      * ����Դ����ȡ����Դ
 39      */
 40     public synchronized void remove(){
 41         if(num > 0){
 42             num--;
 43             System.out.println("������" + Thread.currentThread().getName() +
 44                     "����һ����Դ��" + "��ǰ�̳߳���" + num + "��");
 45             notifyAll();//֪ͨ������������Դ
 46         }else{
 47             try {
 48                 //���û����Դ���������߽���ȴ�״̬
 49                 wait();
 50                 System.out.println("������" + Thread.currentThread().getName() + "�߳̽���ȴ�״̬");
 51             } catch (InterruptedException e) {
 52                 e.printStackTrace();
 53             }
 54         }
 55     }
 56     /**
 57      * ����Դ���������Դ
 58      */
 59     public synchronized void add(){
 60         if(num < size){
 61             num++;
 62             System.out.println(Thread.currentThread().getName() + "����һ����Դ����ǰ��Դ����" 
 63             + num + "��");
 64             //֪ͨ�ȴ���������
 65             notifyAll();
 66         }else{
 67             //�����ǰ��Դ������10����Դ
 68             try{
 69                 wait();//�����߽���ȴ�״̬�����ͷ���
 70                 System.out.println(Thread.currentThread().getName()+"�߳̽���ȴ�");
 71             }catch(InterruptedException e){
 72                 e.printStackTrace();
 73             }
 74         }
 75     }
 76 }
 77 /**
 78  * �������߳�
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
 98  * �������߳�
 99  */
100 class ProducerThread extends Thread{
101     private Resource resource;
102     public ProducerThread(Resource resource){
103         this.resource = resource;
104     }
105     @Override
106     public void run() {
107         //���ϵ�������Դ
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




