delete
from confirmation_token using users
where confirmation_token.user_id = users.id;

delete
from passenger using users
where passenger.user_id = users.id;

delete
from refresh_token using users
where refresh_token.user_id = users.id;

delete
from users
where email = 'artem2004@gmail.com';