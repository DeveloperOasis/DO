package com.backend.controller;

import com.backend.dto.post.Post;
import com.backend.service.CommentService;
import com.backend.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Api(tags = {"Post"})
@RestController
@CrossOrigin(origins = {"*"})
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    /**
     * @param newPost : 사용자가 작성한 Post
     * @param uid     : 사용자 아이디
     * @return 글이 정상적으로 등록된 경우 HttpStatus 201 반환
     */
    @ApiOperation(value = "글 작성", notes = "새로운 포스트를 작성한다.")
    @PostMapping("/api/v2/{uid}")
    public HttpStatus save(@RequestBody Post newPost) throws Exception {
        try {
            postService.save(newPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HttpStatus.CREATED;
    }

    /**
     * @param pid : Post 가 가지고 있는 고유 식별번호
     * @return
     */
    @ApiOperation(value = "글 읽기", notes = "pid 를 통해 글 하나를 찾아서 반환")
    @GetMapping("/api/v2/p/{pid}")
    public Post findById(@PathVariable Long pid) {
        Post post = postService.findById(pid);
        post.setTag(postService.findAllPostTags(pid));
        post.setComments(commentService.findAllCommentsInPost(pid));
        return post;
    }

    /**
     * @param option : 키워드 없이 옵션별로 전체 글을 조회 (작성일 순, 조회순, 좋아요 순)
     * @return 글이 없으면 null 을 반환
     */
    @ApiOperation(value = "옵션에 따른 전체 글 조회", notes = "조회수, 좋아요 수, 최신순")
    @GetMapping("/api/v2/{option}")
    public List<Post> findAllOrderedByOption(@PathVariable String option) {
        List<Post> list = new ArrayList<>();

        if (option.equals("hits")) { // 조회수가 높은순으로 조회
            list = postService.findAllOrderedByHits();
        } else if (option.equals("likes")) { // 좋아요가 높은순으로 조회
            list = postService.findAllOrderedByLikes();
        } else if (option.equals("latest")) { // 최신글 순으로 조회
            list = postService.findAllOrderedByPublishedTime();
        }

        return list;
    }

    /**
     * @param keyword : 제목으로 키워드를 포함한 글들을 검색
     * @return 해당 키워드를 포함하는 글이 없으면 null 을 반환
     */
    @ApiOperation(value = "검색 : 키워드", notes = "제목에 키워드를 포함한 포스트를 조회")
    @GetMapping("/api/v2/find/t/{keyword}")
    public List<Post> findAllPostsByKeywords(@PathVariable String keyword) {
        List<Post> list = postService.findByTitle(keyword);
        return list;
    }

    /**
     * @param nickname : 사용자 닉네임으로 작성글 조회
     * @return 해당 사용자가 작성글이 없을 경우 null 반환
     */
    @ApiOperation(value = "검색 : 유저 닉네임", notes = "해당 닉네임의 사용자가 작성한 모든 글을 조회")
    @GetMapping("/api/v2/find/u/{nickname}")
    public List<Post> findAllPostsByUserName(@PathVariable String nickname) {
        List<Post> list = postService.findByNickName(nickname);
        return list;
    }

    /**
     * @param tag : 태그로 포스트 조회
     * @return 관련 포스트가 존재하지 않을 경우 null 반환
     */
    @ApiOperation(value = "검색 : 태그", notes = "해당 태그를 포함하는 모든 포스트를 조회")
    @GetMapping("/api/v2/find/a/{tag}")
    public List<Post> findAllPostByTag(@PathVariable String tag) {
        List<Post> list = postService.findByTag(tag);
        return list;
    }

    /**
     * @param pid    : 포스트의 pid
     * @param status : 좋아요 토글 상태값 true : 좋아요 활성 false : 좋아요 비활성
     */
    @ApiOperation(value = "좋아요 이벤트 처리", notes = "좋아요를 누른 게시물과 토글 상태값을 넘겨받는다")
    @PutMapping("/api/v2/likes/{pid}/{status}")
    public void updateLikes(@PathVariable Long pid, @PathVariable boolean status) {
        postService.onClickLikes(pid, status);
    }

    /**
     * 공지사항 불러오는 함수, 추후 여러개 공지사항을 불러오는 코드로 바뀔 수 있음
     */
    @ApiOperation(value = "가장 최신 공지사항 조회", notes = "가장 최신의 공지사항을 불러옴, admin = 1")
    @GetMapping("/api/v2/notice")
    public Post loadNotice() {
        return postService.findLatestNotice();
    }

    /**
     * 임시저장 기능 : 넘겨받은 글의 내용을 수정하고 isTemp 속성을 1로 만든다.
     * @param post
     * 나중에 임시저장이 아닌 저장이 된 글들에 대한 처리가 필요함
     */
    @ApiOperation(value = "임시저장", notes = "임시저장을 눌렀을 포스트 처리")
    @PutMapping("/api/v2/temp")
    public void temporarily(@RequestBody Post post) {
        postService.temporarily(post);
    }


    /**
     * 게시글 삭제 기능 : pid 에 해당하는 게시글을 삭제한다. 이 때 관련 댓글도 같이 삭제됨
     * @param pid : 게시글 번호
     */
    @ApiOperation(value = "게시글 삭제", notes = "pid와 일치하는 게시글 삭제")
    @DeleteMapping("/api/v2/{pid}")
    public void delete(@PathVariable Long pid) {
        postService.delete(pid);
    }

    /**
     * 게시글 수정 : 해당 게시물을 수정완료 버튼을 눌렀을 때 임시저장 상태의 글은 저장완료 상태로 바뀜
     * @param post
     */
    @ApiOperation(value = "게시글 수정", notes = "게시글 수정, 임시저장 글을 수정완료할 경우 상태 변경")
    @PutMapping("/api/v2")
    public void update(Post post) {
        postService.update(post);
    }
}
