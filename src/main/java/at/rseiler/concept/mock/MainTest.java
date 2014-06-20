package at.rseiler.concept.mock;

import org.mockito.Mockito;

/**
 * This class shows that the API of all three implementations (MockProxy, MockCgLib, Mockito) are similar.
 *
 * @author reinhard.seiler@gmail.com
 * @see {@link MockCgLib} and @see {@link MockProxy} for more details and comments.
 */
public class MainTest {

    public static void main(String[] args) {

        // java.lang.reflect.Proxy
        System.out.println("# MockProxy");
        FooInterface fooInterfaceMock = MockProxy.mock(FooInterface.class);
        MockProxy.when(fooInterfaceMock.foo()).thenReturn("Foo Fighters!");
        System.out.println(fooInterfaceMock.foo());
        MockProxy.when(fooInterfaceMock.echo("echo")).thenReturn("echo");
        System.out.println(fooInterfaceMock.echo("echo"));
        MockProxy.when(fooInterfaceMock.echo("hello")).thenReturn("world");
        System.out.println(fooInterfaceMock.echo("hello"));
        System.out.println();

        // cglib Enhancer
        System.out.println("# MockCgLib");
        Foo fooMock = MockCgLib.mock(Foo.class);
        MockCgLib.when(fooMock.foo()).thenReturn("Foo Fighters!");
        System.out.println(fooMock.foo());
        MockCgLib.when(fooMock.echo("echo")).thenReturn("echo");
        System.out.println(fooMock.echo("echo"));
        MockCgLib.when(fooMock.echo("hello")).thenReturn("world");
        System.out.println(fooMock.echo("hello"));
        System.out.println();

        // Mockito
        System.out.println("# Mockito");
        Foo fooMockito = Mockito.mock(Foo.class);
        Mockito.when(fooMockito.foo()).thenReturn("Foo Fighters!");
        System.out.println(fooMockito.foo());
        Mockito.when(fooMockito.echo("echo")).thenReturn("echo");
        System.out.println(fooMockito.echo("echo"));
        Mockito.when(fooMockito.echo("hello")).thenReturn("world");
        System.out.println(fooMockito.echo("hello"));
        System.out.println();

        // cglib Enhancer
        System.out.println("# MockCgLib::spy");
        Foo fooMockStub = MockCgLib.spy(new Foo());
        System.out.println(fooMockStub.echo("foo"));
        MockCgLib.when(fooMockStub.echo("foo")).thenReturn("bar");
        System.out.println(fooMockStub.echo("foo"));
        System.out.println();

        // Mockito
        System.out.println("# Mockito::spy");
        Foo fooMockitoStub = Mockito.spy(new Foo());
        System.out.println(fooMockitoStub.echo("foo"));
        Mockito.when(fooMockitoStub.echo("foo")).thenReturn("bar");
        System.out.println(fooMockitoStub.echo("foo"));
        System.out.println();

    }

}
