package co.com.franquicia.restconsumer.handler;

import co.com.franquicia.restconsumer.dto.request.ProductRequest;
import co.com.franquicia.restconsumer.dto.response.ApiResponse;
import co.com.franquicia.restconsumer.dto.response.ErrorResponse;
import co.com.franquicia.usecase.product.CreateProductUseCase;
import co.com.franquicia.usecase.product.DeleteProductUseCase;
import co.com.franquicia.usecase.product.GetTopStockByFranchiseUseCase;
import co.com.franquicia.usecase.product.UpdateProductNameUseCase;
import co.com.franquicia.usecase.product.UpdateProductStockUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final CreateProductUseCase createUseCase;
    private final UpdateProductStockUseCase updateStockUseCase;
    private final UpdateProductNameUseCase updateNameUseCase;
    private final DeleteProductUseCase deleteUseCase;
    private final GetTopStockByFranchiseUseCase getTopStockUseCase;

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(ProductRequest.class)
                .filter(req -> req.getName() != null && !req.getName().isBlank())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El campo nombre_producto es obligatorio y no puede estar vacío ni ser nulo")))
                .filter(req -> req.getBranchId() != null && !req.getBranchId().toString().isBlank())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El campo sucursal_id es obligatorio y no puede estar vacío ni ser nulo")))
                .filter(req -> req.getStock() != null && !req.getStock().toString().isBlank() && req.getStock() >= 0)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El campo stock_producto es obligatorio y no puede estar vacío ni ser nulo y mayor que 0")))
                .flatMap(req -> createUseCase.execute(
                        req.getBranchId(),
                        req.getName(),
                        req.getStock()
                ))
                .map(product -> ApiResponse.builder()
                        .status(200)
                        .message("El Producto se creó exitosamente.")
                        .data(product)
                        .build())
                .flatMap(response -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(ErrorResponse.builder()
                                        .status(HttpStatus.BAD_REQUEST.value())
                                        .error("Bad Request")
                                        .message(e.getMessage())
                                        .timestamp(LocalDateTime.now())
                                        .path(request.path())
                                        .build())
                );
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.bodyToMono(ProductRequest.class)
                .filter(req -> req.getStock() != null && !req.getStock().toString().isBlank() && req.getStock() >= 0)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El campo stock_producto es obligatorio y no puede estar vacío ni ser nulo y mayor que 0")))
                .flatMap(req -> updateStockUseCase.execute(id, req.getStock()))
                .map(product -> ApiResponse.builder()
                        .status(200)
                        .message("El Stock se Actualizo exitosamente.")
                        .data(product)
                        .build())
                .flatMap(response -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(ErrorResponse.builder()
                                        .status(HttpStatus.BAD_REQUEST.value())
                                        .error("Bad Request")
                                        .message(e.getMessage())
                                        .timestamp(LocalDateTime.now())
                                        .path(request.path())
                                        .build())
                );
    }

    public Mono<ServerResponse> updateName(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return request.bodyToMono(ProductRequest.class)
                .filter(req -> req.getName() != null && !req.getName().isBlank())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El campo nombre_producto es obligatorio y no puede estar vacío ni ser nulo")))
                .flatMap(req -> updateNameUseCase.execute(id, req.getName()))
                .map(product -> ApiResponse.builder()
                        .status(200)
                        .message("El Nombre del producto se Actualizo exitosamente.")
                        .data(product)
                        .build())
                .flatMap(response -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(ErrorResponse.builder()
                                        .status(HttpStatus.BAD_REQUEST.value())
                                        .error("Bad Request")
                                        .message(e.getMessage())
                                        .timestamp(LocalDateTime.now())
                                        .path(request.path())
                                        .build())
                );
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return deleteUseCase.execute(id)
                .then(ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(ApiResponse.builder()
                                .status(200)
                                .message("Producto eliminado exitosamente.")
                                .build()))
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(ErrorResponse.builder()
                                        .status(HttpStatus.BAD_REQUEST.value())
                                        .error("Bad Request")
                                        .message(e.getMessage())
                                        .timestamp(LocalDateTime.now())
                                        .path(request.path())
                                        .build())
                );
    }

    public Mono<ServerResponse> getTopStockByFranchise(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(getTopStockUseCase.execute(franchiseId),
                        co.com.franquicia.model.product.TopStockProduct.class)
                .onErrorResume(IllegalArgumentException.class, e ->
                        ServerResponse.badRequest()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(ErrorResponse.builder()
                                        .status(HttpStatus.BAD_REQUEST.value())
                                        .error("Bad Request")
                                        .message(e.getMessage())
                                        .timestamp(LocalDateTime.now())
                                        .path(request.path())
                                        .build())
                );
    }
}
