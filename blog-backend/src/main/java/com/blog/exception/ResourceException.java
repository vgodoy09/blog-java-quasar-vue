package com.blog.exception;

import org.springframework.http.HttpStatus;

public class ResourceException extends RuntimeException {
	private static final long serialVersionUID = -4052880149437789855L;

	public enum ResponseCode {
		/*200*/
		OK(200, 0), 
		CREATED(201, 0), 
		/*300*/
		
		/*400*/
		BAD_REQUEST(400, 0), 
		
		/*401 Attendant*/
		UNAUTHORIZED(401,0),
		UNAUTHORIZED_ATTENDANT(401, 1),
		
		FORBIDDEN(403,0),
		
		NOT_FOUND(404, 0), 
		NOT_ACCEPTABLE(406, 0),
		/*406 token*/
		BAD_TOKEN(406, 100),
		TOKEN_FORBIDDEN(403,101),
		
		/*406 person*/
		BAD_PERSON(406, 200),
		BAD_PERSON_EMAIL(406, 201), 
		BAD_PERSON_NAME(406, 202),
		BAD_PERSON_BIRTHDATE(406, 202),
		BAD_PERSON_PHONE(406, 203),
		BAD_PERSON_GENDER(406, 204),
		BAD_PERSON_DOCUMENT(406, 205),
		BAD_PERSON_PASSWORD(406, 206),
		BAD_PERSON_SEARCH(406, 207), 
		BAD_PERSON_CPF_NOT_UPDATE_WHEN_ALREADY_EXIST(406, 208), 
		BAD_PERSON_EMPLOYEE(406, 209), 
		
		/*406 person address*/
		BAD_ADDRESS(406, 250),
		BAD_CEP(406, 251),
		BAD_CEP_FORMAT(406, 252),
		BAD_ADDRESS_NUMBER(406, 253),
		BAD_CEP_NOT_NULL(406, 254),
		
		/*406 donation*/
		BAD_DONATION(406, 300),
		BAD_DONATION_PAYMENT_METHOD(406, 301),
		BAD_DONATION_FLAG(406, 302),
		BAD_DONATION_PARCEL(406, 303),
		BAD_DONATION_VALUE(406, 304),
		BAD_DONATION_BETTER_DAY(406, 305),
		BAD_DONATION_BILLET_TAG_TYPE(406, 306),
		BAD_DONATION_CURRENCY(406, 307),
		BAD_DONATION_BANK(406, 308),
		BAD_DONATION_FINANCIAL_COMPANY(406, 309),
		BAD_DONATION_PAYMENT_TYPE(406, 310),
		BAD_DONATION_PAYMENT_DATE(406, 311),
		/*406 cartão de crédito*/
		BAD_DONATION_CR_NAME(406, 320),
		BAD_DONATION_CR_NUMBER(406, 321),
		BAD_DONATION_CR_SECURITY(406, 322),
		BAD_DONATION_CR_MONTH(406, 323),
		BAD_DONATION_CR_YEAR(406, 324),
		BAD_DONATION_CR_DUE_DATE(406, 325),
		/*406 Automatic Debit*/
		BAD_DONATION_AD_BRANCH(406, 330),
		BAD_DONATION_AD_BRANCH_DIGIT(406, 331),
		BAD_DONATION_AD_ACCOUNT(406, 332),
		BAD_DONATION_AD_ACCOUNT_DIGIT(406, 333),
		BAD_DONATION_AD_OPERATIONAL_CODE(406, 334),
		BAD_DONATION_IS_PAID(406, 335),
		
		CAN_NOT_CREATE_DONATION(406, 340),
		CAN_NOT_CREATE_PERSON(406, 341),
		CAN_NOT_CREATE_ADRESS(406, 342),
		CAN_NOT_CREATE_DOCUMENT(406, 343),
		CAN_NOT_UPDATE_PERSON(406, 344),
		
		/*406 PayPal*/
		BAD_DONATION_PAYPAL(406, 350),
		BAD_DONATION_PAYPAL_PARCEL(406, 351),
		BAD_DONATION_PAYPAL_REFERENCE_ID(406, 352),
		BAD_DONATION_PAYPAL_URL(406, 353),
		BAD_DONATION_PAYPAL_STATUS(406, 354),
		BAD_DONATION_PAYPAL_PAID_VALUE(406, 355),
		
		/*406 issue*/
		CAN_NOT_CREATE_ISSUE(406, 400),
		
		/*406 attendance*/
		CAN_NOT_CREATE_ATTENDANCE(406, 500),
		CAN_NOT_CLOSE_ATTENDANCE(406, 501),
		
		/*406 initiative*/
		BAD_INITIATIVE(406, 550),
		
		/*406 service*/
		CAN_NOT_CREATE_SERVICE_DONATION(406, 600),
		CAN_NOT_CREATE_SERVICE_MAINTENANCE(406, 601),

		/*406 financial*/
		BAD_FINANCIAL_RECORD(406, 700),
		FINANCIAL_RECORD_DEBIT_SENT_TO_BANK(406, 701), 
		FINANCIAL_RECORD_IS_ACTIVE(406, 702),
		FINANCIAL_RECORD_IS_INACTIVE(406, 703), 
		FINANCIAL_RECORD_DEFEATED(406, 704),
		
		BAD_BANKSLIP(404, 800),
		BAD_BANKSLIP_NOT_REGISTERED(500, 801),
		BAD_BANKSLIP_EMPTY(406, 802),
		
		BAD_ANGEL(406, 900),
		BAD_ANGEL_NAME(406, 901),
		BAD_ANGEL_BIRTHDATE(406, 902),
		BAD_ANGEL_PHONE(406, 903),
		CAN_NOT_CREATE_ANGEL(406, 904),
		CAN_NOT_UPDATE_ANGEL(406, 905),
		CAN_NOT_LIST_ANGEL(406, 906),
		BAD_ANGEL_STATUS(406, 907),
		BAD_ANGEL_PERSON(406, 908),
		CAN_NOT_CREATE_FINANCIAL_RECORD(406, 901),
		
		BAD_DONATION_ANGEL_VALUE(406, 1001), 
		BAD_DONATION_ANGEL(406, 1002), 
		
		BAD_PENDENCY(406, 1100),
		
		BAD_SMS(406, 1200),
		
		/*500*/
		INTERNAL_SERVER_ERROR(500, 0), 
		NOT_IMPLEMENTED(501, 0),  
		
		BAD_DONATION_BETTER_DAY_SERVER(500, 305), 
		
		
		;private Integer httpStatus;
		private Integer internalCode;
		
		private ResponseCode(Integer httpStatus, Integer internalCode) {
			this.httpStatus = httpStatus;
			this.internalCode = internalCode;
		}
		
		public Integer getHttpStatus() {
			return this.httpStatus;
		}
		
		public Integer getInternalCode() {
			return this.internalCode;
		}
	}
	
	private final Integer httpStatusValue;
	private final Integer code;
	
	public ResourceException(ResponseCode responseCode, String message) {
		super(message);
		this.httpStatusValue = responseCode.getHttpStatus();
		this.code = responseCode.getInternalCode();
	}
	
	public ResourceException(HttpStatus httpStatus, Integer code, String message) {
		super(message);
		this.httpStatusValue = httpStatus.value();
		this.code = code;
	}

	public Integer getHttpStatusValue() {
		return httpStatusValue;
	}
	public Integer getCode() {
		return code;
	}

}
