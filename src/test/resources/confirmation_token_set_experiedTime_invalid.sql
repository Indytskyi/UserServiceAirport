update confirmation_token
set expired_at = now()
where user_id = (select id from users where email = 'artem2003@gmail.com');