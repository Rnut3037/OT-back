package com.onetouch.boards.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardReqDto {
    private String title;
    private String content;
    private Integer authorId;
}
