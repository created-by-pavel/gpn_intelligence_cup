package ru.crbpavel.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApiUser, Long> {
    Optional<ApiUser> findByLogin(String login);
}
