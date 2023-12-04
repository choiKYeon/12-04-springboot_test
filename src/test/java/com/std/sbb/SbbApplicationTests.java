package com.std.sbb;

import com.std.sbb.article.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
class SbbApplicationTests {
	@Autowired
	private ArticleService articleService;

	@Test
	@DisplayName("스트림으로 데이터 쑤셔넣기")
	void contextLoads() {
		IntStream.rangeClosed(2, 305)
				.forEach(no -> articleService.create("테스트제목 %d번 입니다.".formatted(no), "테스트내용 %d번 입니다.".formatted(no), null));
	}

}
