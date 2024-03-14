package com.web.repository;

import com.web.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "select u from User u where u.username = ?1")
    Optional<User> findByUsername(String username);

    @Query(value = "select u from User u where u.username = ?1 and u.password = ?2")
    Optional<User> findByUsernameAndPassword(String username, String password);

    @Query(value = "select u.* from users u where u.id = ?1", nativeQuery = true)
    Optional<User> findById(Long id);

    @Query("select u from User u where u.role = ?1")
    public List<User> getUserByRole(String role);


    @Query("select u from User u where u.username like ?1 or u.fullName like ?1")
    public Set<User> searchByParam(String param);

    @Query(value = "select u.* from chat c inner join users u\n" +
            "where (c.sender = ?1 or c.receiver = ?1) and u.id != ?1 group by u.id", nativeQuery = true)
    public List<User> listUserChated(Long userId);


    @Query("select u from User u where u.fullName like ?1 or u.username like ?1")
    public Page<User> findByParam(String param, Pageable pageable);

    @Query("select u from User u where (u.fullName like ?1 or u.username like ?1) and u.role = ?2")
    public Page<User> findByParamAndRole(String param,String role, Pageable pageable);
}
