### Concurrent 容器

- ConcurrentHashMap
> 并发HashMap实现，数组+链表+红黑树
- ConcurrentSkipListMap
> 跳表实现的并发Map容器
- ConcurrentLinkedQueue/ConcurrentLinkedDeque
> 并发的无界队列实现
- CopyOnWriteArrayList /CopyOnWriteSet
> 并发的ArrayList/set实现，使用了COW机制，即在写的时候进行数组复制来避免多线程并发安全。不过该方法只适合写的次数很少。不然会创建很多数组浪费内存和时间