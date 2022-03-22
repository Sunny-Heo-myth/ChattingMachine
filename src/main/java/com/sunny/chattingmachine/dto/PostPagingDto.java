package com.sunny.chattingmachine.dto;

import com.sunny.chattingmachine.domain.BriefPostInfo;
import com.sunny.chattingmachine.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class PostPagingDto {

    private int totalPageCount;
    private int currentPageNum;
    private long totalElementCount;
    private int currentPageElementCount;
    private List<BriefPostInfo> briefPostInfos = new ArrayList<>();

    public PostPagingDto(Page<Post> searchResults) {
        this.totalPageCount = searchResults.getTotalPages();
        this.currentPageNum = searchResults.getNumber();
        this.totalElementCount = searchResults.getTotalElements();
        this.currentPageElementCount = searchResults.getNumberOfElements();
        this.briefPostInfos = searchResults.getContent().stream()
                .map(BriefPostInfo::new)
                .toList();
    }
}
