package com.backend.controller;

import com.backend.dto.post.Tag;
import com.backend.service.TagService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(tags = {"Tag"})
@RestController
@CrossOrigin(origins = {"*"})
public class TagController {

    @Autowired
    TagService tagService;

    @GetMapping("/api/v4")
    public List<Map<String, String>> makeWordCloud() {
        List<Tag> tagList = tagService.loadTopTags();
        List<Map<String, String>> resultList = new ArrayList<>();

        Collections.sort(tagList, new Comparator<Tag>() {
            @Override
            public int compare(Tag t1, Tag t2) {
                return (int) (t1.getHits() - t2.getHits());
            }
        });

        // 구간을 10개 구간으로 나눔 단, 태그의 총 개수가 10개가 안 될 경우 최소는 1로 보장 한다.
        int interval = tagList.size() / 10;
        if(interval == 0) interval += 1;

        int count = 0;
        Long size = 10L;
        for(Tag tag : tagList) {
            count += 1;
            Map<String, String> map = new HashMap<>();
            map.put("text", tag.getName());
            map.put("value", size.toString());
            resultList.add(map);
            if(count == interval) { // 구간이 다 채워지면 다음 구간의 크기로 변경
                count = 0;
                size *= 100L;
            }
        }

        return resultList;
    }
}