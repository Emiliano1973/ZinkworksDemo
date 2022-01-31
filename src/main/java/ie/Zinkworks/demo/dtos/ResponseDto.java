package ie.Zinkworks.demo.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
public class ResponseDto {

	private final String response;
	private final String message;
	private final int statusCode;
	 @JsonInclude(JsonInclude.Include.NON_NULL)
	private final AccountDto accountDetails;
	
	 public ResponseDto(String response, String message, int statusCode, AccountDto accountDetails) {
		this.response = response;
		this.message = message;
		this.statusCode=statusCode;
		this.accountDetails = accountDetails;
	}
	 
	 public ResponseDto(String response, String message, int statusCode) {
			this(response, message, statusCode, null);
		}
	 
	 
}
