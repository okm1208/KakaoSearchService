# 장소 검색 서비스 
오픈 API를 활용하여 '장소 검색 서비스'를 웹 형태로 제공

# 시능
- 로그인 기능 
    - 사용자 id, password 로그인 
- 장소 검색 
    - 키워드를 활용한 장소 검색 기능
    - 페이징 기능 
- 장소 상세 조회 
    - 장소 상세 정보 및 카카오맵 링크 생성 
- 인기 검색어 제공 
    - 많이 검색된 키워드 리스트 제공 

# build & install
```bash
Using Maven: mvn package
```
jar 파일 다운로드

# Run
```bash
java -jar ./target/search-place-1.0.0.jar
```
# 서비스 접속 및 로그인 정보 
```bash
http://localhost:8080/index.html
```

- id : admin
- password : admin

# DB (h2) 접속
```bash
http://localhost:8080/h2
```
- id : admin
- password: admin5678
 
# Swaager UI 
```bash
http://localhost:8080/swagger-ui.html
```

## 개발 환경
- JDK 8
- maven 3.6.3
- Spring boot 2.3.3

## Open Source
- Spring Framework (https://spring.io/)
- Junit (https://junit.org/junit5/)
- Lombok (https://projectlombok.org/)
- Hibernate (https://hibernate.org/)
- H2 Database (https://www.h2database.com/html/main.html)
- AnguarJs (https://semantic-ui.com)
- UI Bootstrap (https://angular-ui.github.io/bootstrap) 
- Swagger (https://swagger.io/)
- Apache commons ( https://commons.apache.org )
    