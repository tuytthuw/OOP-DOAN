package Interfaces;

import java.math.BigDecimal;

public interface Sellable {
    BigDecimal getBasePrice();
    BigDecimal calculateFinalPrice();
    String getType();
}
