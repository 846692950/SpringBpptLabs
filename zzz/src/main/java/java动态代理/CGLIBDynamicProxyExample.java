package java动态代理;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB动态代理的核心原理是通过创建目标类的子类来实现代理。
 * 它通过修改字节码的方式，在运行时动态生成目标类的子类，并重写其中的方法，以实现代理逻辑。
 * 因此，在使用CGLIB动态代理时，目标类和方法不能被final修饰，以及其它的一些影响继承的因素。
 *
 * CGLIB动态代理的主要步骤包括：
 * 1.找到目标类（即需要代理的类）。
 * 2.通过字节码技术创建目标类的子类。
 * 3.在子类中重写目标方法，并在重写的方法中添加代理逻辑。
 * 4.使用生成的子类来创建代理对象。
 */
public class CGLIBDynamicProxyExample {
    public static void main(String[] args) {
        //3.1 创建Enhancer对象，用于创建代理类
        Enhancer enhancer = new Enhancer();
        //3.2 设置父类（目标类）
        enhancer.setSuperclass(TargetClass.class);
        //3.3 设置回调对象
        enhancer.setCallback(new CustomInterceptor());
        //3.4 创建代理类实例
        TargetClass proxy = (TargetClass) enhancer.create();
        //3.5 调用代理类的方法
        proxy.doSomething();
    }
}

/**
 *1.要代理的目标类
 */
class TargetClass {
    public void doSomething() {
        System.out.println("漫无目的的游荡！");
    }
}

/**
 *2.实现MethodInterceptor接口作为代理类的回调
 */
class CustomInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method,
                            Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("Before：出门");
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("After：回家");
        return result;
    }
}

