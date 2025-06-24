package unirio.pm.external_service.client.paypal;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import unirio.pm.external_service.client.paypal.model.Amount;
import unirio.pm.external_service.client.paypal.model.Card;
import unirio.pm.external_service.client.paypal.model.Order;
import unirio.pm.external_service.client.paypal.model.PaymentSource;
import unirio.pm.external_service.client.paypal.model.PurchaseUnit;
import unirio.pm.external_service.client.paypal.model.Response;
import unirio.pm.external_service.dto.CartaoDTO;

@Component
public class PaypalClient {

    private final WebClient webClient;

    private final PaypalAuthClient authClient;

    public PaypalClient(WebClient.Builder builder,
                        PaypalAuthClient authClient,
                        @Value("${paypal.base-url}") String baseUrl) {
        this.webClient      = builder.baseUrl(baseUrl).build();
        this.authClient     = authClient;
    }

        public boolean autorizarTransacao(CartaoDTO cartao, BigDecimal valor) {

        String token = authClient.getAccessToken();
        if (token == null || token.isEmpty()) {
            return false; // falha ao obter token
        }

        Order order = new Order();
        order.setIntent("CAPTURE");
        PurchaseUnit pu = new PurchaseUnit();
        pu.setAmount(new Amount("BRL", valor));
        order.getPurchase_units().add(pu);

        Card card = new Card();
        card.setNumber(cartao.getNumero());
        card.setExpiry(cartao.getValidade());
        card.setName(cartao.getTitular());
        order.setPayment_source(new PaymentSource(card));

        Response response = webClient.post()
            .uri("/v2/checkout/orders")
            .header("Authorization", "Bearer " + token)
            .bodyValue(order)
            .retrieve()
            .bodyToMono(Response.class)
            .block();

        if (response == null || response.getId() == null) {
            return false;
        }

        Response capResp = webClient.post()
            .uri("/v2/checkout/orders/{orderId}/capture", response.getId())
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToMono(Response.class)
            .block();

        return capResp != null && "COMPLETED".equals(capResp.getStatus());
    }
}
