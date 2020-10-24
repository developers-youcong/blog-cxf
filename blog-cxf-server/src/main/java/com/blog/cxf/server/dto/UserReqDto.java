package com.blog.cxf.server.dto;

import lombok.Data;

/**
 * @description:
 * @author: youcong
 * @time: 2020/10/24 22:49
 */
@Data
public class UserReqDto {

    private Long ID;

    private String email;

    private String username;

    private String password;
}
