package com.onetouch.boards.service;

import com.onetouch.boards.dto.BoardReqDto;
import com.onetouch.boards.entity.BoardEntity;
import com.onetouch.boards.repository.BoardRepository;
import com.onetouch.boards.vo.UserInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final RestTemplate restTemplate;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
        this.restTemplate = new RestTemplate();
    }

    // 전체 게시글 조회
    public List<BoardEntity> getAllBoards() {
        return boardRepository.findAll();
    }

    // 단일 게시글 조회
    public BoardEntity getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }

    // 게시글 작성
    public BoardEntity createBoard(BoardReqDto dto) {
        // 작성자 존재 확인 (users 서비스 호출)
        getUsernameByAuthorId(dto.getAuthorId()); // 존재하지 않으면 예외

        BoardEntity board = BoardEntity.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .authorId(dto.getAuthorId())
                .build();

        return boardRepository.save(board);
    }

    // 게시글 수정
    public BoardEntity updateBoard(Long id, BoardReqDto dto) {
        BoardEntity board = getBoardById(id);
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        return boardRepository.save(board);
    }

    // 게시글 삭제
    public BoardEntity deleteBoard(Long id) {
        BoardEntity board = getBoardById(id);
        boardRepository.delete(board);
        return board;
    }

    // 사용자 이름 조회 (users 서비스 호출)
    public String getUsernameByAuthorId(Integer authorId) {
        String url = "http://localhost:8080/users/info/" + authorId; //개발용
        // String url = "백엔드 pod 주소:8080/users/info/" + authorId; 배포용
        ResponseEntity<UserInfoDto> response =
                restTemplate.getForEntity(url, UserInfoDto.class);
        if (response.getBody() == null) {
            throw new RuntimeException("작성자 정보 조회 실패");
        }
        return response.getBody().getUsername();
    }
}
