package co.com.franquicia.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Entity
@Table(name = "franchise")
public class FranchiseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O AUTO, según tu base de datos
    private Long id;
    private String name;
}
