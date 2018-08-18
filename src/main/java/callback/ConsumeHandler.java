package callback;

import org.apache.poi.ss.usermodel.Row;

@FunctionalInterface
public interface ConsumeHandler<T> {
    T read(Row row);
}
