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

    public BoardEntity createBoard(BoardReqDto dto) {
        BoardEntity board = BoardEntity.builder()
            .title(dto.getTitle())
            .content(dto.getContent())
            .authorId(dto.getAuthorId()) // ✅ 이게 맞는 버전
            .build();
    return boardRepository.save(board);
}

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
        // String url = "http://localhost:8080/users/info/" + authorId; //개발용
        String url = "http://users.default.svc.cluster.local:8080/users/info/" + authorId;
        ResponseEntity<UserInfoDto> response =
                restTemplate.getForEntity(url, UserInfoDto.class);
        if (response.getBody() == null) {
            throw new RuntimeException("작성자 정보 조회 실패");
        }
        return response.getBody().getUsername();
    }

    public Integer getAuthorIdByUsername(String username) {
    try {
        ResponseEntity<UserInfoDto> response = restTemplate.getForEntity(
            "http://users.default.svc.cluster.local:8080/api/users/username/" + username,
            UserInfoDto.class
        );
        return response.getBody().getId();
    } catch (Exception e) {
        throw new RuntimeException("작성자 조회 실패: " + username);
    }
}

}
