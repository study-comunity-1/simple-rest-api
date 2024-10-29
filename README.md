# simple-rest-api
스터디 커뮤니티 1기 첫째 주 REST 실습


## 📝 설명 (Description)

![image](https://github.com/user-attachments/assets/3e38559d-3c89-4467-a827-361d2634db39)



## 도메인별 기능 및 필드

#### 책 도메인
	
	(books)
	인덱스 	id BIGINT AUTO_INCREMENT PRIMARY KEY
	책 제목	title VARCHAR(255) NOT NULL
	저자 		author VARCHAR(255) NOT NULL
	출판사 	publisher VARCHAR(255) NOT NULL
	가격 		price INT NOT NULL
	재고 		stock INT NOT NULL
	발행일 	published_date TIMESTAMP NOT NULL
	쪽 수 		page INT NOT NULL
	카테고리 	category VARCHAR(255) NOT NULL
	책 소개 	description TEXT
	책 코드 	isbn VARCHAR(30) NOT NULL UNIQUE

기능 : 목록 리스트, 상세 목록, 페이징, 추가, 삭제, 수정, 책 재고(매입시 재고++), 책 카테고리별 조회, 작가별 조회(작가가 쓴 책만 모아보기) => 책 추가,삭제,수정...은 관리자만 가능하다


#### 유저 도메인

	(users)
	인덱스 	id BIGINT AUTO_INCREMENT PRIMARY KEY
	이름 		name VARCHAR(255) NOT NULL
	이메일 	email VARCHAR(255) NOT NULL UNIQUE 
	패스워드 	password VARCHAR(255) NOT NULL
	닉네임 	nickname VARCHAR(255) NOT NULL UNIQUE 
	가입일 	join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	전화번호 	phone_number VARCHAR(20) NOT NULL UNIQUE
	주소 		address TEXT NOT NULL
	회원 유형 	user_type ENUM('USER', 'ADMIN') NOT NULL
			(일반 유저, 관리자)

기능 :  회원가입, 로그인, 로그아웃, 회원 탈퇴, 회원 수정


#### 결제 도메인 
		
	(payments))
	인덱스 	id BIGINT AUTO_INCREMENT PRIMARY KEY
	결제 상태 	payment_status ENUM('PENDING', 'COMPLETED', 'FAILED', 'CANCEL_REQUESTED', 'CANCEL_COMPLETED') NOT NULL
			(결제 대기, 결제 완료, 결제 실패, 결제취소요청, 결제취소완료)
	결제 금액 	payment_amount INT NOT NULL
	결제 수량 	payment_quantity INT NOT NULL
	결제 일시 	payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	결제 방법 	payment_method ENUM('CREDIT_CARD', 'BANK_TRANSFER', 'CASH_DEPOSIT') NOT NULL
			(신용카드, 계좌이체, 무통장입금)
	구매자 	buyer_id BIGINT NOT NULL
	구매책	book_id BIGINT NOT NULL

	FOREIGN KEY (buyer_id) REFERENCES users(id)
	FOREIGN KEY (book_id) REFERENCES books(id)

기능 :	 <br/>
	구매자 관련) <br/>
	결제 생성 :  로그인 여부 확인 -> 책 재고, 금액 비교해서 결제가 가능한지 아닌지 확인 그 후에 결제대기 혹은 결제 실패 <br/>
	결제 조회 :  로그인 여부 확인 -> 해당 유저의 최근 결제 내역 조회  <br/>
	결제 수정 :  로그인 여부 확인 -> 결제 대기인 경우에만 수정 가능 (결제 방법, 수량 등 수정) <br/>
	결제 취소 :  로그인 여부 확인 -> 결제 대기인 경우에는 자동으로 취소 가능 -> 결제취소완료 상태 <br/>
						   결제 완료상태에서는 관리자가 확인 후 취소 -> 결제취소요청 상태 <br/>

		(결제 대기상태에서 재고--, 결제취소완료 상태가 되면 재고++) <br/>

	판매자(=> 관리자) 관련) <br/>
	판매 내역 조회 : 관리자로 로그인 -> (모든 구매자의 구매내역 == 판매내역) 을 조회 <br/>
	취소 관리 : 관리자로 로그인 -> 구매자가 결제완료된 상태의 구매건을 취소하고자하는경우 (결제취소요청 상태인 경우), 관리자가 확인 후 수동으로 취소 -> 결제취소완료 상태 <br/>
							(결제취소완료시 책 재고++)

