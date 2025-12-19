package co.com.franquicia.restconsumer.handler;

import co.com.franquicia.model.franchise.Franchise;
import co.com.franquicia.restconsumer.dto.request.FranchiseRequest;
import co.com.franquicia.usecase.franchise.CreateFranchiseUseCase;
import co.com.franquicia.usecase.franchise.GetAllFranchisesUseCase;
import co.com.franquicia.usecase.franchise.UpdateFranchiseNameUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FranchiseHandlerTest {

    @Mock
    private CreateFranchiseUseCase createUseCase;

    @Mock
    private UpdateFranchiseNameUseCase updateUseCase;

    @Mock
    private GetAllFranchisesUseCase getAllUseCase;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private FranchiseHandler franchiseHandler;

    private FranchiseRequest franchiseRequest;
    private Franchise expectedFranchise;
    private List<Franchise> expectedFranchiseList;

    @BeforeEach
    void setUp() {
        franchiseRequest = FranchiseRequest.builder()
                .name("Franquicia Colombia")
                .build();

        expectedFranchise = Franchise.builder()
                .id(1L)
                .name("Franquicia Colombia")
                .build();

        expectedFranchiseList = List.of(
                Franchise.builder().id(1L).name("Franquicia Colombia").build(),
                Franchise.builder().id(2L).name("Franquicia Ecuador").build()
        );
    }

    @Test
    void givenValidRequest_whenCreate_thenReturns200WithCreatedFranchise() {
        // Arrange
        when(serverRequest.bodyToMono(FranchiseRequest.class))
                .thenReturn(Mono.just(franchiseRequest));
        when(createUseCase.execute(eq("Franquicia Colombia")))
                .thenReturn(Mono.just(expectedFranchise));

        // Act
        Mono<ServerResponse> actualResult = franchiseHandler.create(serverRequest);

        // Assert
        StepVerifier.create(actualResult)
                .expectNextMatches(response ->
                        response.statusCode() == HttpStatus.OK)
                .verifyComplete();

        verify(serverRequest).bodyToMono(FranchiseRequest.class);
        verify(createUseCase).execute(eq("Franquicia Colombia"));
    }

    @Test
    void givenDuplicateName_whenCreate_thenReturns400WithErrorResponse() {
        // Arrange
        IllegalArgumentException expectedException =
                new IllegalArgumentException("Franchise with name 'Franquicia Colombia' already exists");

        when(serverRequest.bodyToMono(FranchiseRequest.class))
                .thenReturn(Mono.just(franchiseRequest));
        when(serverRequest.path()).thenReturn("/api/v1/franchises/create");
        when(createUseCase.execute(eq("Franquicia Colombia")))
                .thenReturn(Mono.error(expectedException));

        // Act
        Mono<ServerResponse> actualResult = franchiseHandler.create(serverRequest);

        // Assert
        StepVerifier.create(actualResult)
                .expectNextMatches(response ->
                        response.statusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();

        verify(serverRequest).bodyToMono(FranchiseRequest.class);
        verify(createUseCase).execute(eq("Franquicia Colombia"));
    }

    @Test
    void givenValidRequest_whenUpdateName_thenReturns200WithUpdatedFranchise() {
        // Arrange
        Long franchiseId = 1L;
        Franchise updatedFranchise = Franchise.builder()
                .id(1L)
                .name("Franquicia Colombia Premium")
                .build();

        when(serverRequest.pathVariable("id")).thenReturn("1");
        when(serverRequest.bodyToMono(FranchiseRequest.class))
                .thenReturn(Mono.just(franchiseRequest));
        when(updateUseCase.execute(eq(franchiseId), eq("Franquicia Colombia")))
                .thenReturn(Mono.just(updatedFranchise));

        // Act
        Mono<ServerResponse> actualResult = franchiseHandler.updateName(serverRequest);

        // Assert
        StepVerifier.create(actualResult)
                .expectNextMatches(response ->
                        response.statusCode() == HttpStatus.OK)
                .verifyComplete();

        verify(serverRequest).pathVariable("id");
        verify(serverRequest).bodyToMono(FranchiseRequest.class);
        verify(updateUseCase).execute(eq(franchiseId), eq("Franquicia Colombia"));
    }

    @Test
    void givenValidRequest_whenGetAll_thenReturns200WithFranchiseList() {
        // Arrange
        when(getAllUseCase.execute()).thenReturn(Flux.fromIterable(expectedFranchiseList));

        // Act
        Mono<ServerResponse> actualResult = franchiseHandler.getAll(serverRequest);

        // Assert
        StepVerifier.create(actualResult)
                .expectNextMatches(response ->
                        response.statusCode() == HttpStatus.OK)
                .verifyComplete();

        verify(getAllUseCase).execute();
    }

    @Test
    void givenEmptyDatabase_whenGetAll_thenReturns200WithEmptyList() {
        // Arrange
        when(getAllUseCase.execute()).thenReturn(Flux.empty());

        // Act
        Mono<ServerResponse> actualResult = franchiseHandler.getAll(serverRequest);

        // Assert
        StepVerifier.create(actualResult)
                .expectNextMatches(response ->
                        response.statusCode() == HttpStatus.OK)
                .verifyComplete();

        verify(getAllUseCase).execute();
    }

}
