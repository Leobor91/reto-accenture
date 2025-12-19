package co.com.franquicia.usecase.franchise;

import co.com.franquicia.model.franchise.Franchise;
import co.com.franquicia.model.franchise.gateway.FranchiseGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateFranchiseUseCaseTest {

    @Mock
    private FranchiseGateway franchiseGateway;

    @InjectMocks
    private CreateFranchiseUseCase createFranchiseUseCase;

    private String inputName;
    private Franchise expectedFranchise;

    @BeforeEach
    void setUp() {
        inputName = "Franquicia Colombia";
        expectedFranchise = Franchise.builder()
                .id(1L)
                .name("Franquicia Colombia")
                .build();
    }

    @Test
    void givenValidName_whenExecute_thenFranchiseCreatedSuccessfully() {
        // Arrange
        when(franchiseGateway.findByName(eq(inputName))).thenReturn(Mono.empty());
        when(franchiseGateway.save(any(Franchise.class))).thenReturn(Mono.just(expectedFranchise));

        // Act
        Mono<Franchise> actualResult = createFranchiseUseCase.execute(inputName);

        // Assert
        StepVerifier.create(actualResult)
                .expectNext(expectedFranchise)
                .verifyComplete();

        verify(franchiseGateway).findByName(eq(inputName));
        verify(franchiseGateway).save(any(Franchise.class));
    }

    @Test
    void givenDuplicateName_whenExecute_thenThrowsIllegalArgumentException() {
        // Arrange
        Franchise existingFranchise = Franchise.builder()
                .id(2L)
                .name("Franquicia Colombia")
                .build();
        String expectedErrorMessage = "La franquicia con nombre 'Franquicia Colombia' ya existe.";

        when(franchiseGateway.findByName(eq(inputName))).thenReturn(Mono.just(existingFranchise));

        // Act
        Mono<Franchise> actualResult = createFranchiseUseCase.execute(inputName);

        // Assert
        StepVerifier.create(actualResult)
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals(expectedErrorMessage))
                .verify();

        verify(franchiseGateway).findByName(eq(inputName));
    }

    @Test
    void givenGatewayFailure_whenExecute_thenPropagatesError() {
        // Arrange
        RuntimeException expectedException = new RuntimeException("Database connection failed");

        when(franchiseGateway.findByName(eq(inputName))).thenReturn(Mono.error(expectedException));

        // Act
        Mono<Franchise> actualResult = createFranchiseUseCase.execute(inputName);

        // Assert
        StepVerifier.create(actualResult)
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseGateway).findByName(eq(inputName));
    }
}
