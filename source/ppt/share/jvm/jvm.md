% Java的JVM表示
% 王一帆

## 目录

- 初识JVM
- Java的ClassFile表示
- ClassFile的JVM表示
- JVM启动流程
	- Loading
	- Linking
		- Verification
		- Preparation
		- Resolution
	- Initailizing
- 示例

# 初识JVM

- 一次编译，到处运行 ClassFile,JVM
- 传值还是传引用？栈，堆
- 字符串比较 常量池
- 初始化顺序 加载(双亲委托模型)，初始化
- GC YoungGC,FullGC,eden,survive,old,perm
- PC,栈帧，指令,方法区

# 参考资料

- [The Java Language Specification, Java SE 8 Edition](https://docs.oracle.com/javase/specs/jls/se8/jls8.pdf)
- [The Java Virtual Machine Specification, Java SE 8 Edition](https://docs.oracle.com/javase/specs/jvms/se8/jvms8.pdf)
- [Inside the Java 2 Virtual Machine](http://www.artima.com/insidejvm/ed2/index.html)
- [深入理解Java虚拟机-JVM高级特性与最佳实践](https://book.douban.com/subject/6522893/)
- [自己动手写Java虚拟机](https://book.douban.com/subject/26802084/)

# 谢谢
