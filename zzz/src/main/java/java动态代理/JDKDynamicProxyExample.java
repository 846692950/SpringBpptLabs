package java动态代理;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKDynamicProxyExample {
/**
 * Java动态代理是Java语言提供的一种在运行时生成代理类和对象的机制。
 * 它允许在运行时创建代理类来代理实现了特定接口的目标对象，
 * 并将方法调用转发到目标对象，同时允许在调用方法前后添加额外的逻辑。
 *
 * JDK动态代理的实现依赖于Java的反射机制，主要涉及以下几个关键类：
 * 1.Proxy：这是用于创建代理对象的类。它提供了一个静态方法 newProxyInstance()，用于创建代理对象。
 * 2.InvocationHandler：这是代理对象的调用处理器接口。当代理对象的方法被调用时，调用处理器的 invoke() 方法会被执行，允许在调用方法前后添加自定义的逻辑。
 * 3.Method：代表代理对象的方法。
 * 4.InvocationTargetException：在 invoke() 方法中可能抛出的异常，用于封装被调用方法的异常。
 *
 * JDK动态代理要求目标对象实现至少一个接口，这是因为代理对象是基于接口来实现的。
 * 在代理对象被创建时，它会实现与目标对象相同的接口，并将方法调用转发给目标对象。
 * 如果目标对象没有实现接口，那么代理对象就无法实现相同的接口，也就无法进行代理。
 * 这种限制是因为Java动态代理是基于接口的代理，
 * 它要求代理对象实现了与目标对象相同的接口，从而保证了代理对象可以调用相同的方法签名。
 * 如果目标对象没有实现接口，代理对象就无法实现相同的接口，也就无法进行代理。
 */
    public static void main(String[] args) {
        //4.1 创建目标对象
        SomeInterface someObject = new SomeClass();
        //4.2 创建InvocationHandler实例
        InvocationHandler handler = new CustomInvocationHandler(someObject);
        //4.3 使用Proxy类创建代理对象
        SomeInterface proxy = (SomeInterface) Proxy.newProxyInstance(
                SomeInterface.class.getClassLoader(),
                new Class[]{SomeInterface.class},
                handler);

        //4.4 调用代理对象的方法
        proxy.doSomething();
    }
}

/**
 * 1.定义接口
 */
interface SomeInterface {
    void doSomething();
}

/**
 * 2.实现接口的类
 */
class SomeClass implements SomeInterface {
    @Override
    public void doSomething() {
        System.out.println("漫无目的的游荡！");
    }
}

/**
 * 3.实现InvocationHandler接口，作为代理类的处理器
 */
class CustomInvocationHandler implements InvocationHandler {
    private final SomeInterface target;
    public CustomInvocationHandler(SomeInterface target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before：出门！");
        Object result = method.invoke(target, args);
        System.out.println("After：回家！");
        return result;
    }
}

