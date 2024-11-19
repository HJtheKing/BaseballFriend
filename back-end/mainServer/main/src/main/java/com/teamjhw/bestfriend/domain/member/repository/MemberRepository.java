package com.teamjhw.bestfriend.domain.member.repository;


import com.teamjhw.bestfriend.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /*
     * * email로 회원 조회
     */
    Optional<Member> findByEmail(String email);

    /**
     * email 존재 여부 확인
     * */
    Boolean existsByEmail(String email);
}