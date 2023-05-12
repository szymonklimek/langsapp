

CREATE TABLE IF NOT EXISTS user_follow_query.follows (
  id uuid NOT NULL,
  user_id uuid NOT NULL,
  follower_user_id uuid NOT NULL,
  is_followed BOOLEAN NOT NULL,
  updated_at TIMESTAMP NOT NULL
);
