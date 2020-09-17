
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
		, '$2a$10$hFQj6wJFFRECCqS4cjkJ7OzbKeYfWVjlS9ln12eXyZz3M0aP.BYTa'
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