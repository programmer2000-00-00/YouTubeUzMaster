package com.company.YouTubeUz.repository;

import com.company.YouTubeUz.entity.ProfileEntity;
import com.company.YouTubeUz.enums.ProfileStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {
    Optional<ProfileEntity> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set visible = :visible where id = :id")
    int updateVisible(@Param("visible") Boolean visible, @Param("id") Integer id);

    List<ProfileEntity> findByVisible(boolean b, Pageable pageable);

    Optional<ProfileEntity> findByEmailAndPassword(String email, String pswd);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status = :status where id = :id")
    void updateStatus(@Param("status") ProfileStatus status, @Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set attachId = :attachId where id = :id")
    void updateAttach(@Param("attachId") String attachId, @Param("id") Integer id);

    @Transactional
    @Modifying
    @Query(value = "update ProfileEntity set email = :email where  id = :id")
    int changeEmail(@Param("id") Integer userId, @Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "update ProfileEntity set password = :pass where  id = :id")
    void changePassword(@Param("id") Integer userId, @Param("pass") String password);

}
