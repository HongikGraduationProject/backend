package com.hongik.graduationproject.repository;

import com.hongik.graduationproject.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByKakaoId(Long kakaoId);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByImei(String imei);
    boolean existsByImei(String imei);
}