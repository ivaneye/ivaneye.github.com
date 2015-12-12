---
layout: post
title: 读源码-JUnit3实现
categories: [junit3]
tags: [opensource,junit3,java]
avatarimg: "/img/head.jpg"
author: Ivan

---

# 内容回顾

- 上篇内容主要介绍了JUnit3的使用
- 如何编写测试类
- 如何运行单个测试类
- 如何运行多个测试类
- 如何设置Class级别的setUp()和tearDown()方法
- 如何多次运行测试

本篇文章将梳理JUnit3源码

# 代码结构

上篇最后给出了测试UML图

![]({{site.CDN_PATH}}/assets/opensource/junit3/diagram2.png)

我们可以看出

- 所有的测试类都继承了TestCase这个类
- TestCase这个类继承了Assert类，实现了Test接口

# Assert类

我们先看Assert这个类，你会发现这个类很简单，提供了各种静态的assert*()方法来进行比较。

# Test接口

```java
package junit.framework;

public interface Test {
	public abstract int countTestCases();
	public abstract void run(TestResult result);
}
```

<!-- more -->

Test接口有两个方法

- countTestCases()方法:统计需要运行的测试的数量
- run(result)方法:顾名思义就是用来跑测试的

# TeseCase类

TestCase实现了Test接口，那么肯定需要实现如上两个方法

```java
public abstract class TestCase extends Assert implements Test {

  ...

  public int countTestCases() {
		return 1;
	}

  public void run(TestResult result) {
		result.run(this);
	}

  ...

}
```

- countTestCases()方法返回1
- run(result)方法中，是调用TestResult()中的run(Test)方法

那么问题来了？为什么要传入一个TestResult，然后再将自身传递给TestResult去执行呢？下篇分析！

# TestResult类

TestResult类看名字也能才出来，是用来收集测试结果的类.

```java
protected void run(final TestCase test) {
  startTest(test);
  Protectable p= new Protectable() {
    public void protect() throws Throwable {
      test.runBare();
    }
  };
  runProtected(test, p);

  endTest(test);
}

public void runProtected(final Test test, Protectable p) {
  try {
    p.protect();
  }catch (AssertionFailedError e) {
    addFailure(test, e);
  }catch (ThreadDeath e) { // don't catch ThreadDeath by accident
    throw e;
  }catch (Throwable e) {
    addError(test, e);
  }
}
```

- startTest(test)方法是触发添加的Listener的
- 后面构建了一个Protectable对象，里面运行test.runBare()
- 调用runProtected方法，你会发现runProtected方法就是调用Protectable的protect方法。是不是有点多此一举？继续留悬念，后面解释!
- endTest(test)方法也是触发添加的Listener的

# TestCase.runBare()

我们再次回到了TestCase方法，看看runBare()方法

```java
public void runBare() throws Throwable {
  Throwable exception= null;
  setUp();
  try {
    runTest();
  } catch (Throwable running) {
    exception= running;
  } finally {
    try {
      tearDown();
    } catch (Throwable tearingDown) {
      if (exception == null) exception= tearingDown;
    }
  }
  if (exception != null) throw exception;
}
```

- 从这里的代码，我们终于看到了测试执行的一个大致流程
- 先执行setUp()方法，接着runTest()，最后tearDown()。是不是就是我们的测试执行流程呢？

我们分别来看看这三个方法在TestCase中的实现:

- setUp()

```java
protected void setUp() throws Exception {
}
```

- tearDown()

```java
protected void tearDown() throws Exception {
}
```

上面两个方法主要就是给子类覆盖的！

- runTest()

```java
protected void runTest() throws Throwable {
  assertNotNull(fName); // Some VMs crash when calling  getMethod(null,null);
  Method runMethod= null;
  try {
    // use getMethod to get all public inherited
    // methods. getDeclaredMethods returns all
    // methods of this class but excludes the
    // inherited ones.
    runMethod= getClass().getMethod(fName, (Class[])null);
  } catch (NoSuchMethodException e) {
    fail("Method \""+fName+"\" not found");
  }
  if (!Modifier.isPublic(runMethod.getModifiers())) {
    fail("Method \""+fName+"\" should be public");
  }

  try {
    runMethod.invoke(this, (Object[])new Class[0]);
  } catch (InvocationTargetException e) {
    e.fillInStackTrace();
    throw e.getTargetException();
  } catch (IllegalAccessException e) {
    e.fillInStackTrace();
    throw e;
  }
}
```

这里就是通过反射来获取方法进而执行！

# 入口

如上的代码，只是测试结构代码。如何执行上面的测试代码呢？必然有个main方法啊！

在上篇中通过命令行执行

```sh
java junit.textui.TestRunner org.ivan.TestAll
```

可以看到，入口类为TestRunner

# TestRunner类

```java
public static void main(String args[]) {
  TestRunner aTestRunner= new TestRunner();
  try {
    TestResult r= aTestRunner.start(args);
    if (!r.wasSuccessful())
    System.exit(FAILURE_EXIT);
    System.exit(SUCCESS_EXIT);
  } catch(Exception e) {
    System.err.println(e.getMessage());
    System.exit(EXCEPTION_EXIT);
  }
}

public TestResult start(String args[]) throws Exception {
  String testCase= "";
  String method= "";
  boolean wait= false;

  for (int i= 0; i < args.length; i++) {
    if (args[i].equals("-wait"))
    wait= true;
    else if (args[i].equals("-c"))
    testCase= extractClassName(args[++i]);
    else if (args[i].equals("-m")) {
    String arg= args[++i];
    int lastIndex= arg.lastIndexOf('.');
    testCase= arg.substring(0, lastIndex);
    method= arg.substring(lastIndex + 1);
  } else if (args[i].equals("-v"))
    System.err.println("JUnit " + Version.id() + " by Kent Beck and Erich Gamma");
    else
    testCase= args[i];
  }

  if (testCase.equals(""))
    throw new Exception("Usage: TestRunner [-wait] testCaseName, where name is the name of the TestCase class");

  try {
    if (!method.equals(""))
    return runSingleMethod(testCase, method, wait);
    Test suite= getTest(testCase);
    return doRun(suite, wait);
  } catch (Exception e) {
    throw new Exception("Could not create and run test suite: " + e);
  }
}
```

- TestRunner中的核心方法为start方法，主要解析传递的参数，并执行相应的测试
- 其中Test suite= getTest(testCase);返回的实际上是TestSuite。也就是说，即使你不将TestCase添加到TestSuite中，JUnit也会自动帮你进行封装
- doRun就是实际的运行测试的方法了

```java
public TestResult doRun(Test suite, boolean wait) {
  TestResult result= createTestResult();
  result.addListener(fPrinter);
  long startTime= System.currentTimeMillis();
  suite.run(result);
  long endTime= System.currentTimeMillis();
  long runTime= endTime-startTime;
  fPrinter.print(result, runTime);

  pause(wait);
  return result;
}
```

- 首先实例化一个TestResult，然后添加Listener
- 执行TestSuite的run方法
- 输出

# TestSuite

```java
public void run(TestResult result) {
for (Enumeration e= tests(); e.hasMoreElements(); ) {
  if (result.shouldStop() )
    break;
    Test test= (Test)e.nextElement();
    runTest(test, result);
  }
}

public void runTest(Test test, TestResult result) {
  test.run(result);
}
```

- 我们知道TestSuite中是Test的集合，这里run方法，就是遍历TestSuite中的Test并调用runTest方法
- 而runTest方法实际上就是调用test的run方法

那Test是如何添加到TestSuite中的呢？这个动作是在实例化TestSuite时进行的!

```java
public TestSuite(final Class theClass) {
  fName= theClass.getName();
  try {
    getTestConstructor(theClass); // Avoid generating multiple error messages
  } catch (NoSuchMethodException e) {
    addTest(warning("Class "+theClass.getName()+" has no public constructor TestCase(String name) or TestCase()"));
    return;
  }

  if (!Modifier.isPublic(theClass.getModifiers())) {
    addTest(warning("Class "+theClass.getName()+" is not public"));
    return;
  }

  Class superClass= theClass;
  Vector names= new Vector();
  while (Test.class.isAssignableFrom(superClass)) {
    Method[] methods= superClass.getDeclaredMethods();
    for (int i= 0; i < methods.length; i++) {
      addTestMethod(methods[i], names, theClass);
    }
    superClass= superClass.getSuperclass();
  }
  if (fTests.size() == 0)
  addTest(warning("No tests found in "+theClass.getName()));
}
```

- 核心功能就是addTestMethod方法

```java
private void addTestMethod(Method m, Vector names, Class theClass) {
  String name= m.getName();
  if (names.contains(name))
    return;
  if (!isPublicTestMethod(m)) {
    if (isTestMethod(m))
      addTest(warning("Test method isn't public: "+m.getName()));
    return;
  }
  names.addElement(name);
  addTest(createTest(theClass, name));
}

private boolean isTestMethod(Method m) {
	String name= m.getName();
	Class[] parameters= m.getParameterTypes();
	Class returnType= m.getReturnType();
	return parameters.length == 0 && name.startsWith("test") && returnType.equals(Void.TYPE);
}
```

- isTestMethod方法用来判断是否为测试方法
- 是测试方法的条件是:方法没有参数，方法名以test开头，且没有返回值
- 如果是测试方法，则添加到Test集合里

这就是整个JUnit的执行流程！

# 时序图

![]({{site.CDN_PATH}}/assets/opensource/junit3/seq.png)


# UML

![]({{site.CDN_PATH}}/assets/opensource/junit3/diagram.png)
