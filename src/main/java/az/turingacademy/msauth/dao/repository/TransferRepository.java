package az.turingacademy.msauth.dao.repository;

import az.turingacademy.msauth.dao.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Long> {
}
