package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    public User findByEmail(String email);
    public User findByKakaoId(Long kakaoId);

    Optional<User> findById(String id);
}