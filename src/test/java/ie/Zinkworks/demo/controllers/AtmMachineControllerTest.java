package ie.Zinkworks.demo.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ie.Zinkworks.demo.dtos.AmountWithDrawRequest;
import ie.Zinkworks.demo.dtos.ResponseDto;
import ie.Zinkworks.demo.dtos.ResponseEnum;
import ie.Zinkworks.demo.services.AccountService;

@WebMvcTest(AtmMachineController.class)
public class AtmMachineControllerTest {

	private static final String WITHDRAW_REQUEST="{\r\n" + 
			"    \"pin\": \"1234\",\r\n" + 
			"    \"amountRequested\": 315\r\n" + 
			"}";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;
	
	
	@Test
	public void shouldReturnOkStatusWhenBankknotesFromAtm() throws Exception{
	
		when(accountService.withdraw(any(AmountWithDrawRequest.class))).thenReturn(new ResponseDto(ResponseEnum.SUCCESS.name(), "OK", HttpStatus.OK.value()));
		
		this.mockMvc.perform(put("/atm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(WITHDRAW_REQUEST)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
	}
	
	

	@Test
	public void shouldReturnNotFoundStatusWhenBankknotesFromAtmWhenAccountIsNotFound() throws Exception{
	
		when(accountService.withdraw(any(AmountWithDrawRequest.class))).thenReturn(new ResponseDto(ResponseEnum.FAIL.name(), "KO", HttpStatus.NOT_FOUND.value()));
		
		this.mockMvc.perform(put("/atm").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(WITHDRAW_REQUEST)).andExpect(MockMvcResultMatchers.status().isNotFound()).andDo(print());
	}
	
}
