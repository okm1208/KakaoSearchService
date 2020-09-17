package com.kakaobank.search.account.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="account_authorities")
@Entity
@Data
@IdClass(AccountRole.Ids.class)
public class AccountRole {

    @Id
    private Integer userNo;
    @Id
    private String authority;


    public static class Ids implements Serializable {
        private Integer userNo;
        private String authority;

    }
}
