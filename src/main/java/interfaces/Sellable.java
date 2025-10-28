package interfaces;

import java.math.BigDecimal;

public interface Sellable {
    BigDecimal getPrice();

    String getDescription();

    boolean isAvailable();
}
