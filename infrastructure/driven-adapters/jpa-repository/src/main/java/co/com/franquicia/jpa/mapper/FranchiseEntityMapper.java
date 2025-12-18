package co.com.franquicia.jpa.mapper;

import co.com.franquicia.jpa.entity.FranchiseEntity;
import co.com.franquicia.model.franchise.Franchise;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FranchiseEntityMapper {

    Franchise toModel(FranchiseEntity franchiseEntity);
    FranchiseEntity toEntity(Franchise franchise);
}
