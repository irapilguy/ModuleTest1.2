import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
    private ShoppingCart cart;

    ShoppingCartTest(){
        cart = new ShoppingCart();
    }

    @Test
    void ZeroPriceTest() {
        Exception exp = Assertions.assertThrows(IllegalArgumentException.class, () -> cart.addItem("Bag", 0.0f, 2, Item.Type.REGULAR));
        assertEquals("Illegal price", exp.getMessage());
    }

    @Test
    void EmptyTitleTest() {
        Exception exp = Assertions.assertThrows(IllegalArgumentException.class, () -> cart.addItem("", 1, 2, Item.Type.REGULAR));
        assertEquals("Illegal title", exp.getMessage());
    }

    @Test
    void BigTitleTest() {
        Exception exp = Assertions.assertThrows(IllegalArgumentException.class, () -> cart.addItem("Painting by numbers - christmas tree with toys", 100, 2, Item.Type.REGULAR));
        assertEquals("Illegal title", exp.getMessage());
    }

    @Test
    void ZeroQuantityTest() {
        Exception exp = Assertions.assertThrows(IllegalArgumentException.class, () -> cart.addItem("Christmas tree", 100, 0, Item.Type.REGULAR));
        assertEquals("Illegal quantity", exp.getMessage());
    }

    @Test
    void CeilingOfQuantityTest() {
        Exception exp = Assertions.assertThrows(IllegalArgumentException.class, () -> cart.addItem("Christmas tree", 100, 1001, Item.Type.REGULAR));
        assertEquals("Illegal quantity", exp.getMessage());
    }

    @Test
    void CeilingOfPriceTest() {
        Exception exp = Assertions.assertThrows(IllegalArgumentException.class, () -> cart.addItem("Christmas tree", 1000.0f, 1, Item.Type.REGULAR));
        assertEquals("Illegal price", exp.getMessage());
    }

    @Test
    void ItemTypeTest() {
        Exception exp = Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            for (int i = 0; i < 100; i++) {
                cart.addItem("Christmas tree", 100.0f, 1, Item.Type.REGULAR);
            }
        });
        assertEquals("No more space in cart", exp.getMessage());
    }

    @ParameterizedTest
    @CsvSource({"1", "2", "4"})
    public void RegularDiscountTest(int quantity){
       assertEquals(0, ShoppingCart.calculateDiscount(new Item("Some title", 10, quantity, Item.Type.REGULAR)));
    }

    @ParameterizedTest
    @CsvSource({"1, 0", "2, 50", "3, 50"})
    public void SecondDiscountTest(int quantity, int expectedResult){
        assertEquals(expectedResult, ShoppingCart.calculateDiscount(new Item("Some title", 10, quantity, Item.Type.SECOND)));
    }

    @ParameterizedTest
    @CsvSource({"1, 10", "10, 20", "11, 20", "40, 50"})
    public void DiscountDiscountTest(int quantity, int expectedResult){
        assertEquals(expectedResult, ShoppingCart.calculateDiscount(new Item("Some title", 10, quantity, Item.Type.DISCOUNT)));
    }

    @ParameterizedTest
    @CsvSource({"1, 80", "10, 80"})
    public void SaleDiscountTest(int quantity, int expectedResult){
        assertEquals(expectedResult, ShoppingCart.calculateDiscount(new Item("Some title", 10, quantity, Item.Type.SALE)));
    }

    @ParameterizedTest
    @CsvSource({"100, 10", "300, 30", "900, 80", "999, 80" })
    public  void FullDiscountTest(int quantity, int expectedResult){
        assertEquals(expectedResult, ShoppingCart.calculateDiscount(new Item("Some title", 10, quantity, Item.Type.REGULAR)));
    }

}