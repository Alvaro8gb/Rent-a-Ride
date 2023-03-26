package es.ucm.fdi.iw;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.iw.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaSpecificationExecutor<Vehicle> {

    default List<Vehicle> search(String name, Integer seats) {
        return findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (name != null && !name.isEmpty()) {
                predicates.add(builder.like(root.get("name"), "%" + name + "%"));
            }
            if (seats != null) {
                predicates.add(builder.equal(root.get("seats"), seats));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }

}