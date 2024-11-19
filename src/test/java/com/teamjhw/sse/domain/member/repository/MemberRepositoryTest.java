package com.teamjhw.sse.domain.member.repository;

import com.teamjhw.sse.domain.member.dto.MemberResDto;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@Disabled
@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Transactional
    public void 멤버DTO_조회() {
        Optional<MemberResDto> result = memberRepository.getMemberById(2L);
        Assertions.assertThat(result.get().favoriteTeamId()).isEqualTo(2);
    }
}
