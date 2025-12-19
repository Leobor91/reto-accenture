package co.com.franquicia.usecase.product;

import co.com.franquicia.model.product.Product;

import co.com.franquicia.model.product.gateway.ProductGateway;
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
public class UpdateProductStockUseCaseTest {

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private UpdateProductStockUseCase updateProductStockUseCase;

    private Long inputProductId;
    private Integer inputNewStock;
    private Product existingProduct;
    private Product expectedUpdatedProduct;

    @BeforeEach
    void setUp() {
        inputProductId = 1L;
        inputNewStock = 250;

        existingProduct = Product.builder()
                .id(1L)
                .branchId(1L)
                .name("Laptop Dell XPS 15")
                .stock(150)
                .build();

        expectedUpdatedProduct = Product.builder()
                .id(1L)
                .branchId(1L)
                .name("Laptop Dell XPS 15")
                .stock(250)
                .build();
    }

    @Test
    void givenNonExistentProduct_whenExecute_thenThrowsIllegalArgumentException() {
        // Arrange
        String expectedErrorMessage = "Producto no encontrado con id: " + inputProductId;

        when(productGateway.findById(eq(inputProductId))).thenReturn(Mono.empty());

        // Act
        Mono<Product> actualResult = updateProductStockUseCase.execute(inputProductId, inputNewStock);

        // Assert
        StepVerifier.create(actualResult)
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals(expectedErrorMessage))
                .verify();

        verify(productGateway).findById(eq(inputProductId));
    }

}
