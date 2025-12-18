package co.com.franquicia.restconsumer.handler;


import co.com.franquicia.model.franchise.Franchise;
import co.com.franquicia.restconsumer.dto.response.ApiResponse;
import co.com.franquicia.restconsumer.dto.response.ErrorResponse;
import co.com.franquicia.restconsumer.dto.request.FranchiseRequest;
import co.com.franquicia.usecase.franchise.CreateFranchiseUseCase;
import co.com.franquicia.usecase.franchise.GetAllFranchisesUseCase;
import co.com.franquicia.usecase.franchise.UpdateFranchiseNameUseCase;
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
public class FranchiseHandler {

    private final CreateFranchiseUseCase createUseCase;
    private final UpdateFranchiseNameUseCase updateUseCase;
    private final GetAllFranchisesUseCase getAllUseCase;

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(FranchiseRequest.class)
                .filter(req -> req.getName() != null && !req.getName().isBlank())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El campo nombre_franquicia es obligatorio y no puede estar vacío ni ser nulo")))
                .flatMap(req -> createUseCase.execute(req.getName()))
                .map(franchise -> ApiResponse.builder()
                        .status(200)
                        .message("La franquicia se creó exitosamente.")
                        .data(franchise)
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
        return request.bodyToMono(FranchiseRequest.class)
                .filter(req -> req.getName() != null && !req.getName().isBlank())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El campo nombre_franquicia es obligatorio y no puede estar vacío ni ser nulo")))
                .flatMap(req -> updateUseCase.execute(id, req.getName()))
                .map(franchise -> ApiResponse.builder()
                        .status(200)
                        .message("La franquicia se Actualizo exitosamente.")
                        .data(franchise)
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

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return getAllUseCase.execute()
                .collectList()
                .map(franchises -> ApiResponse.builder()
                        .status(200)
                        .message("Franquicias obtenidas exitosamente.")
                        .data(franchises)
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

}
