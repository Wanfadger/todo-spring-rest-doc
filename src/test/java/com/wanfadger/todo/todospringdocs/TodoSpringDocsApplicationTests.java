package com.wanfadger.todo.todospringdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.el.stream.Stream;
import com.wanfadger.todo.todospringdocs.models.Todo;
import com.wanfadger.todo.todospringdocs.repository.TodoRepository;
import org.assertj.core.util.Streams;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class , SpringExtension.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class TodoSpringDocsApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TodoRepository todoRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();


	@Before
	 void setUp(){
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
				.apply(documentationConfiguration(this.restDocumentation))
				.build();
	}



	@Test
	void contextLoads() {
	}


	@Test
	void persistTodoTest() throws Exception {
		Todo todo = new Todo();
		todo.setName("cooking food");
		String todoJson = objectMapper.writeValueAsString(todo);

		given(this.todoRepository.save(any(Todo.class))).willReturn(todo);

	MvcResult result =	this.mockMvc.perform(post("/todos")
				.content(todoJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			    .andDo(print())
				.andExpect(status().isOk())
			    .andExpect(content().json(todoJson))
			    .andDo(document("{methodName}" ,
						preprocessRequest(prettyPrint()) ,
						preprocessResponse(prettyPrint())))
		        .andReturn();

	assert(result.getResponse().getContentAsString()).equals(todoJson);

	}


	@Test
	void repoFetchByIdTest(){
		Todo todo = new Todo();
		todo.setName("Hello world");
		given(this.todoRepository.findById("id"))
				.willReturn(Optional.of(todo));

		assert(this.todoRepository.findById("id").get().getName())
				.equals("Hello world");

	}

	@Test
	void fetchById() throws Exception{
		Todo todo = new Todo();
		todo.setId("ff808181754eb9af01754ebb78110001");
		todo.setName("Taking breakfast");
		todo.setCreatedAt(LocalDateTime.parse("2020-10-22T08:15:10.225484" , DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		todo.setModifiedAt(LocalDateTime.parse("2020-10-22T08:15:10.225513" , DateTimeFormatter.ISO_LOCAL_DATE_TIME));

		//given
        given(this.todoRepository.findById(todo.getId())).willReturn(Optional.of(todo));

        //when
	  MvcResult result = this.mockMvc.perform(get("/todos/{id}" , todo.getId())
	  .contentType(MediaType.APPLICATION_JSON))
			   .andDo(print())
			   .andExpect(status().isOk())
			   .andExpect(content().json(objectMapper.writeValueAsString(todo)))
			  .andDo(document("{methodName}" ,
					  preprocessRequest(prettyPrint()) ,
					  preprocessResponse(prettyPrint())))
			   .andReturn();

	  //then
	  assert(result.getResponse().getContentAsString()).equals(objectMapper.writeValueAsString(todo));

	  assert(this.todoRepository.findById(todo.getId()).get().getName()).equals(todo.getName());
	}

	@Test
	public void fetchTodosTest() throws Exception{

		//given
		given(this.todoRepository.findAll()).willReturn(todos());

		//when
	 MvcResult result =	this.mockMvc.perform(get("/todos")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(todos())))
			    .andDo(document("{methodName}" ,
					 preprocessRequest(prettyPrint()) ,
					 preprocessResponse(prettyPrint())))
				.andReturn();

	 //then
		assert(result.getResponse().getContentAsString()).equals(objectMapper.writeValueAsString(todos()));

	}

	private Iterable<Todo> todos(){
	  return Arrays.asList(		new Todo( "ff808181754eb9af01754ebb48e00000", "Morninging Prayer ", LocalDateTime.parse("2020-10-22T08:14:58.144267" , DateTimeFormatter.ISO_LOCAL_DATE_TIME), LocalDateTime.parse("2020-10-22T08:14:58.144232" , DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
				new Todo( "ff808181754eb9af01754ebb78110001", "Taking breakfast", LocalDateTime.parse("2020-10-22T08:15:10.225513" , DateTimeFormatter.ISO_LOCAL_DATE_TIME), LocalDateTime.parse("2020-10-22T08:15:10.225484" , DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
				new Todo( "ff808181754eb9af01754ebbc0590002", "preparing todo list", LocalDateTime.parse("2020-10-22T08:15:28.729367" , DateTimeFormatter.ISO_LOCAL_DATE_TIME), LocalDateTime.parse("2020-10-22T08:15:28.729336" , DateTimeFormatter.ISO_LOCAL_DATE_TIME))
				);
	}


}
