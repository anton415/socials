INSERT INTO users (id, username, email, password) VALUES
    ('11111111-1111-1111-1111-111111111111', 'alice', 'alice@example.com', 'password'),
    ('22222222-2222-2222-2222-222222222222', 'bob', 'bob@example.com', 'password'),
    ('33333333-3333-3333-3333-333333333333', 'charlie', 'charlie@example.com', 'password');

INSERT INTO posts (id, user_id, title, text, created) VALUES
    (
        '44444444-4444-4444-4444-444444444441',
        '11111111-1111-1111-1111-111111111111',
        'First post',
        'Alice shares her first post.',
        TIMESTAMP WITH TIME ZONE '2026-03-28 10:00:00+00:00'
    ),
    (
        '44444444-4444-4444-4444-444444444442',
        '22222222-2222-2222-2222-222222222222',
        'Travel update',
        'Bob posts a travel update.',
        TIMESTAMP WITH TIME ZONE '2026-03-29 12:30:00+00:00'
    ),
    (
        '44444444-4444-4444-4444-444444444443',
        '33333333-3333-3333-3333-333333333333',
        'Workout notes',
        'Charlie shares workout notes.',
        TIMESTAMP WITH TIME ZONE '2026-03-30 09:15:00+00:00'
    );

INSERT INTO images (id, post_id, image_url) VALUES
    (
        '55555555-5555-5555-5555-555555555551',
        '44444444-4444-4444-4444-444444444441',
        'https://example.com/images/alice-post-1.png'
    ),
    (
        '55555555-5555-5555-5555-555555555552',
        '44444444-4444-4444-4444-444444444442',
        'https://example.com/images/bob-travel.png'
    );

INSERT INTO subscriptions (follower_id, followee_id) VALUES
    ('11111111-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222'),
    ('11111111-1111-1111-1111-111111111111', '33333333-3333-3333-3333-333333333333');

INSERT INTO friend_requests (id, sender_id, recipient_id, status) VALUES
    (
        '66666666-6666-6666-6666-666666666661',
        '33333333-3333-3333-3333-333333333333',
        '11111111-1111-1111-1111-111111111111',
        'PENDING'
    );

INSERT INTO friendships (user_id, friend_id) VALUES
    ('11111111-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222'),
    ('22222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111');

INSERT INTO messages (id, sender_id, recipient_id, text) VALUES
    (
        '77777777-7777-7777-7777-777777777771',
        '11111111-1111-1111-1111-111111111111',
        '22222222-2222-2222-2222-222222222222',
        'Hi Bob, welcome to Socials!'
    );
