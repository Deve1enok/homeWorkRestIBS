package tests;

import data.DataProduct;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import models.ProductListModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.ProductSpecification.*;

@DisplayName("QualitApi")
@Epic("Список товаров")
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

        step("Проверяем тело-ответа", () -> {
            assertThat(dataProduct.getProductExoticFruit()).as("Не совпадает имя первого товара")
                    .isEqualTo(products.get(4).getName());
            assertThat(dataProduct.getProductTypeFruit()).as("Не совпадает тип товара")
                    .isEqualTo(products.get(4).getType());
            assertThat(products.get(4).isExotic()).as("Не совпадает чек-бокс экзотический")
                    .isTrue();
        });
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

        step("Проверяем тело-ответа", () -> {
            assertThat(dataProduct.getProductExoticFruit()).as("Не совпадает имя первого товара")
                    .isEqualTo(products.get(4).getName());
            assertThat(dataProduct.getProductTypeFruit()).as("Не совпадает тип товара")
                    .isEqualTo(products.get(4).getType());
            assertThat(products.get(4).isExotic()).as("Не совпадает чек-бокс экзотический")
                    .isTrue();
        });

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

        step("Проверяем тело-ответа", () -> {
            assertThat(dataProduct.getProductName()).as("Не совпадает имя первого товара")
                    .isEqualTo(productsAfterReset.get(0).getName());
            assertThat(dataProduct.getProductName1()).as("Не совпадает имя второго товара")
                    .isEqualTo(productsAfterReset.get(1).getName());
            assertThat(dataProduct.getProductName2()).as("Не совпадает имя третьего товара")
                    .isEqualTo(productsAfterReset.get(2).getName());
            assertThat(dataProduct.getProductName3()).as("Не совпадает имя четвертого товара")
                    .isEqualTo(productsAfterReset.get(3).getName());
            assertThat(dataProduct.getProductTypeFruit()).as("Не совпадает тип товара")
                    .isEqualTo(productsAfterReset.get(0).getType())
                    .isEqualTo(productsAfterReset.get(3).getType());
            assertThat(dataProduct.getProductTypeVeg()).as("Не совпадает тип товара")
                    .isEqualTo(productsAfterReset.get(1).getType())
                    .isEqualTo(productsAfterReset.get(2).getType());
            assertThat(productsAfterReset.get(0).isExotic()).as("Не совпадает чек-бокс экзотический").isTrue();
            assertThat(productsAfterReset.get(1).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(productsAfterReset.get(2).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(productsAfterReset.get(3).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
        });
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

        step("Проверяем тело-ответа", () -> {
            assertThat(dataProduct.getProductFruit()).as("Не совпадает имя первого товара")
                    .isEqualTo(products.get(4).getName());
            assertThat(dataProduct.getProductTypeFruit()).as("Не совпадает тип товара")
                    .isEqualTo(products.get(4).getType());
            assertThat(products.get(4).isExotic()).as("Не совпадает чек-бокс экзотический")
                    .isFalse();
        });

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

        step("Проверяем тело-ответа", () -> {
            assertThat(dataProduct.getProductName()).as("Не совпадает имя первого товара")
                    .isEqualTo(productsAfterReset.get(0).getName());
            assertThat(dataProduct.getProductName1()).as("Не совпадает имя второго товара")
                    .isEqualTo(productsAfterReset.get(1).getName());
            assertThat(dataProduct.getProductName2()).as("Не совпадает имя третьего товара")
                    .isEqualTo(productsAfterReset.get(2).getName());
            assertThat(dataProduct.getProductName3()).as("Не совпадает имя четвертого товара")
                    .isEqualTo(productsAfterReset.get(3).getName());
            assertThat(dataProduct.getProductTypeFruit()).as("Не совпадает тип товара")
                    .isEqualTo(productsAfterReset.get(0).getType())
                    .isEqualTo(productsAfterReset.get(3).getType());
            assertThat(dataProduct.getProductTypeVeg()).as("Не совпадает тип товара")
                    .isEqualTo(productsAfterReset.get(1).getType())
                    .isEqualTo(productsAfterReset.get(2).getType());
            assertThat(productsAfterReset.get(0).isExotic()).as("Не совпадает чек-бокс экзотический").isTrue();
            assertThat(productsAfterReset.get(1).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(productsAfterReset.get(2).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(productsAfterReset.get(3).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
        });
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

        step("Проверяем тело-ответа", () -> {
            assertThat(dataProduct.getEmptyField()).as("Не совпадает имя первого товара")
                    .isEqualTo(products.get(4).getName());
            assertThat(dataProduct.getProductTypeFruit()).as("Не совпадает тип товара")
                    .isEqualTo(products.get(4).getType());
            assertThat(products.get(4).isExotic()).as("Не совпадает чек-бокс экзотический")
                    .isTrue();
        });

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

        step("Проверяем тело-ответа", () -> {
            assertThat(dataProduct.getProductName()).as("Не совпадает имя первого товара")
                    .isEqualTo(productsAfterReset.get(0).getName());
            assertThat(dataProduct.getProductName1()).as("Не совпадает имя второго товара")
                    .isEqualTo(productsAfterReset.get(1).getName());
            assertThat(dataProduct.getProductName2()).as("Не совпадает имя третьего товара")
                    .isEqualTo(productsAfterReset.get(2).getName());
            assertThat(dataProduct.getProductName3()).as("Не совпадает имя четвертого товара")
                    .isEqualTo(productsAfterReset.get(3).getName());
            assertThat(dataProduct.getProductTypeFruit()).as("Не совпадает тип товара")
                    .isEqualTo(productsAfterReset.get(0).getType())
                    .isEqualTo(productsAfterReset.get(3).getType());
            assertThat(dataProduct.getProductTypeVeg()).as("Не совпадает тип товара")
                    .isEqualTo(productsAfterReset.get(1).getType())
                    .isEqualTo(productsAfterReset.get(2).getType());
            assertThat(productsAfterReset.get(0).isExotic()).as("Не совпадает чек-бокс экзотический").isTrue();
            assertThat(productsAfterReset.get(1).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(productsAfterReset.get(2).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(productsAfterReset.get(3).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
        });
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

        step("Проверяем тело-ответа", () -> {
            assertThat(dataProduct.getProductExoticVegetable()).as("Не совпадает имя первого товара")
                    .isEqualTo(products.get(4).getName());
            assertThat(dataProduct.getProductTypeVeg()).as("Не совпадает тип товара")
                    .isEqualTo(products.get(4).getType());
            assertThat(products.get(4).isExotic()).as("Не совпадает чек-бокс экзотический")
                    .isTrue();
        });

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

        step("Проверяем тело-ответа", () -> {
            assertThat(dataProduct.getProductName()).as("Не совпадает имя первого товара")
                    .isEqualTo(productsAfterReset.get(0).getName());
            assertThat(dataProduct.getProductName1()).as("Не совпадает имя второго товара")
                    .isEqualTo(productsAfterReset.get(1).getName());
            assertThat(dataProduct.getProductName2()).as("Не совпадает имя третьего товара")
                    .isEqualTo(productsAfterReset.get(2).getName());
            assertThat(dataProduct.getProductName3()).as("Не совпадает имя четвертого товара")
                    .isEqualTo(productsAfterReset.get(3).getName());
            assertThat(dataProduct.getProductTypeFruit()).as("Не совпадает тип товара")
                    .isEqualTo(productsAfterReset.get(0).getType())
                    .isEqualTo(productsAfterReset.get(3).getType());
            assertThat(dataProduct.getProductTypeVeg()).as("Не совпадает тип товара")
                    .isEqualTo(productsAfterReset.get(1).getType())
                    .isEqualTo(productsAfterReset.get(2).getType());
            assertThat(productsAfterReset.get(0).isExotic()).as("Не совпадает чек-бокс экзотический").isTrue();
            assertThat(productsAfterReset.get(1).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(productsAfterReset.get(2).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(productsAfterReset.get(3).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
        });
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

        step("Проверяем тело-ответа", () -> {
            assertThat(dataProduct.getProductVegetable()).as("Не совпадает имя первого товара")
                    .isEqualTo(products.get(4).getName());
            assertThat(dataProduct.getProductTypeVeg()).as("Не совпадает тип товара")
                    .isEqualTo(products.get(4).getType());
            assertThat(products.get(4).isExotic()).as("Не совпадает чек-бокс экзотический")
                    .isFalse();
        });

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

        step("Проверяем тело-ответа", () -> {
            assertThat(dataProduct.getProductName()).as("Не совпадает имя первого товара")
                    .isEqualTo(productsAfterReset.get(0).getName());
            assertThat(dataProduct.getProductName1()).as("Не совпадает имя второго товара")
                    .isEqualTo(productsAfterReset.get(1).getName());
            assertThat(dataProduct.getProductName2()).as("Не совпадает имя третьего товара")
                    .isEqualTo(productsAfterReset.get(2).getName());
            assertThat(dataProduct.getProductName3()).as("Не совпадает имя четвертого товара")
                    .isEqualTo(productsAfterReset.get(3).getName());
            assertThat(dataProduct.getProductTypeFruit()).as("Не совпадает тип товара")
                    .isEqualTo(productsAfterReset.get(0).getType())
                    .isEqualTo(productsAfterReset.get(3).getType());
            assertThat(dataProduct.getProductTypeVeg()).as("Не совпадает тип товара")
                    .isEqualTo(productsAfterReset.get(1).getType())
                    .isEqualTo(productsAfterReset.get(2).getType());
            assertThat(productsAfterReset.get(0).isExotic()).as("Не совпадает чек-бокс экзотический").isTrue();
            assertThat(productsAfterReset.get(1).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(productsAfterReset.get(2).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(productsAfterReset.get(3).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
        });
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
    @DisplayName("Проверка добавления фрукта с пустым полем 'Тип' с чек-боксом экзотический")
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
