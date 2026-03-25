--liquibase formatted sql

--changeset antonserdyuchenko:001-init-socials-domain
-- Пользователи могут зарегистрироваться, указав имя пользователя, электронную почту и пароль.
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR NOT NULL UNIQUE,
    email VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL
);

-- Пользователи могут создавать новые посты, указывая текст, заголовок и прикрепляя изображения.
CREATE TABLE posts (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users (id),
    title VARCHAR NOT NULL,
    text TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Для поста можно хранить прикрепленные изображения.
CREATE TABLE images (
    id UUID PRIMARY KEY,
    post_id UUID NOT NULL REFERENCES posts (id),
    -- хранится не само изображение, а ссылка или путь к нему
    image_url TEXT NOT NULL
);

-- Лента активности пользователя должна отображать последние посты от пользователей, на которых он подписан.
-- таблица связи, показывает кто на кого подписан
CREATE TABLE subscriptions (
    -- кто подписался
    follower_id UUID NOT NULL REFERENCES users (id),
    -- на кого подписались
    followee_id UUID NOT NULL REFERENCES users (id),
    PRIMARY KEY (follower_id, followee_id)
);

-- Пользователи могут отправлять заявки в друзья другим пользователям.
CREATE TABLE friend_requests (
    id UUID PRIMARY KEY,
    -- кто отправил
    sender_id UUID NOT NULL REFERENCES users (id),
    -- кому отправили
    recipient_id UUID NOT NULL REFERENCES users (id),
    status VARCHAR NOT NULL
);

-- Пользователи, являющиеся друзьями, также являются подписчиками друг на друга.
CREATE TABLE friendships (
    user_id UUID NOT NULL REFERENCES users (id),
    friend_id UUID NOT NULL REFERENCES users (id),
    PRIMARY KEY (user_id, friend_id)
);

-- Друзья могут писать друг другу сообщения.
CREATE TABLE messages (
    id UUID PRIMARY KEY,
    -- кто отправил
    sender_id UUID NOT NULL REFERENCES users (id),
    -- кому отправили
    recipient_id UUID NOT NULL REFERENCES users (id),
    text TEXT NOT NULL
);
