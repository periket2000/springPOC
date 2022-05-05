package es.mybi.demo.core.repository;

import es.mybi.demo.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
