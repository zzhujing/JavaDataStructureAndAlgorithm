### JUC - 原子类型

#### `AtomicXXX`

> 底层使用`Unsafe`包中的`compareAndSwapXX()`方法调用`CPU`汇编指令进行原子性操作

  `ex : AtomicInteger#Api`
  
```java
public class AtomicInteger extends Number implements java.io.Serializable {

    // setup to use Unsafe.compareAndSwapInt for updates
    //核心使用Unsafe包进行操作
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    //偏移量
    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                (AtomicInteger.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }
    //volatile修饰
    private volatile int value;
    
    //get
    public final int get() {
        return value;
    }
    //set
    public final void set(int newValue) {
        value = newValue;
    }

    //最终一致性的set
    public final void lazySet(int newValue) {
        unsafe.putOrderedInt(this, valueOffset, newValue);
    }

    //原子性的设置newValue，并且返回oldValue
    public final int getAndSet(int newValue) {
        return unsafe.getAndSetInt(this, valueOffset, newValue);
    }
    //使用CAS进行期望值和当前value进行比较，若相同则设置为update
    public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }
    //强行并且不保证保护的CAS
    public final boolean weakCompareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }
    
    //获取oldValue并且当前value+1
    public final int getAndIncrement() {
        return unsafe.getAndAddInt(this, valueOffset, 1);
    }
    //获取oldValue并且当前value-1
    public final int getAndDecrement() {
        return unsafe.getAndAddInt(this, valueOffset, -1);
    }
    //获取oldValue，并且当前值+上传入值
    public final int getAndAdd(int delta) {
        return unsafe.getAndAddInt(this, valueOffset, delta);
    }
    
    public final int incrementAndGet() {
        return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
    }

    public final int decrementAndGet() {
        return unsafe.getAndAddInt(this, valueOffset, -1) - 1;
    }

    public final int addAndGet(int delta) {
        return unsafe.getAndAddInt(this, valueOffset, delta) + delta;
    }
    //获取oldValue,并且根据传入的function修改当前value
    public final int getAndUpdate(IntUnaryOperator updateFunction) {
        int prev, next;
        do {
            prev = get();
            next = updateFunction.applyAsInt(prev);
        } while (!compareAndSet(prev, next));
        return prev;
    }

    public final int updateAndGet(IntUnaryOperator updateFunction) {
        int prev, next;
        do {
            prev = get();
            next = updateFunction.applyAsInt(prev);
        } while (!compareAndSet(prev, next));
        return next;
    }
    //获取oldValue,并且根据传入的BiFunction修改当前value
    public final int getAndAccumulate(int x,
                                      IntBinaryOperator accumulatorFunction) {
        int prev, next;
        do {
            prev = get();
            next = accumulatorFunction.applyAsInt(prev, x);
        } while (!compareAndSet(prev, next));
        return prev;
    }
    
    public final int accumulateAndGet(int x,
                                      IntBinaryOperator accumulatorFunction) {
        int prev, next;
        do {
            prev = get();
            next = accumulatorFunction.applyAsInt(prev, x);
        } while (!compareAndSet(prev, next));
        return next;
    }
}
```

---
> 其中AtomicReference可以将自定义的Object进行原子操作，类似的还有AtomicXXXArray

### AtomicXXx会产生的`ABA`问题

> 对于基本类型这样的先后顺序无所谓，但是对于一些特殊的数据结构，比如栈，一些引用类型。在另外一个线程进行CAS并且进行一些其他的操作之后再将 volatile修饰的变量设置为t1的期望值，则会出现错误
比如有一个包含A,B两节点的双向链表
- t1 -> 将A弹出栈顶并获取到B的引用  compareAndSet(A,B)
- t2 -> 在此过程中将A,B都弹出，然后依次push D,C,A
- t1 -> 抢到执行权，发现还是栈顶还是A的引用。则继续执行获取B，那么此时就获取不到了。产生错误。
- 对于此种情况，Juc提供了`AtomicStampedReference`

实例Demo
```java
public class AtomicStampReferenceTest {
    
    //使用了AtomicStampReference来解决`ABA`问题
    @Test
    public void testStampReference() throws InterruptedException {
        AtomicStampedReference<Simple> stampRef = new AtomicStampedReference<>(new Simple("hujing"), 0);
        final Simple xcc = new Simple("xcc");
        final Simple ref = stampRef.getReference();
        final int stamp = stampRef.getStamp();
        final Thread t1 = new Thread(() -> {
            System.out.println(stampRef.compareAndSet(ref, xcc, stampRef.getStamp(), stampRef.getStamp() + 1)); //true
            System.out.println(stampRef.compareAndSet(xcc, ref, stampRef.getStamp(), stampRef.getStamp() + 1)); //true
        });
        t1.start();
        t1.join();
        System.out.println(stampRef.compareAndSet(ref, xcc, stamp, stamp + 1)); //false
    }

    @Test
    public void testOrdinalReference() throws InterruptedException {
        AtomicReference<Simple> atomicRef = new AtomicReference<>(new Simple("hujing"));
        final Simple xcc = new Simple("xcc");
        final Simple ref = atomicRef.get();
        final Thread t1 = new Thread(() -> {
            //此时经过多次改变，最后返回的引用不变,如果这里的Simple为栈或者队列，链表等数据结构则会有问题
            System.out.println(atomicRef.compareAndSet(ref, xcc)); //true
            System.out.println(atomicRef.compareAndSet(xcc, ref)); //true
        });
        t1.start();
        t1.join();
        System.out.println(atomicRef.compareAndSet(ref, xcc)); //true
    }


    static class Simple {
        private String name;

        public Simple(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
```

#### `AtomicXxxxFiledUpdater`

> 这种主要是针对原生AtomicXX在数量太多额外消费内存的问题, 他可以为某个类的属性赋予原子操作 ， 可以使用他来避免显示申明锁和使用synchronized

```java
public class AtomicXXFieldUpdaterTest {

    private volatile Integer i;
    private final AtomicIntegerFieldUpdater<AtomicXXFieldUpdaterTest> updater = AtomicIntegerFieldUpdater.newUpdater(AtomicXXFieldUpdaterTest.class, "i");

    @Test
    public void testFieldUpdater() {

        AtomicXXFieldUpdaterTest test = new AtomicXXFieldUpdaterTest();

        for (int j = 0; j < 2; j++) {
            new Thread(() -> {
                final int MAX = 20;
                while (updater.getAndIncrement(test) < MAX) {
                    System.out.println(Thread.currentThread().getName() + "->" + updater.get(test));
                }
            }).start();
        }
    }
}
```

  使用注意点：
- `AtomicXXFieldUpdater`修饰的属性如果不在该类中，那么不能使用`private`和`protected`关键词修饰。
- 修饰的属性必须要是`volatile`类型

 相比于普通的`AtomicXxx`来说具备：
- 对于`AtomicXX`类型来说更加节省内存，尤其是面对那种链表数据结构的
- 能让其他类的volatile字段属性具有原子性。


 简单比较各种并发手段的执行效率
![执行效率简单比较](../src/main/java/com/concurrent/juc/unsafe/UnsafeTest.java)

#### Unsafe类的一些简单使用

- 避开类的初始化直接调用`allocateInstance()`分配对象内存
```
final Unsafe unsafe = UnsafeTest.getUnsafe();
final Simple s = (Simple) unsafe.allocateInstance(Simple.class);
```
- 动态改变属性值`putXxx()`,`putInt()`,`putObject`..
```
Field f = Simple.class.getDeclaredField("i");
unsafe.putInt(work, unsafe.objectFieldOffset(f), 43);
work.work();
```
- 也可以直接使用`defineClass()`加载类
 
 
 
 

    
