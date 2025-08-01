package unirio.pm.external_service.client.paypal;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonProperty;

import reactor.core.publisher.Mono;
import unirio.pm.external_service.client.paypal.model.Amount;
import unirio.pm.external_service.client.paypal.model.Card;
import unirio.pm.external_service.client.paypal.model.Order;
import unirio.pm.external_service.client.paypal.model.PaymentSource;
import unirio.pm.external_service.client.paypal.model.PurchaseUnit;
import unirio.pm.external_service.dto.CartaoDTO;
import unirio.pm.external_service.exception.cobranca.PaypalApiException;
import unirio.pm.external_service.exception.cobranca.PaypalApiException.PaypalErrorDetail;

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

    public void autorizarTransacao(CartaoDTO cartao, BigDecimal valor) {

        String token = authClient.getAccessToken();
        if (token == null || token.isEmpty()) {
            throw new PaypalApiException("TOKEN_ERROR");
        }

        Order order = createOrder(cartao, valor);
        String requestId = UUID.randomUUID().toString(); 

        try {
            Response resp = webClient.post()
                .uri("/v2/checkout/orders")
                .header("Authorization", "Bearer " + token)
                .header("Paypal-Request-Id", requestId)
                .bodyValue(order)
                .retrieve()
                .onStatus(  HttpStatusCode::isError, 
                            clientResponse ->
                            clientResponse.bodyToMono(PaypalErrorBody.class)
                            .flatMap(err -> Mono.error(new PaypalApiException(
                                clientResponse.statusCode().value(),
                                err.getName(),
                                err.getDetails()
                            )))
                )
                .bodyToMono(Response.class)
                .block();

            if (resp == null || resp.getId() == null) {
                throw new PaypalApiException(500, null, null);
            }

            webClient.post()
                .uri("/v2/checkout/orders/{orderId}/capture", resp.getId())
                .header("Authorization", "Bearer " + token)
                .header("Paypal-Request-Id", requestId)
                .header("Content-Type", "application/json") //Sem isso aqui tava dando Unsupported Media Type
                .retrieve()
                .onStatus(  HttpStatusCode::isError, 
                            clientResponse -> clientResponse
                                .bodyToMono(PaypalErrorBody.class)
                                .flatMap(err -> Mono.error(new PaypalApiException(
                                    clientResponse.statusCode().value(),
                                    err.getName(),
                                    err.getDetails()
                        )))
                )
                .bodyToMono(Response.class)
                .block();

        } catch (PaypalApiException e) {
            throw e;
        } catch (Exception e) {
            throw new PaypalApiException("Erro interno");
        }
    }

    private Order createOrder(CartaoDTO cartao, BigDecimal valor) {
       
        PurchaseUnit pu = new PurchaseUnit();
        pu.setAmount(new Amount("BRL", valor.toString()));

        Card card = new Card(
            cartao.getNumero(), 
            cartao.getValidade(),
            cartao.getTitular(),
            cartao.getCvv());

        Order order = new Order("CAPTURE", new PaymentSource(card));
        order.getPurchaseUnits().add(pu);
        order.setPaymentSource(new PaymentSource(card));
        return order;
    }

    static class Response {
        @JsonProperty("id")
        protected String id;
        public String getId() { return id; }
    }

    static class PaypalErrorBody {
        private String name;
        private List<PaypalErrorDetail> details;

        public String getName() { return name; }

        public List<PaypalErrorDetail> getDetails() { return details; }
    }
}
