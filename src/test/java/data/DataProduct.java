package data;

import lombok.Data;

@Data
public class DataProduct {
    private final String productName = "Апельсин";
    private final String productName1 = "Капуста";
    private final String productName2 = "Помидор";
    private final String productName3 = "Яблоко";

    private final String productExoticFruit = "Манго";
    private final String productFruit = "Банан";
    private final String productExoticVegetable = "Кивано";
    private final String productVegetable = "Картофель";
    private final String emptyField = "";

    private final String productTypeFruit = "FRUIT";
    private final String productTypeVeg = "VEGETABLE";
}
