package co.com.franquicia.jpa.adapter;

import co.com.franquicia.jpa.entity.FranchiseEntity;
import co.com.franquicia.jpa.mapper.FranchiseEntityMapper;
import co.com.franquicia.jpa.repository.FranchiseJpaRepository;
import co.com.franquicia.model.franchise.Franchise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FranchiseRepositoryAdapterTest {

    @Mock
    private FranchiseJpaRepository jpaRepository;

    @Mock
    private FranchiseEntityMapper mapper;

    @InjectMocks
    private FranchiseRepositoryAdapter repositoryAdapter;

    private Franchise inputFranchise;
    private FranchiseEntity franchiseEntity;
    private Franchise expectedFranchise;

    @BeforeEach
    void setUp() {
        inputFranchise = Franchise.builder()
                .name("Franquicia Colombia")
                .build();

        franchiseEntity = FranchiseEntity.builder()
                .id(1L)
                .name("Franquicia Colombia")
                .build();

        expectedFranchise = Franchise.builder()
                .id(1L)
                .name("Franquicia Colombia")
                .build();
    }

    @Test
    void givenValidFranchise_whenSave_thenFranchiseSavedSuccessfully() {
        // Arrange
        when(mapper.toEntity(any(Franchise.class))).thenReturn(franchiseEntity);
        when(jpaRepository.save(any(FranchiseEntity.class))).thenReturn(franchiseEntity);
        when(mapper.toModel(any(FranchiseEntity.class))).thenReturn(expectedFranchise);

        // Act
        Mono<Franchise> actualResult = repositoryAdapter.save(inputFranchise);

        // Assert
        StepVerifier.create(actualResult)
                .expectNext(expectedFranchise)
                .verifyComplete();

        verify(mapper).toEntity(any(Franchise.class));
        verify(jpaRepository).save(any(FranchiseEntity.class));
        verify(mapper).toModel(any(FranchiseEntity.class));
    }

    @Test
    void givenValidName_whenFindByName_thenReturnsFranchise() {
        // Arrange
        String searchName = "Franquicia Colombia";

        when(jpaRepository.findByName(eq(searchName))).thenReturn(Optional.of(franchiseEntity));
        when(mapper.toModel(any(FranchiseEntity.class))).thenReturn(expectedFranchise);

        // Act
        Mono<Franchise> actualResult = repositoryAdapter.findByName(searchName);

        // Assert
        StepVerifier.create(actualResult)
                .expectNext(expectedFranchise)
                .verifyComplete();

        verify(jpaRepository).findByName(eq(searchName));
        verify(mapper).toModel(any(FranchiseEntity.class));
    }

    @Test
    void givenNonExistentName_whenFindByName_thenReturnsEmptyMono() {
        // Arrange
        String searchName = "Non Existent";

        when(jpaRepository.findByName(eq(searchName))).thenReturn(Optional.empty());

        // Act
        Mono<Franchise> actualResult = repositoryAdapter.findByName(searchName);

        // Assert
        StepVerifier.create(actualResult)
                .verifyComplete();

        verify(jpaRepository).findByName(eq(searchName));
    }

    @Test
    void givenValidId_whenFindById_thenReturnsFranchise() {
        // Arrange
        Long searchId = 1L;

        when(jpaRepository.findById(eq(searchId))).thenReturn(Optional.of(franchiseEntity));
        when(mapper.toModel(any(FranchiseEntity.class))).thenReturn(expectedFranchise);

        // Act
        Mono<Franchise> actualResult = repositoryAdapter.findById(searchId);

        // Assert
        StepVerifier.create(actualResult)
                .expectNext(expectedFranchise)
                .verifyComplete();

        verify(jpaRepository).findById(eq(searchId));
        verify(mapper).toModel(any(FranchiseEntity.class));
    }

    @Test
    void givenDatabaseError_whenSave_thenPropagatesError() {
        // Arrange
        RuntimeException expectedException = new RuntimeException("Database connection failed");

        when(mapper.toEntity(any(Franchise.class))).thenReturn(franchiseEntity);
        when(jpaRepository.save(any(FranchiseEntity.class))).thenThrow(expectedException);

        // Act
        Mono<Franchise> actualResult = repositoryAdapter.save(inputFranchise);

        // Assert
        StepVerifier.create(actualResult)
                .expectError(RuntimeException.class)
                .verify();

        verify(mapper).toEntity(any(Franchise.class));
        verify(jpaRepository).save(any(FranchiseEntity.class));
    }
}
