package co.com.franquicia.jpa.mapper;

import co.com.franquicia.jpa.entity.BranchEntity;
import co.com.franquicia.model.branch.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BranchEntityMapper {

    Branch toModel(BranchEntity branchEntity);
    BranchEntity toEntity(Branch branch);
}
