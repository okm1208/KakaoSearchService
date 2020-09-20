package com.kakaobank.search.account.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 *  @author 오경무 ( okm1208@gmail.com )
 *  @since : 2020-09-15
 *  description : 계정 권한 Entity
 */

@Table(name="account_authorities")
@Entity
@Data
@IdClass(AccountRole.Ids.class)
public class AccountRole {

    @Id
    private Integer userNo;

    @Id
    @Column(length = 10, nullable = false)
    private String authority;


    public static class Ids implements Serializable {
        private Integer userNo;
        private String authority;

    }
}
