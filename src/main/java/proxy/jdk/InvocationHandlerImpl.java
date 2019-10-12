package proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by ma peiliang
 * Create Date: 2018/7/16 14:46
 * Description: jdk动态代理实现类
 */
public class InvocationHandlerImpl implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        method.invoke(proxy,args);
        return null;
    }
}
