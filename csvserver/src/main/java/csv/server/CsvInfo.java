package csv.server;

import java.util.Date;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
public class CsvInfo {
    private final long value;

    public CsvInfo() {
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public CsvInfo(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return new Date((value() - 2208988800L) * 1000L).toString();
    }
}
