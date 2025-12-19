package co.com.franquicia.usecase.product;

import co.com.franquicia.model.franchise.Franchise;
import co.com.franquicia.model.franchise.gateway.FranchiseGateway;
import co.com.franquicia.model.product.TopStockProduct;
import co.com.franquicia.model.product.gateway.ProductGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetTopStockByFranchiseUseCaseTest {

    @Mock
    private FranchiseGateway franchiseGateway;

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private GetTopStockByFranchiseUseCase getTopStockByFranchiseUseCase;

    private Long inputFranchiseId;
    private Franchise existingFranchise;
    private List<TopStockProduct> expectedTopStockProducts;

    @BeforeEach
    void setUp() {
        inputFranchiseId = 1L;

        existingFranchise = Franchise.builder()
                .id(1L)
                .name("Franquicia Colombia")
                .build();

        expectedTopStockProducts = List.of(
                TopStockProduct.builder()
                        .branchId(1L)
                        .branchName("Sucursal Bogotá Norte")
                        .productName("Laptop Dell XPS 15")
                        .stock(250)
                        .build(),
                TopStockProduct.builder()
                        .branchId(2L)
                        .branchName("Sucursal Centro")
                        .productName("iPhone 15 Pro")
                        .stock(300)
                        .build()
        );
    }

    @Test
    void givenValidFranchiseId_whenExecute_thenReturnsTopStockProducts() {
        // Arrange
        when(franchiseGateway.findById(eq(inputFranchiseId))).thenReturn(Mono.just(existingFranchise));
        when(productGateway.findTopStockByFranchise(eq(inputFranchiseId)))
                .thenReturn(Flux.fromIterable(expectedTopStockProducts));

        // Act
        Flux<TopStockProduct> actualResult = getTopStockByFranchiseUseCase.execute(inputFranchiseId);

        // Assert
        StepVerifier.create(actualResult)
                .expectNextSequence(expectedTopStockProducts)
                .verifyComplete();

        verify(franchiseGateway).findById(eq(inputFranchiseId));
        verify(productGateway).findTopStockByFranchise(eq(inputFranchiseId));
    }

    @Test
    void givenNonExistentFranchise_whenExecute_thenThrowsIllegalArgumentException() {
        // Arrange
        String expectedErrorMessage = "Franquicia no encontrada con el id: " + inputFranchiseId;

        when(franchiseGateway.findById(eq(inputFranchiseId))).thenReturn(Mono.empty());

        // Act
        Flux<TopStockProduct> actualResult = getTopStockByFranchiseUseCase.execute(inputFranchiseId);

        // Assert
        StepVerifier.create(actualResult)
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals(expectedErrorMessage))
                .verify();

        verify(franchiseGateway).findById(eq(inputFranchiseId));
    }

    @Test
    void givenFranchiseWithNoBranches_whenExecute_thenReturnsEmptyFlux() {
        // Arrange
        when(franchiseGateway.findById(eq(inputFranchiseId))).thenReturn(Mono.just(existingFranchise));
        when(productGateway.findTopStockByFranchise(eq(inputFranchiseId))).thenReturn(Flux.empty());

        // Act
        Flux<TopStockProduct> actualResult = getTopStockByFranchiseUseCase.execute(inputFranchiseId);

        // Assert
        StepVerifier.create(actualResult)
                .verifyComplete();

        verify(franchiseGateway).findById(eq(inputFranchiseId));
        verify(productGateway).findTopStockByFranchise(eq(inputFranchiseId));
    }

    @Test
    void givenGatewayFailure_whenExecute_thenPropagatesError() {
        // Arrange
        RuntimeException expectedException = new RuntimeException("Database connection failed");

        when(franchiseGateway.findById(eq(inputFranchiseId))).thenReturn(Mono.error(expectedException));

        // Act
        Flux<TopStockProduct> actualResult = getTopStockByFranchiseUseCase.execute(inputFranchiseId);

        // Assert
        StepVerifier.create(actualResult)
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseGateway).findById(eq(inputFranchiseId));
    }
}
