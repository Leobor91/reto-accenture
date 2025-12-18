package co.com.franquicia.jpa.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "branch")
@Entity
public class BranchEntity {

    @Id
    private Long id;
    private Long franchiseId;
    private String name;
}
