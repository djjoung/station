package chargingstation;

//import java.util.function.LongConsumer;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="managements", path="managements")
public interface ManagementRepository extends JpaRepository<Management, Long>{
    
    Management findByOrderId(Long orderId);

}
