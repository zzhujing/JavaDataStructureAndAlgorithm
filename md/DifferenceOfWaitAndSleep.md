### wait和Sleep方法的区别

- `wait()`方法是`Object`类的方法 , `sleep()`是`Thread`类拥有的方法
- `wait()`会释放当前锁到锁队列中,`sleep()`不会
- 使用`wait()`的时候当前线程必须是锁的持有者,`sleep()`不需要
- `wait()`需要唤醒`sleep()`不需要