package callback;

import org.apache.poi.ss.usermodel.Row;

@FunctionalInterface
public interface ReadWithResultHandler<T> {
    T read(Row row);
}