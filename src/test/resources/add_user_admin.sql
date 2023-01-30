INSERT INTO  users (email, enabled, locked, password, role)
values('artem2004@gmail.com', true, false, '$2a$10$zJNFrmIRlkWWylopLuSxNu6x5xWkSf2vTQ7Nmc.CaVMvfg9A3hGzm', 'ADMIN');
INSERT INTO passenger (data_birth, first_name, gender, last_name, photo, user_id)
values('2005-06-18', 'Ivan', 'MALE', 'Ivanov', 'photo.jpg', (select id from users where email = 'artem2004@gmail.com'));
INSERT INTO confirmation_token ( expired_at, local_date_time, token, user_id)
values(now() + interval '15 minutes', now(), 'd34e927f-9f9e-4feb-a6b4-cdff1465df6b', (select id from users where email = 'artem2004@gmail.com'));