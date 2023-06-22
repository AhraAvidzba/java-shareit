DROP TABLE booking;
DROP TABLE items;
DROP TABLE item_requests;
DROP TABLE users;

CREATE TABLE IF NOT EXISTS users (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar(100),
email varchar(320) );

CREATE TABLE IF NOT EXISTS item_requests (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
description varchar(1000),
creation_date timestamp,
user_id BIGINT,
CONSTRAINT fk_request_to_user FOREIGN KEY(user_id) REFERENCES users(id) );

CREATE TABLE IF NOT EXISTS items (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name varchar(100),
description varchar(1000),
available boolean,
user_id BIGINT,
request_id BIGINT,
CONSTRAINT fk_items_to_users FOREIGN KEY(user_id) REFERENCES users(id),
CONSTRAINT fk_items_to_request FOREIGN KEY(request_id) REFERENCES item_requests(id) );

CREATE TABLE IF NOT EXISTS booking (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
_start timestamp,
_end timestamp,
item_id BIGINT,
user_id BIGINT,
_status varchar,
CONSTRAINT fk_booking_to_item FOREIGN KEY(item_id) REFERENCES items(id),
CONSTRAINT fk_booking_to_user FOREIGN KEY(user_id) REFERENCES users(id) );

