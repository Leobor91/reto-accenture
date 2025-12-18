package co.com.franquicia.restconsumer.api;

import co.com.franquicia.restconsumer.handler.BranchHandler;
import co.com.franquicia.restconsumer.handler.FranchiseHandler;
import co.com.franquicia.restconsumer.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class RouterConfig {

    private static final String API_V1 = "/api/v1";

    @Bean
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler) {
        return RouterFunctions
                .route(POST(API_V1 + "/franchises/create").and(accept(APPLICATION_JSON)),
                        handler::create)
                .andRoute(PUT(API_V1 + "/franchises/{id}/name").and(accept(APPLICATION_JSON)),
                        handler::updateName)
                .andRoute(GET(API_V1 + "/franchises"),
                        handler::getAll);
    }

    @Bean
    public RouterFunction<ServerResponse> branchRoutes(BranchHandler handler) {
        return RouterFunctions
                .route(POST(API_V1 + "/branches/create").and(accept(APPLICATION_JSON)),
                        handler::create)
                .andRoute(PUT(API_V1 + "/branches/{id}/name").and(accept(APPLICATION_JSON)),
                        handler::updateName)
                .andRoute(GET(API_V1 + "/franchises/{franchiseId}/branches"),
                        handler::getByFranchise);
    }

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return RouterFunctions
                .route(POST(API_V1 + "/products/create").and(accept(APPLICATION_JSON)),
                        handler::create)
                .andRoute(PUT(API_V1 + "/products/{id}/stock").and(accept(APPLICATION_JSON)),
                        handler::updateStock)
                .andRoute(PUT(API_V1 + "/products/{id}/name").and(accept(APPLICATION_JSON)),
                        handler::updateName)
                .andRoute(DELETE(API_V1 + "/products/{id}"),
                        handler::delete)
                .andRoute(GET(API_V1 + "/franchises/{franchiseId}/top-stock-products"),
                        handler::getTopStockByFranchise);
    }

}
