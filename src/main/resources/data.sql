
INSERT INTO account
	(
		user_no
		, user_id
		, password
		, active
		, status
	)
VALUES
	(
		1
		, 'admin'
		, '$2a$10$cBGaBw4qj6f5GH1KYDQ2HO.C4OY42O89XF/g7iU1Mau5lyac/vcCm'
		, 'true'
		, 'ACTIVE'
	);

INSERT INTO account_authorities
	(
		user_no
		, authority
	)
VALUES
	(
		1
		, 'ROLE_USER'
	);


INSERT INTO keyword_history_statistics
    (
        stat_no
        , keyword
        , accumulate_count
        , reg_dt
    )
VALUES
    ( 1, '낙성대역', 10 , now()),
    ( 2, '강남역' , 29292 , now()),
    ( 3, '곱창집' , 29931 , now()),
    ( 4, '회식' , 19293, now()),
    ( 5, '영어학원' , 2883 , now()),
    ( 6, '카페' , 23993 , now()),
    ( 7, '버스정류장' ,12883 , now()),
    ( 8, '초등학교' , 23 , now()),
    ( 9, '중국집' , 488 , now()),
    ( 10, '맛집' , 984284 , now()),
    ( 11, '성동구' , 244020 , now()),
    ( 12, '관악구' , 21230 , now()),
    ( 13, '경기도맛집' , 23230 , now()),
    ( 14, '어린이집' , 23120 , now()),
    ( 15, '고속버스터미널' , 2290 , now()),
    ( 16, '아이스크림' , 1230 , now()),
    ( 17, '빵집' , 40 , now());