
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
		, 'ADMIN'
	);