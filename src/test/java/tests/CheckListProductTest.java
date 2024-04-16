package tests;

import data.DataProduct;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.response.Response;
import models.ProductListModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.ProductSpecification.requestSpecification;
import static specs.ProductSpecification.responseSpecification200;

@DisplayName("QualitApi")
@Epic("Список товаров")
public class CheckListProductTest {
    DataProduct dataProduct = new DataProduct();
    private String cookies;

    @Test
    @Tag("positive_test")
    @Owner("Fazlyakhemtov D.A.")
    @Feature("Получение списка товара")
    @DisplayName("Проверка дефолтного списка продуктов")
    public void checkDefaultListProducts() {

        List<ProductListModel> products = step("Отправляем Get запрос на получение списка товаров", () ->
                given(requestSpecification)

                        .when()
                        .get("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().body().jsonPath().getList("", ProductListModel.class));

        step("Проверяем тело-ответа", () ->
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
                        () -> Assertions.assertFalse(products.get(3).isExotic()))
        );
    }

    @Test
    @Tag("positive_test")
    @Owner("Fazlyakhemtov D.A.")
    @Feature("Получение списка товара")
    @DisplayName("Проверка дефолтного списка продуктов после сброса данных")
    public void checkDefaultListProductAfterReset() {

        Response response = step("Отправляем Post запрос для сброса данных", () ->
                given(requestSpecification)

                        .when()
                        .post("/data/reset")

                        .then()

                        .spec(responseSpecification200)
                        .extract().response());

        cookies = response.getHeader("Set-Cookie");

        List<ProductListModel> products = step("Отправляем Get запрос на получение списка товаров", () ->
                given(requestSpecification)

                        .cookie(cookies)
                        .when()
                        .get("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().body().jsonPath().getList("", ProductListModel.class));

        step("Проверяем тело-ответа", () ->
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
                        () -> Assertions.assertFalse(products.get(3).isExotic()))
        );
    }
}
