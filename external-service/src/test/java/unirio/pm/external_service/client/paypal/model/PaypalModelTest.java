package unirio.pm.external_service.client.paypal.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PaypalModelTest {

    @Test
    void testAmount() {
        Amount amount = new Amount("BRL", "100.00");

        assertEquals("BRL", amount.getCurrency_code());
        assertEquals("100.00", amount.getValue());

        amount.setCurrency_code("USD");
        amount.setValue("200.00");

        assertEquals("USD", amount.getCurrency_code());
        assertEquals("200.00", amount.getValue());
    }

    @Test
    void testCard() {
        Card card = new Card();
        card.setNumber("4111111111111111");
        card.setExpiry("2030-12");
        card.setName("Teste");
        card.setSecurityCode("123");

        assertEquals("4111111111111111", card.getNumber());
        assertEquals("2030-12", card.getExpiry());
        assertEquals("Teste", card.getName());
        assertEquals("123", card.getsecurityCode());
    }

    @Test
    void testPaymentSource() {
        Card card = new Card();
        card.setNumber("4000000000000002");

        PaymentSource source = new PaymentSource(card);

        assertEquals("4000000000000002", source.getCard().getNumber());

        Card newCard = new Card();
        newCard.setNumber("1234567890123456");
        source.setCard(newCard);

        assertEquals("1234567890123456", source.getCard().getNumber());
    }

    @Test
    void testPurchaseUnit() {
        Amount amount = new Amount("EUR", "50.00");
        PurchaseUnit unit = new PurchaseUnit();
        unit.setAmount(amount);

        assertEquals("EUR", unit.getAmount().getCurrency_code());
        assertEquals("50.00", unit.getAmount().getValue());
    }

    @Test
    void testOrder() {
        Order order = new Order();
        order.setIntent("CAPTURE");

        PurchaseUnit unit = new PurchaseUnit();
        unit.setAmount(new Amount("USD", "75.00"));
        order.setPurchase_units(List.of(unit));

        Card card = new Card();
        card.setName("Test");
        PaymentSource source = new PaymentSource(card);
        order.setPayment_source(source);

        assertEquals("CAPTURE", order.getIntent());
        assertEquals(1, order.getPurchase_units().size());
        assertEquals("75.00", order.getPurchase_units().get(0).getAmount().getValue());
        assertEquals("Test", order.getPayment_source().getCard().getName());
    }
}
