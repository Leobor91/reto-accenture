package co.com.franquicia.jpa.mapper;

import co.com.franquicia.jpa.entity.ProductEntity;
import co.com.franquicia.model.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductEntityMapper {

    Product toModel(ProductEntity productEntity);
    ProductEntity toEntity(Product product);
}
