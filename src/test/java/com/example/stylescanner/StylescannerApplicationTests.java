package com.example.stylescanner;

import com.example.stylescanner.instagram.util.InstagramGraphApiUtil;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@SpringBootTest
class StylescannerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("instagram graph api 연결 테스트")
	void instagram_api() throws IOException, JSONException {
		InstagramGraphApiUtil util = new InstagramGraphApiUtil();
		util.SearchCeleb("you_r_love");
	}


}
