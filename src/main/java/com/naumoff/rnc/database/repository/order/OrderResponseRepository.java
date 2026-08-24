package com.naumoff.rnc.database.repository.order;

import com.naumoff.rnc.database.entities.order.ResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderResponseRepository extends JpaRepository<ResponseEntity, Long> {

}
