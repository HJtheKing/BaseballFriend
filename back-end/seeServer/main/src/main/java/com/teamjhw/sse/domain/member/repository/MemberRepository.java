package com.teamjhw.sse.domain.member.repository;

import com.teamjhw.sse.domain.member.dto.MemberResDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<MemberResDto> getMemberById(Long id) {
        String sql = "SELECT member_id, favorite_team_id FROM member WHERE member_id = ?";
        try {
            MemberResDto member = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{id},
                    (rs, rowNum) -> new MemberResDto(rs.getLong("member_id"), rs.getLong("favorite_team_id"))
            );
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}