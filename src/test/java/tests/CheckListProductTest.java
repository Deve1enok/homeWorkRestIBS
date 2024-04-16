package tests;

import data.DataProduct;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.response.Response;
import models.ProductListModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
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

        step("Проверяем тело-ответа", () -> {
            assertThat(dataProduct.getProductName()).as("Не совпадает имя первого товара")
                    .isEqualTo(products.get(0).getName());
            assertThat(dataProduct.getProductName1()).as("Не совпадает имя второго товара")
                    .isEqualTo(products.get(1).getName());
            assertThat(dataProduct.getProductName2()).as("Не совпадает имя третьего товара")
                    .isEqualTo(products.get(2).getName());
            assertThat(dataProduct.getProductName3()).as("Не совпадает имя четвертого товара")
                    .isEqualTo(products.get(3).getName());
            assertThat(dataProduct.getProductTypeFruit()).as("Не совпадает тип товара")
                    .isEqualTo(products.get(0).getType())
                    .isEqualTo(products.get(3).getType());
            assertThat(dataProduct.getProductTypeVeg()).as("Не совпадает тип товара")
                    .isEqualTo(products.get(1).getType())
                    .isEqualTo(products.get(2).getType());
            assertThat(products.get(0).isExotic()).as("Не совпадает чек-бокс экзотический").isTrue();
            assertThat(products.get(1).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(products.get(2).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(products.get(3).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
        });
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

        step("Проверяем тело-ответа", () -> {
            assertThat(dataProduct.getProductName()).as("Не совпадает имя первого товара")
                    .isEqualTo(products.get(0).getName());
            assertThat(dataProduct.getProductName1()).as("Не совпадает имя второго товара")
                    .isEqualTo(products.get(1).getName());
            assertThat(dataProduct.getProductName2()).as("Не совпадает имя третьего товара")
                    .isEqualTo(products.get(2).getName());
            assertThat(dataProduct.getProductName3()).as("Не совпадает имя четвертого товара")
                    .isEqualTo(products.get(3).getName());
            assertThat(dataProduct.getProductTypeFruit()).as("Не совпадает тип товара")
                    .isEqualTo(products.get(0).getType())
                    .isEqualTo(products.get(3).getType());
            assertThat(dataProduct.getProductTypeVeg()).as("Не совпадает тип товара")
                    .isEqualTo(products.get(1).getType())
                    .isEqualTo(products.get(2).getType());
            assertThat(products.get(0).isExotic()).as("Не совпадает чек-бокс экзотический").isTrue();
            assertThat(products.get(1).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(products.get(2).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
            assertThat(products.get(2).isExotic()).as("Не совпадает чек-бокс экзотический").isFalse();
        });
    }
}
