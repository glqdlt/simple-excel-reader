package callback;

import org.apache.poi.ss.usermodel.Row;

public interface ReaderHandler<T> {
    T handler(Row row);
}
