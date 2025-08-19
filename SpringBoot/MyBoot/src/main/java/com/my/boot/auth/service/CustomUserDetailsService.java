package com.my.boot.auth.service;

import com.my.boot.user.entity.User;
import com.my.boot.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  /*
  * 참고
  *
  *
  * // 리포지토리: role_cd 원문만 조회 (JPQL)
@Query("select r.roleCd from MemberRole r where r.userId = :userId")
Optional<String> findRoleCsvByUserId(@Param("userId") Long userId);

// 서비스: 분해 + 정렬
List<String> roles = repo.findRoleCsvByUserId(userId)
    .map(csv -> Arrays.stream(csv.split(","))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .sorted(Comparator.comparingInt((String s) ->
            switch (s) {
                case "USER" -> 1;
                case "MANAGER" -> 2;
                case "ADMIN" -> 3;
                default -> 4;
            }).thenComparing(s -> s))
        .toList())
    .orElseGet(List::of);
    * @Entity @Table(name = "hr_member_roles")
public class MemberRole {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 또는 String (DB에 맞춤)

    @Column(name = "role_cd", nullable = false)
    private String roleCd;

    // ... equals/hashCode 등
}

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

    @Query("""
        select r.roleCd
        from MemberRole r
        where r.userId = :userId
        order by case
            when r.roleCd = 'USER' then 1
            when r.roleCd = 'MANAGER' then 2
            when r.roleCd = 'ADMIN' then 3
            else 4
        end, r.roleCd
        """)
    List<String> findRoleListByUserId(@Param("userId") Long userId);
}

*
*
*
*
*
  *
  *
  *
  * */





  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
    log.info("----------------loadUserByUsername-----------------------------");

    User user = userRepository.selectUserWithRoles(loginId).orElseThrow(
        () ->  new UsernameNotFoundException("사용자 없음"));

    // 권한 정보 별도 조회 (성능 최적화)
    List<String> roleNames = memberMapper.selectRoleListByUserId(userId);
    // 해당 부분 변환 필요
    // dto > EncryptService > MockSafeDb 가져오는 작업이 필요 ~


    MemberDTO memberDTO = new MemberDTO(
        member.getUserId(),
        member.getPasswd(),
        member.getSalt(),
        member.getUserNm(),
        roleNames,
        member.getPasswdChangeDate(),
        member.getRnkNm(),
        member.getRspofcNm(),
        member.getOrgId(),
        encryptService // 🔥 복호화 서비스 전달
    );

    log.info("✅ 사용자 인증 성공: userId={}, userNm={}, roles={}",
        memberDTO.getUserId(), memberDTO.getUserNm(), roleNames);

    return memberDTO;
  }
}
