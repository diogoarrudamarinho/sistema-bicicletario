package dev.unirio.externalservice.client.paypal.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaypalModelTest {

    private Card card;

    @BeforeEach
    @SuppressWarnings("unused")
    void setup(){
        card = new Card(
            "4111111111111111",
            "2030-12", 
            "Teste", 
            "123"
        );
    }

    @Test
    void testAmount() {
        Amount amount = new Amount("BRL", "100.00");

        assertEquals("BRL", amount.getCurrencyCode());
        assertEquals("100.00", amount.getValue());

        amount.setCurrencyCode("USD");
        amount.setValue("200.00");

        assertEquals("USD", amount.getCurrencyCode());
        assertEquals("200.00", amount.getValue());
    }

    @Test
    void testCard() {
   
        assertEquals("4111111111111111", card.getNumber());
        assertEquals("2030-12", card.getExpiry());
        assertEquals("Teste", card.getName());
        assertEquals("123", card.getsecurityCode());

        card.setExpiry("2030-11");
        card.setName("Test");
        card.setSecurityCode("1234");
        card.setNumber("4111111111111112");

        assertEquals("4111111111111112", card.getNumber());
        assertEquals("2030-11", card.getExpiry());
        assertEquals("Test", card.getName());
        assertEquals("1234", card.getsecurityCode());
    }

    @Test
    void testPaymentSource() {
        PaymentSource source = new PaymentSource(card);
        assertEquals("4111111111111111", source.getCard().getNumber());
        source.setCard(new Card ("4111111111111112", "2030-11", "Test", "1234"));
        assertEquals("4111111111111112", source.getCard().getNumber());
    }

    @Test
    void testPurchaseUnit() {
        Amount amount = new Amount("EUR", "50.00");
        PurchaseUnit unit = new PurchaseUnit();
        unit.setAmount(amount);

        assertEquals("EUR", unit.getAmount().getCurrencyCode());
        assertEquals("50.00", unit.getAmount().getValue());
    }

    @Test
    void testOrder() {

        PurchaseUnit unit = new PurchaseUnit();
        unit.setAmount(new Amount("USD", "75.00"));

        PaymentSource source = new PaymentSource(card);
        Order order = new Order("CAPTURE", source);
        order.setPurchaseUnits(List.of(unit));

        assertEquals("CAPTURE", order.getIntent());
        assertEquals(1, order.getPurchaseUnits().size());
        assertEquals("75.00", order.getPurchaseUnits().get(0).getAmount().getValue());
        assertEquals("Teste", order.getPaymentSource().getCard().getName());

        order.setIntent("INTENT");
        order.setPaymentSource(new PaymentSource(card));
        assertEquals("INTENT", order.getIntent());
        assertEquals("Teste", order.getPaymentSource().getCard().getName());
        assertEquals(1, order.getPurchaseUnits().size());
    }
}
