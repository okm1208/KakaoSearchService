package com.kakaobank.search.account.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 계정 Entity
 */
@Table(name="account")
@Entity
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userNo;

    @Column(length = 30, nullable = false, unique = true)
    private String userId;
    @Column(length = 200, nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="account_authorities" ,joinColumns = @JoinColumn(name = "userNo") )
    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private List<AccountAuthority> roles;

    @OneToMany(fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens;

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public enum AccountStatus {
        ACTIVE("활성"),
        LOCKED("잠김"),
        TERMINATE("탈퇴");

        final String desc;
    }

    public enum AccountAuthority implements GrantedAuthority {
        ROLE_ADMIN, ROLE_USER;

        @Override
        public String getAuthority() {
            return this.name();
        }

        @Override
        public String toString(){
            return this.name();
        }

        public static AccountAuthority findByName(String name){
            for(AccountAuthority v : values()){
                if( v.name().equals(name)){
                    return v;
                }
            }
            return null;
        }
    }
}
