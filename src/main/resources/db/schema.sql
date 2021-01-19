CREATE TABLE IF NOT EXISTS member (
  member_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL unique,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(30) NOT NULL,
  email VARCHAR(255) NOT NULL,
  phone_number VARCHAR(30) NOT NULL,
  birth_date DATE,
  address VARCHAR(255)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS orders (
  order_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  member_id BIGINT UNSIGNED NOT NULL,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  created_at DATETIME,
  updated_at DATETIME,
  status VARCHAR(20) NOT NULL,
  FOREIGN KEY (member_id) REFERENCES member(member_id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS estimate (
  estimate_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT UNSIGNED NOT NULL,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  price BIGINT NOT NULL,
  duration VARCHAR(255) NOT NULL,
  created_at DATETIME,
  updated_at DATETIME,
  status VARCHAR(20) NOT NULL,
  FOREIGN KEY (order_id) REFERENCES orders(order_id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS file (
  file_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT UNSIGNED NOT NULL,
  original_file_name VARCHAR(255) NOT NULL,
  file_name VARCHAR(255) NOT NULL,
  file_path VARCHAR(255) NOT NULL,
  file_size BIGINT NOT NULL,
  created_at DATETIME,
  updated_at DATETIME,
  FOREIGN KEY (order_id) REFERENCES orders(order_id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS dashboard (
  dashboard_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT UNSIGNED NOT NULL,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  created_at DATETIME,
  updated_at DATETIME,
  FOREIGN KEY (order_id) REFERENCES orders(order_id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS figure (
  figure_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  dashboard_id BIGINT UNSIGNED NOT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  original_file_name VARCHAR(255),
  file_name VARCHAR(255),
  created_at DATETIME,
  updated_at DATETIME,
  FOREIGN KEY (dashboard_id) REFERENCES dashboard(dashboard_id)
) engine=InnoDB;