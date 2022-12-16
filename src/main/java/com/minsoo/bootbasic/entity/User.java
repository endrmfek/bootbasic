package com.minsoo.bootbasic.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class User implements UserDetails { // security 적용
    /*
    UserDetails는 UserDetailsService를 통해 입력된 로그인 정보를 가지고
    데이터베이스에서 사용자 정보를 가져오는 역할을 수행한다.
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true)
    private String uid;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //??
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER) // ??
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //계정이 가지고 있는 권한 목록을 리턴
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        //계정 아이디 리턴
        return this.uid;
    }

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        // 계정이 만료됐는지? -> true는 만료되지 않았다.
        return true;
    }

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        //계정이 잠겼는지?? -> true는 잠기지 않았다.
        return true;
    }

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        //비밀번호가 만료됐는지?? -> true는 만료되지 않았다.
        return true;
    }

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        //계정이 활성화되있냐? -> true는 활성화.
        return true;
    }
}
