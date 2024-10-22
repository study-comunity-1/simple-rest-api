-- 책 도메인 테이블 생성
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,          -- 책 ID (Primary Key)
    title VARCHAR(255) NOT NULL,                   -- 책 제목
    author VARCHAR(255) NOT NULL,                  -- 저자
    publisher VARCHAR(255) NOT NULL,               -- 출판사
    price INT NOT NULL,                            -- 가격
    stock INT NOT NULL,                            -- 재고
    published_date TIMESTAMP NOT NULL,             -- 발행일
    page INT NOT NULL,                             -- 쪽수
    category VARCHAR(255) NOT NULL,                -- 카테고리
    description TEXT,                              -- 책 소개
    isbn VARCHAR(30) NOT NULL UNIQUE               -- 책 코드 (ISBN)
);

-- 유저 도메인 테이블 생성
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,          -- 유저 ID (Primary Key)
    name VARCHAR(255) NOT NULL,                    -- 이름
    email VARCHAR(255) NOT NULL UNIQUE,            -- 이메일 (Unique)
    password VARCHAR(255) NOT NULL,                -- 패스워드
    nickname VARCHAR(255) NOT NULL UNIQUE,         -- 닉네임 (Unique)
    join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 가입일
    phone_number VARCHAR(20) NOT NULL UNIQUE,      -- 전화번호 (Unique)
    address TEXT NOT NULL,                         -- 주소
    user_type ENUM('USER', 'ADMIN') NOT NULL       -- 회원 유형 (일반 유저, 관리자)
);

-- 결제 도메인 테이블 생성
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,          -- 결제 ID (Primary Key)
    payment_status ENUM('PENDING', 'COMPLETED',
                  'FAILED', 'CANCEL_REQUESTED', 'CANCEL_COMPLETED') NOT NULL,
                            -- 결제 상태 (대기, 완료, 실패, 취소 요청, 취소 완료)
    payment_amount INT NOT NULL,                   -- 결제 금액
    payment_quantity INT NOT NULL,                 -- 결제 수량
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 결제 일시
    payment_method ENUM('CREDIT_CARD', 'BANK_TRANSFER', 'CASH_DEPOSIT') NOT NULL,
                            -- 결제 방법 (신용카드, 계좌이체, 무통장입금)
    buyer_id BIGINT NOT NULL,                      -- 구매자 (외래 키)
    book_id BIGINT NOT NULL,                       -- 구매 책 (외래 키)

    FOREIGN KEY (buyer_id) REFERENCES users(id),   -- 구매자와 연관 (FK)
    FOREIGN KEY (book_id) REFERENCES books(id)     -- 책과 연관 (FK)
);