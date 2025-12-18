package co.com.franquicia.restconsumer.handler;

import co.com.franquicia.restconsumer.dto.request.BranchRequest;
import co.com.franquicia.restconsumer.dto.response.ApiResponse;
import co.com.franquicia.restconsumer.dto.response.ErrorResponse;
import co.com.franquicia.usecase.branch.CreateBranchUseCase;
import co.com.franquicia.usecase.branch.GetBranchesByFranchiseUseCase;
import co.com.franquicia.usecase.branch.UpdateBranchNameUseCase;
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
public class BranchHandler {

    private final CreateBranchUseCase createUseCase;
    private final UpdateBranchNameUseCase updateUseCase;
    private final GetBranchesByFranchiseUseCase getByFranchiseUseCase;

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(BranchRequest.class)
                .filter(req -> req.getName() != null && !req.getName().isBlank())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El campo nombre_sucursal es obligatorio y no puede estar vacío ni ser nulo")))
                .filter(req -> req.getFranchiseId() != null && !req.getFranchiseId().toString().isBlank())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("el campo franquicia_id es obligatorio y no puede estar vacío ni ser nulo")))
                .flatMap(req -> createUseCase.execute(req.getFranchiseId(), req.getName()))
                .map(branch -> ApiResponse.builder()
                        .status(200)
                        .message("La Sucursal se creó exitosamente.")
                        .data(branch)
                        .build()
                )
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
        return request.bodyToMono(BranchRequest.class)
                .filter(req -> req.getName() != null && !req.getName().isBlank())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El campo nombre_sucursal es obligatorio y no puede estar vacío ni ser nulo")))
                .flatMap(req -> updateUseCase.execute(id, req.getName()))
                .map(branch -> ApiResponse.builder()
                        .status(200)
                        .message("La Sucursal se Actualizo exitosamente.")
                        .data(branch)
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

    public Mono<ServerResponse> getByFranchise(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));
        return getByFranchiseUseCase.execute(franchiseId)
                .collectList()
                .map(branches -> ApiResponse.builder()
                        .status(200)
                        .message("Sucursales obtenidas exitosamente.")
                        .data(branches)
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
