import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.example.clients.OrderClient;
import org.example.objects.OrderObject;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.example.clients.CourierClient.BASE_URI;

import java.util.List;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final List<String> color;
    private int trackId;
    OrderClient orderApi = new OrderClient();
    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "{index}: Заказ с цветом: {0}")
    public static Object[][] checkCreateOrderWithChoiceColor() {
        return new Object[][]{
                {List.of("GREY")},
                {List.of("BLACK")},
                {List.of("GREY", "BLACK")},
                {List.of()},
        };
    }
    @Test
    @DisplayName("Создание заказа с выбором цвета: эндпоинт /api/v1/orders")
    @Description("Проверка, что можно создать заказ на: серый цвет, черный цвет, выбор двух цветов, без выбора цвета")
    public void paramCreateOrderTest() {
        new TestClass();
        OrderObject order = new OrderObject("Виктор", "Иванов", "Ижорская, 10", "4", "6800188888", 4, "2023-10-11", "Доставить самокат после 3 часов дня", color);
        ValidatableResponse response = orderApi.requestGetListOrders(order);
        trackId = response.extract().path("track");
        response.assertThat().body("$", Matchers.allOf(Matchers.hasKey("track")))
                .statusCode(HttpStatus.SC_CREATED);

    }
    @After
    public void tearDown(){
        orderApi.deleteTrack(trackId);
    }
}
