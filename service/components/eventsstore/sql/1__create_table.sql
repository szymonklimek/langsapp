

CREATE TABLE IF NOT EXISTS events_store.events (
  id uuid NOT NULL,
  user_id uuid NOT NULL,
  event_type TEXT NOT NULL,
  event_data TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL
);
