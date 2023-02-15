package idea.verlif.mock.data.exception;

public class MockDataException extends RuntimeException {

    public MockDataException(String msg) {
        super(msg);
    }

    public MockDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public MockDataException(Throwable t) {
        super(t);
    }
}
