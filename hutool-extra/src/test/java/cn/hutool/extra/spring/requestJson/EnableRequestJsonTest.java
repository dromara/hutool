package cn.hutool.extra.spring.requestJson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by useheart on 2023/7/11
 * @author useheart
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EnableRequestJsonApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EnableRequestJsonTest {

	@Autowired
	private MockMvc mockMvc;

	public static class TestA {
		public String a = "testA";

		@Override
		public String toString() {
			return "TestA{" +
					"a='" + a + '\'' +
					'}';
		}
	}

	public static class TestB {
		public String b = "testB";

		@Override
		public String toString() {
			return "TestB{" +
					"b='" + b + '\'' +
					'}';
		}
	}

	public static class TestJson {
		public boolean bool = true;
		public int int1 = 11;
		public Long long1 = 1111L;
		public String str = "hello world";
		public Object object = new Object();
		public TestA testA = new TestA();
		public TestB testB = new TestB();
		public List<TestA> testAList = Arrays.asList(new TestA(), new TestA());
		public Set<Object> objectSet = new HashSet<>(Arrays.asList(new TestA(), new TestB()));
		public Map<String, TestB> testMap = Collections.singletonMap("testB", new TestB());
	}

	@Test
	public void test() throws Exception {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		final String content = objectMapper.writeValueAsString(new TestJson());
		System.out.println("requestBefore" + content);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/test")
						.contentType(MediaType.APPLICATION_JSON)
						.content(content))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("success")));
	}
}
