package at.rseiler.concept.mock;

import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a very simple implementation of a mock framework. It's based on
 * a CgLib proxy. The API is similar to the API of Mockito.
 * The idea is not to write an own mock framework, but to understand how
 * proxy based frameworks work.
 *
 * @author reinhard.seiler@gmail.com
 */
public class MockCgLib {

    private static AbstractMockMethodInterceptor lastMockInvocationHandler;

    /**
     * Creates a mock based on a class.
     *
     * @param clazz the class of the mock
     * @param <T>   the type of the mock
     * @return the mock object
     */
    public static <T> T mock(Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new MockMethodInterceptor());
        return (T) enhancer.create();
    }

    /**
     * Wraps the object into a proxy.
     *
     * @param obj the object
     * @param <T> the type of the object
     * @return the proxy
     */
    public static <T> T spy(Object obj) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(obj.getClass());
        enhancer.setCallback(new SpyMethodInterceptor());
        return (T) enhancer.create();
    }

    /**
     * This class is just needed to ERHALTEN the type information. So that we
     * have the type information for the When::thenReturn method.
     *
     * @param obj the value which we doesn't need.
     * @param <T> the type of the return value
     * @return an instance of When with the correct type information
     */
    public static <T> When<T> when(T obj) {
        return new When<>();
    }

    public static class When<T> {

        /**
         * Sets the return value for the last method call.
         *
         * @param retObj the return value
         */
        public void thenReturn(T retObj) {
            lastMockInvocationHandler.setRetObj(retObj);
        }

    }

    private static abstract class AbstractMockMethodInterceptor implements MethodInterceptor {

        private Method lastMethod;
        private Object[] lastArgs;
        private List<DataHolder> dataHolders = new ArrayList<>();

        /**
         * Intercepts the method call and decides what value will be returned.
         */
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            lastMockInvocationHandler = this;
            lastMethod = method;
            lastArgs = args;

            // checks if the method was already called with the given arguments
            for (DataHolder dataHolder : dataHolders) {
                if (dataHolder.getMethod().equals(method) && Arrays.deepEquals(dataHolder.getArgs(), args)) {
                    // if so than return the stored value
                    return dataHolder.getRetObj();
                }
            }

            // otherwise returns the default value
            return getVal(obj, args, proxy);
        }

        /**
         * Gets the default value of the method call.
         */
        public abstract Object getVal(Object obj, Object[] args, MethodProxy proxy);

        /**
         * Adds the return value for the last called method with the last given arguments.
         *
         * @param retObj the return value
         */
        public void setRetObj(Object retObj) {
            dataHolders.add(new DataHolder(lastMethod, lastArgs, retObj));
        }

    }


    private static class MockMethodInterceptor extends AbstractMockMethodInterceptor {

        /**
         * If we mock a class or interface the only value we can return is null.
         */
        @Override
        public Object getVal(Object obj, Object[] args, MethodProxy proxy) {
            return null;
        }

    }

    private static class SpyMethodInterceptor extends AbstractMockMethodInterceptor {

        /**
         * If we mock an object than we just call the method from this object.
         */
        @Override
        public Object getVal(Object obj, Object[] args, MethodProxy proxy) {
            try {
                return proxy.invokeSuper(obj, args);
            } catch (Throwable throwable) {
                return null;
            }
        }

    }

    /**
     * Stores the method with it's arguments and the return value.
     */
    private static class DataHolder {
        private final Object[] args;
        private final Method method;
        private final Object retObj;

        private DataHolder(Method method, Object[] args, Object retObj) {
            this.args = args;
            this.method = method;
            this.retObj = retObj;
        }

        private Object[] getArgs() {
            return args;
        }

        private Method getMethod() {
            return method;
        }

        private Object getRetObj() {
            return retObj;
        }
    }

}
