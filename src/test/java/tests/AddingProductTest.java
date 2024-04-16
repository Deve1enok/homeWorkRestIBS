package tests;

import data.DataProduct;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import models.ProductListModel;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.ProductSpecification.*;

@DisplayName("QualitApi")
public class AddingProductTest {

    DataProduct dataProduct = new DataProduct();
    ProductListModel productListModel = new ProductListModel();
    private String cookies;

    @Test
    @Tag("positive_test")
    @Owner("Fazlyakhemtov D.A.")
    @Feature("Добавление товара")
    @DisplayName("Проверка добавления/сброса фрукта с типом фрукт и чек-боксом экзотический")
    public void addingNewProductAndCheckThemForAdding() {
        RestAssured.defaultParser = Parser.JSON;

        productListModel.setName(dataProduct.getProductExoticFruit());
        productListModel.setType(dataProduct.getProductTypeFruit());
        productListModel.setExotic(true);

        Response response = step("Отправляем Post запрос на добавление товара", () ->
                given(requestSpecification)

                        .body(productListModel)

                        .when()
                        .post("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().response());

        cookies = response.getHeader("Set-Cookie");

        List<ProductListModel> products = step("Отправляем Get запрос на получение добавленного товара", () ->
                given(requestSpecification)

                        .cookie(cookies)
                        .when()
                        .get("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().body().jsonPath().getList("", ProductListModel.class));

        step("Проверяем тело-ответа", () ->
                Assertions.assertAll(
                        () -> Assertions.assertEquals(dataProduct.getProductExoticFruit(), products.get(4).getName()),
                        () -> Assertions.assertEquals(dataProduct.getProductTypeFruit(), products.get(4).getType()),
                        () -> Assertions.assertTrue(products.get(4).isExotic())
                ));
    }

    @Test
    @Tag("positive_test")
    @Owner("Fazlyakhemtov D.A.")
    @Feature("Добавление/сброс товара")
    @DisplayName("Проверка добавления/сброса фрукта с типом фрукт и чек-боксом экзотический")
    public void addingExoticFruitAndCheckThemForAddingWithResetData() {
        RestAssured.defaultParser = Parser.JSON;

        productListModel.setName(dataProduct.getProductExoticFruit());
        productListModel.setType(dataProduct.getProductTypeFruit());
        productListModel.setExotic(true);

        Response response = step("Отправляем Post запрос на добавление товара", () ->
                given(requestSpecification)

                        .body(productListModel)

                        .when()
                        .post("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().response());

        cookies = response.getHeader("Set-Cookie");

        List<ProductListModel> products = step("Отправляем Get запрос на получение добавленного товара", () ->
                given(requestSpecification)

                        .cookie(cookies)
                        .when()
                        .get("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().body().jsonPath().getList("", ProductListModel.class));

        step("Проверяем тело-ответа", () ->
                Assertions.assertAll(
                        () -> Assertions.assertEquals(dataProduct.getProductExoticFruit(), products.get(4).getName()),
                        () -> Assertions.assertEquals(dataProduct.getProductTypeFruit(), products.get(4).getType()),
                        () -> Assertions.assertTrue(products.get(4).isExotic())
                ));

        step("Отправляем Post запрос для сброса данных", () ->
                given(requestSpecification)

                        .cookie(cookies)
                        .when()
                        .post("/data/reset")

                        .then()

                        .spec(responseSpecification200)
                        .extract().response());

        List<ProductListModel> productsAfterReset = step(
                "Отправляем Get запрос на получение списка после сброса данных", () ->
                        given(requestSpecification)

                                .cookie(cookies)
                                .when()
                                .get("/food")

                                .then()
                                .spec(responseSpecification200)
                                .extract().body().jsonPath().getList("", ProductListModel.class));

        step("Проверяем тело-ответа", () ->
                Assertions.assertAll(
                        () -> Assertions.
                                assertEquals(dataProduct.getProductName(), productsAfterReset.get(0).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeFruit(), productsAfterReset.get(0).getType()),
                        () -> Assertions.
                                assertTrue(productsAfterReset.get(0).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName1(), productsAfterReset.get(1).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeVeg(), productsAfterReset.get(1).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(1).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName2(), productsAfterReset.get(2).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeVeg(), productsAfterReset.get(2).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(2).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName3(), productsAfterReset.get(3).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeFruit(), productsAfterReset.get(3).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(3).isExotic())
                ));
    }

    @Test
    @Tag("positive_test")
    @Owner("Fazlyakhemtov D.A.")
    @Feature("Добавление/сброс товара")
    @DisplayName("Проверка добавления/сброса фрукта с типом фрукт без чек-бокса экзотический")
    public void addingFruitWithoutExoticAndCheckThemForAddingWithResetData() {
        RestAssured.defaultParser = Parser.JSON;

        productListModel.setName(dataProduct.getProductFruit());
        productListModel.setType(dataProduct.getProductTypeFruit());

        Response response = step("Отправляем Post запрос на добавление товара", () ->
                given(requestSpecification)

                        .body(productListModel)

                        .when()
                        .post("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().response());

        cookies = response.getHeader("Set-Cookie");

        List<ProductListModel> products = step("Отправляем Get запрос на получение добавленного товара", () ->
                given(requestSpecification)

                        .cookie(cookies)
                        .when()
                        .get("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().body().jsonPath().getList("", ProductListModel.class));

        step("Проверяем тело-ответа", () ->
                Assertions.assertAll(
                        () -> Assertions.assertEquals(dataProduct.getProductFruit(), products.get(4).getName()),
                        () -> Assertions.assertEquals(dataProduct.getProductTypeFruit(), products.get(4).getType()),
                        () -> Assertions.assertFalse(products.get(4).isExotic())
                ));

        step("Отправляем Post запрос для сброса данных", () ->
                given(requestSpecification)

                        .cookie(cookies)
                        .when()
                        .post("/data/reset")

                        .then()

                        .spec(responseSpecification200)
                        .extract().response());

        List<ProductListModel> productsAfterReset = step(
                "Отправляем Get запрос на получение списка после сброса данных", () ->
                        given(requestSpecification)

                                .cookie(cookies)
                                .when()
                                .get("/food")

                                .then()
                                .spec(responseSpecification200)
                                .extract().body().jsonPath().getList("", ProductListModel.class));

        step("Проверяем тело-ответа", () ->
                Assertions.assertAll(
                        () -> Assertions.
                                assertEquals(dataProduct.getProductName(), productsAfterReset.get(0).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeFruit(), productsAfterReset.get(0).getType()),
                        () -> Assertions.
                                assertTrue(productsAfterReset.get(0).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName1(), productsAfterReset.get(1).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeVeg(), productsAfterReset.get(1).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(1).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName2(), productsAfterReset.get(2).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeVeg(), productsAfterReset.get(2).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(2).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName3(), productsAfterReset.get(3).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeFruit(), productsAfterReset.get(3).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(3).isExotic())
                ));
    }

    @Test
    @Tag("negative_test")
    @Owner("Fazlyakhemtov D.A.")
    @Feature("Добавление/сброс товара")
    @DisplayName("Проверка добавления/сброса товара с пустым полем 'Наименование' с типом фрукт и чек-боксом экзотический")
    public void addingEmptyNameProductAndCheckThemForAddingWithResetData() {
        RestAssured.defaultParser = Parser.JSON;

        productListModel.setName(dataProduct.getEmptyField());
        productListModel.setType(dataProduct.getProductTypeFruit());
        productListModel.setExotic(true);

        Response response = step("Отправляем Post запрос на добавление товара", () ->
                given(requestSpecification)

                        .body(productListModel)

                        .when()
                        .post("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().response());

        cookies = response.getHeader("Set-Cookie");

        List<ProductListModel> products = step("Отправляем Get запрос на получение добавленного товара", () ->
                given(requestSpecification)

                        .cookie(cookies)
                        .when()
                        .get("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().body().jsonPath().getList("", ProductListModel.class));

        step("Проверяем тело-ответа", () ->
                Assertions.assertAll(
                        () -> Assertions.assertEquals(dataProduct.getEmptyField(), products.get(4).getName()),
                        () -> Assertions.assertEquals(dataProduct.getProductTypeFruit(), products.get(4).getType()),
                        () -> Assertions.assertTrue(products.get(4).isExotic())
                ));

        step("Отправляем Post запрос для сброса данных", () ->
                given(requestSpecification)

                        .cookie(cookies)
                        .when()
                        .post("/data/reset")

                        .then()

                        .spec(responseSpecification200)
                        .extract().response());

        List<ProductListModel> productsAfterReset = step(
                "Отправляем Get запрос на получение списка после сброса данных", () ->
                        given(requestSpecification)

                                .cookie(cookies)
                                .when()
                                .get("/food")

                                .then()
                                .spec(responseSpecification200)
                                .extract().body().jsonPath().getList("", ProductListModel.class));

        step("Проверяем тело-ответа", () ->
                Assertions.assertAll(
                        () -> Assertions.
                                assertEquals(dataProduct.getProductName(), productsAfterReset.get(0).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeFruit(), productsAfterReset.get(0).getType()),
                        () -> Assertions.
                                assertTrue(productsAfterReset.get(0).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName1(), productsAfterReset.get(1).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeVeg(), productsAfterReset.get(1).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(1).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName2(), productsAfterReset.get(2).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeVeg(), productsAfterReset.get(2).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(2).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName3(), productsAfterReset.get(3).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeFruit(), productsAfterReset.get(3).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(3).isExotic())
                ));
    }

    @Test
    @Tag("positive_test")
    @Owner("Fazlyakhemtov D.A.")
    @Feature("Добавление/сброс товара")
    @DisplayName("Проверка добавления/сброса овоща с типом овощ и чек-боксом экзотический")
    public void addingVegetableAndCheckThemForAddingWithResetData() {
        RestAssured.defaultParser = Parser.JSON;

        productListModel.setName(dataProduct.getProductExoticVegetable());
        productListModel.setType(dataProduct.getProductTypeVeg());
        productListModel.setExotic(true);

        Response response = step("Отправляем Post запрос на добавление товара", () ->
                given(requestSpecification)

                        .body(productListModel)

                        .when()
                        .post("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().response());

        cookies = response.getHeader("Set-Cookie");

        List<ProductListModel> products = step("Отправляем Get запрос на получение добавленного товара", () ->
                given(requestSpecification)

                        .cookie(cookies)
                        .when()
                        .get("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().body().jsonPath().getList("", ProductListModel.class));

        step("Проверяем тело-ответа", () ->
                Assertions.assertAll(
                        () -> Assertions.assertEquals(dataProduct.getProductExoticVegetable(), products.get(4).getName()),
                        () -> Assertions.assertEquals(dataProduct.getProductTypeVeg(), products.get(4).getType()),
                        () -> Assertions.assertTrue(products.get(4).isExotic())
                ));

        step("Отправляем Post запрос для сброса данных", () ->
                given(requestSpecification)

                        .cookie(cookies)
                        .when()
                        .post("/data/reset")

                        .then()

                        .spec(responseSpecification200)
                        .extract().response());

        List<ProductListModel> productsAfterReset = step(
                "Отправляем Get запрос на получение списка после сброса данных", () ->
                        given(requestSpecification)

                                .cookie(cookies)
                                .when()
                                .get("/food")

                                .then()
                                .spec(responseSpecification200)
                                .extract().body().jsonPath().getList("", ProductListModel.class));

        step("Проверяем тело-ответа", () ->
                Assertions.assertAll(
                        () -> Assertions.
                                assertEquals(dataProduct.getProductName(), productsAfterReset.get(0).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeFruit(), productsAfterReset.get(0).getType()),
                        () -> Assertions.
                                assertTrue(productsAfterReset.get(0).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName1(), productsAfterReset.get(1).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeVeg(), productsAfterReset.get(1).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(1).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName2(), productsAfterReset.get(2).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeVeg(), productsAfterReset.get(2).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(2).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName3(), productsAfterReset.get(3).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeFruit(), productsAfterReset.get(3).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(3).isExotic())
                ));
    }

    @Test
    @Tag("positive_test")
    @Owner("Fazlyakhemtov D.A.")
    @Feature("Добавление/сброс товара")
    @DisplayName("Проверка добавления/сброса овоща с типом овощ без чек-боксом экзотический")
    public void addingVegetableWithoutExoticAndCheckThemForAddingWithResetData() {
        RestAssured.defaultParser = Parser.JSON;

        productListModel.setName(dataProduct.getProductVegetable());
        productListModel.setType(dataProduct.getProductTypeVeg());

        Response response = step("Отправляем Post запрос на добавление товара", () ->
                given(requestSpecification)

                        .body(productListModel)

                        .when()
                        .post("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().response());

        cookies = response.getHeader("Set-Cookie");

        List<ProductListModel> products = step("Отправляем Get запрос на получение добавленного товара", () ->
                given(requestSpecification)

                        .cookie(cookies)
                        .when()
                        .get("/food")

                        .then()
                        .spec(responseSpecification200)
                        .extract().body().jsonPath().getList("", ProductListModel.class));

        step("Проверяем тело-ответа", () ->
                Assertions.assertAll(
                        () -> Assertions.assertEquals(dataProduct.getProductVegetable(), products.get(4).getName()),
                        () -> Assertions.assertEquals(dataProduct.getProductTypeVeg(), products.get(4).getType()),
                        () -> Assertions.assertFalse(products.get(4).isExotic())
                ));

        step("Отправляем Post запрос для сброса данных", () ->
                given(requestSpecification)

                        .cookie(cookies)
                        .when()
                        .post("/data/reset")

                        .then()

                        .spec(responseSpecification200)
                        .extract().response());

        List<ProductListModel> productsAfterReset = step(
                "Отправляем Get запрос на получение списка после сброса данных", () ->
                        given(requestSpecification)

                                .cookie(cookies)
                                .when()
                                .get("/food")

                                .then()
                                .spec(responseSpecification200)
                                .extract().body().jsonPath().getList("", ProductListModel.class));

        step("Проверяем тело-ответа", () ->
                Assertions.assertAll(
                        () -> Assertions.
                                assertEquals(dataProduct.getProductName(), productsAfterReset.get(0).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeFruit(), productsAfterReset.get(0).getType()),
                        () -> Assertions.
                                assertTrue(productsAfterReset.get(0).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName1(), productsAfterReset.get(1).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeVeg(), productsAfterReset.get(1).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(1).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName2(), productsAfterReset.get(2).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeVeg(), productsAfterReset.get(2).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(2).isExotic()),

                        () -> Assertions.
                                assertEquals(dataProduct.getProductName3(), productsAfterReset.get(3).getName()),
                        () -> Assertions.
                                assertEquals(dataProduct.getProductTypeFruit(), productsAfterReset.get(3).getType()),
                        () -> Assertions.
                                assertFalse(productsAfterReset.get(3).isExotic())
                ));
    }

    @Test
    @Disabled("Приходит код 200, ожидается код 400")
    @Tag("negative_test")
    @Owner("Fazlyakhemtov D.A.")
    @Feature("Добавление товара")
    @DisplayName("Проверка добавления фрукта без поля 'Тип' с чек-боксом экзотический")
    public void addingNewProductWithoutType400() {
        RestAssured.defaultParser = Parser.JSON;

        productListModel.setName(dataProduct.getProductExoticFruit());
        productListModel.setExotic(true);

        step("Отправляем Post запрос на добавление товара", () ->
                given(requestSpecification)

                        .body(productListModel)

                        .when()
                        .post("/food")

                        .then()
                        .spec(responseSpecification400)
                        .extract().response());
    }

    @Test
    @Tag("negative_test")
    @Owner("Fazlyakhemtov D.A.")
    @Feature("Добавление товара")
    @DisplayName("Проверка добавления фрукта без поля 'Тип' с чек-боксом экзотический")
    public void addingNewProductWithEmptyType400() {
        RestAssured.defaultParser = Parser.JSON;

        productListModel.setName(dataProduct.getProductExoticFruit());
        productListModel.setType(dataProduct.getEmptyField());
        productListModel.setExotic(true);

        step("Отправляем Post запрос на добавление товара", () ->
                given(requestSpecification)

                        .body(productListModel)

                        .when()
                        .post("/food")

                        .then()
                        .spec(responseSpecification400)
                        .extract().response());
    }
}
