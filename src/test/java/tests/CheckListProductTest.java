package tests;

import data.DataProduct;
import models.ProductListModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static specs.ProductSpecification.requestSpecification;
import static specs.ProductSpecification.responseSpecification200;


public class CheckListProductTest {
    DataProduct dataProduct = new DataProduct();

    @Test
    @DisplayName("Проверка дефолтного списка продуктов")
    public void checkDefaultListProducts() {

        List<ProductListModel> products = given(requestSpecification)

                .when()
                .get("/food")

                .then()
                .spec(responseSpecification200)
                .extract().body().jsonPath().getList("", ProductListModel.class);


        Assertions.assertAll(
                () -> Assertions.assertEquals(dataProduct.getProductName(), products.get(0).getName()),
                () -> Assertions.assertEquals(dataProduct.getProductTypeFruit(), products.get(0).getType()),
                () -> Assertions.assertTrue(products.get(0).isExotic()),
                () -> Assertions.assertEquals(dataProduct.getProductName1(), products.get(1).getName()),
                () -> Assertions.assertEquals(dataProduct.getProductTypeVeg(), products.get(1).getType()),
                () -> Assertions.assertFalse(products.get(1).isExotic()),
                () -> Assertions.assertEquals(dataProduct.getProductName2(), products.get(2).getName()),
                () -> Assertions.assertEquals(dataProduct.getProductTypeVeg(), products.get(2).getType()),
                () -> Assertions.assertFalse(products.get(2).isExotic()),
                () -> Assertions.assertEquals(dataProduct.getProductName3(), products.get(3).getName()),
                () -> Assertions.assertEquals(dataProduct.getProductTypeFruit(), products.get(3).getType()),
                () -> Assertions.assertFalse(products.get(3).isExotic())
        );
    }
}
