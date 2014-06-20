package at.rseiler.concept.mock;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MockProxyTest {

    @Test
    public void testFooInterfaceProxy() throws Exception {
        // create the mock
        FooInterface fooInterfaceMock = MockProxy.mock(FooInterface.class);

        // returns null because no return value is defined
        assertEquals(null, fooInterfaceMock.foo());

        // sets a return value for foo
        MockProxy.when(fooInterfaceMock.foo()).thenReturn("Foo Fighters!");
        assertEquals("Foo Fighters!", fooInterfaceMock.foo());

        // sets a return value for echo("echo")
        MockProxy.when(fooInterfaceMock.echo("echo")).thenReturn("echo");
        assertEquals("echo", fooInterfaceMock.echo("echo"));

        // sets a return value for echo("hello")
        MockProxy.when(fooInterfaceMock.echo("hello")).thenReturn("world");
        assertEquals("world", fooInterfaceMock.echo("hello"));
        // still the echo("echo") call works because the MockCgLib impl. supports different argument -> return values
        assertEquals("echo", fooInterfaceMock.echo("echo"));
    }

}
