# Доменная модель Social Media

```mermaid
erDiagram
    USERS ||--o{ POSTS : creates
    POSTS ||--o{ IMAGES : has
    USERS ||--o{ SUBSCRIPTIONS : follower
    USERS ||--o{ SUBSCRIPTIONS : followee
    USERS ||--o{ FRIEND_REQUESTS : sends
    USERS ||--o{ FRIEND_REQUESTS : receives
    USERS ||--o{ FRIENDSHIPS : user
    USERS ||--o{ FRIENDSHIPS : friend
    USERS ||--o{ MESSAGES : sends
    USERS ||--o{ MESSAGES : receives

    USERS {
        uuid id PK
        varchar username UK
        varchar email UK
        varchar password
    }

    POSTS {
        uuid id PK
        uuid user_id FK
        varchar title
        text text
        timestamptz created
    }

    IMAGES {
        uuid id PK
        uuid post_id FK
        text image_url
    }

    SUBSCRIPTIONS {
        uuid follower_id PK, FK
        uuid followee_id PK, FK
    }

    FRIEND_REQUESTS {
        uuid id PK
        uuid sender_id FK
        uuid recipient_id FK
        varchar status
    }

    FRIENDSHIPS {
        uuid user_id PK, FK
        uuid friend_id PK, FK
    }

    MESSAGES {
        uuid id PK
        uuid sender_id FK
        uuid recipient_id FK
        text text
    }
```

## Требования по заданию

### 1. Аутентификация и авторизация

- Пользователи могут зарегистрироваться, указав имя пользователя, электронную почту и пароль.
- Пользователи могут войти в систему, предоставив правильные учетные данные.
- API должен обеспечивать защиту конфиденциальности пользовательских данных, включая хэширование паролей и использование JWT.

### 2. Управление постами

- Пользователи могут создавать новые посты, указывая текст, заголовок и прикрепляя изображения.
- Пользователи могут просматривать посты других пользователей.
- Пользователи могут обновлять и удалять свои собственные посты.

### 3. Взаимодействие пользователей

- Пользователи могут отправлять заявки в друзья другим пользователям. С этого момента, пользователь, отправивший заявку, остается подписчиком до тех пор, пока сам не откажется от подписки.
- Если пользователь, получивший заявку, принимает ее, оба пользователя становятся друзьями.
- Если пользователь, получивший заявку, отклонит ее, пользователь, отправивший заявку, все равно остается подписчиком.
- Пользователи, являющиеся друзьями, также являются подписчиками друг на друга.
- Если один из друзей удаляет другого из друзей, то он также отписывается. Второй пользователь при этом должен остаться подписчиком.
- Друзья могут писать друг другу сообщения.

### 4. Подписки и лента активности

- Лента активности пользователя должна отображать последние посты от пользователей, на которых он подписан.
- Лента активности должна поддерживать пагинацию и сортировку по времени создания постов.
