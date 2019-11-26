### 阻塞队列

#### `ArrayBlockingQueue` 

> 基于数组实现的有边界的阻塞队列，阻塞是利于`ReetrantLock` + `Condition` 来实现的。

- `take() : ` 取出一个元素，若队列为空则阻塞
- `put()  ：` 添加一个元素，若队列满的则阻塞
- `peek()/poll() : ` 前者为查看队尾元素，若队列为空则返回`null` ， 后者为查看并移除队尾元素，若队列为空则抛`NoSuchElementExecpption` 
- `element()/remove() : ` 前者为`peek()` 扩充，且返回值为空抛出异常 , 后者类似
- `add() / offer() : ` 都为添加，相比`put() ` 阻塞，前者超过长度会抛出`IllegalStateExeception`，后者则会返回`false`

#### `PriorityBlockingQueue`

> 无边界且会按照传入或者默认顺序排序的优先阻塞队列，底层为数组实现。会自动扩容

- `take() ： `会阻塞

#### `LinkedBlockingQueue`

>基于单链表实现的阻塞队列，可选边界

#### `SynchronousQueue`

> 同步阻塞队列，不储存元素，通过`Trahsfer`来确保数据一定有消费。

- `put() : ` 会阻塞直到有消费者取出元素
- `take() ：`会阻塞知道有生产者生产元素

#### `LinkedBlockingDeque`

> 双向链表实现的阻塞队列 ，多了一些`addFirst()` , `removeLast()`，`takeFirst()`

#### `DelayQueue`

> 有过期时间的阻塞队列，其中元素必须实现`Delayed`接口。会根据其中的`getDelay()`方法判断是否过期，以及通过`compareTo()`方法来对过期时间长短进行排序

- `take() : ` 若队列中没有过期元素那么会阻塞。

- `poll() / peek() :` 后者会直接获取队尾元素，前者会调用`peek()`方法查看是否为`null`且是否过期，若没过期则返回`null`

- `iterator() ： `遍历可以直接快速返回

  ####  

#### `LinkedTransferQueue`

> 基于链表的无界阻塞队列，并且在生产消费的同时支持保证生产时一定消费，消费时一定有生产。

- `transfer() / take():` 方法分别在没有消费者/生产者线程的时候阻塞 