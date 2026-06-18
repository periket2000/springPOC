package es.mybi.demo.bdpoc.core.repository;

import es.mybi.demo.bdpoc.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
