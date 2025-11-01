SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname IN ('authuser_capcred_db', 'loan_capcred_db', 'payment_capcred_db', ' notification_capcred_db')
  AND pid <> pg_backend_pid();

CREATE DATABASE authuser_capcred_db;
CREATE DATABASE loan_capcred_db;
CREATE DATABASE payment_capcred_db;
CREATE DATABASE notification_capcred_db;
