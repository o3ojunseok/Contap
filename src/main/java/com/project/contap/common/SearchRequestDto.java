package com.project.contap.common;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequestDto {
    private List<String> searchTags;
    private int type; // 0 : or / 1 : and
    private int page;
    private int field; // 0 : 백엔드 /  1 : 프론트 / 2 : 디자인 / 3 : 전체
}
