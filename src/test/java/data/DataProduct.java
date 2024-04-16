package data;

import lombok.Data;

@Data
public class DataProduct {
    private final String productName = "Апельсин";
    private final String productName1 = "Капуста";
    private final String productName2 = "Помидор";
    private final String productName3 = "Яблоко";
    private final String productName4 = "Банан";

    private final String productTypeFruit = "FRUIT";
    private final String productTypeVeg = "VEGETABLE";
}
