package at.rseiler.concept.mock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a very simple implementation of a mock framework. It's based on
 * {@link Proxy}. The API is similar to the API of Mockito.
 * This mock class is limited to mock interfaces. Because of the limitation
 * of the {@link Proxy}.
 * The idea is not to write an own mock framework, but to understand how
 * proxy based frameworks work.
 *
 * @author reinhard.seiler@gmail.com
 */
public class MockProxy {

    private static MockInvocationHandler lastMockInvocationHandler;

    /**
     * Creates a mock based on a class.
     *
     * @param clazz the class of the mock
     * @param <T>   the type of the mock
     * @return the mock object
     */
    public static <T> T mock(Class<T> clazz) {
        MockInvocationHandler invocationHandler = new MockInvocationHandler();
        T proxy = (T) Proxy.newProxyInstance(MockProxy.class.getClassLoader(), new Class[]{clazz}, invocationHandler);
        return proxy;
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

    private static class MockInvocationHandler implements InvocationHandler {

        private Method lastMethod;
        private Object[] lastArgs;
        private List<DataHolder> dataHolders = new ArrayList<>();

        /**
         * Intercepts the method call and decides what value will be returned.
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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

            // otherwise return null
            return null;
        }

        /**
         * Adds the return value for the last called method with the last given arguments.
         *
         * @param retObj the return value
         */
        public void setRetObj(Object retObj) {
            dataHolders.add(new DataHolder(lastMethod, lastArgs, retObj));
        }

        /**
         * Stores the method with it's arguments and the return value.
         */
        private class DataHolder {
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

}
