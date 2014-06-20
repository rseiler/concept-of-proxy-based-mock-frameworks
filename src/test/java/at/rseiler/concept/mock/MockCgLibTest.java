package at.rseiler.concept.mock;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MockCgLibTest {

    @Test
    public void testClassProxy() throws Exception {
        // creates the mock
        Foo fooMock = MockCgLib.mock(Foo.class);

        // returns null because no return value is defined
        assertEquals(null, fooMock.foo());
        // sets a return value for foo

        MockCgLib.when(fooMock.foo()).thenReturn("Foo Fighters!");
        assertEquals("Foo Fighters!", fooMock.foo());

        // sets a return value for echo("echo")
        MockCgLib.when(fooMock.echo("echo")).thenReturn("echo");
        Assert.assertEquals("echo", fooMock.echo("echo"));

        // sets a return value for echo("hello")
        MockCgLib.when(fooMock.echo("hello")).thenReturn("world");
        Assert.assertEquals("world", fooMock.echo("hello"));

        // still the echo("echo") call works because the MockCgLib impl. supports different argument -> return values
        Assert.assertEquals("echo", fooMock.echo("echo"));
    }

    @Test
    public void testClassSpy() throws Exception {
        // sets a return value for echo("echo")
        Foo fooMockStub = MockCgLib.spy(new Foo());

        // returns foo, because the method from the Foo object is called and returns "foo"
        assertEquals("foo", fooMockStub.echo("foo"));

        // change the return value from foo
        MockCgLib.when(fooMockStub.echo("foo")).thenReturn("bar");
        assertEquals("bar", fooMockStub.echo("foo"));

        // returns echo, because the method from the Foo object is called and returns "echo"
        assertEquals("echo", fooMockStub.echo("echo"));
    }

}
