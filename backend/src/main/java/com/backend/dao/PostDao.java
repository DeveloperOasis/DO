package com.backend.dao;

import com.backend.dto.post.Post;

import java.util.List;

public interface PostDao {

    public Long save(Post post); // 글 작성
    public Post findById(Long id); // 포스트 고유 번호(pid) 로 조회
    public List<Post> findByTitle(String title); // 제목으로 조회
    public List<Post> findByNickName(String nickname); // 사용자 이름으로 작성글 조회
    public List<Post> findByTag(String tag); // 태그로 포스트 조회

    public List<Post> findAllOrderedByHits(); // 조회순으로 조회
    public List<Post> findAllOrderedByLikes(); // 좋아요 순으로 조회
    public List<Post> findAllOrderedByPublishedTime(); // 최신글 순으로 조회
    //public List<Post> findAll(); // 모든 글을 조회

//    public void update(Long id); // 게시글 수정
//    public void delete(Long id); // 게시글 삭제
    public void increaseHits(Long pid); // 포스트 조회시 조회수 증가
    public void increaseLikes(Long pid); // 게시글 좋아요 등록
    public void decreaseLikes(Long pid); // 게시글 좋아요 해제

    public List<String> findAllPostTags(Long pid); // 포스트에 등록된 태그 정보를 가져온다.

}
