-- Core master tables
CREATE TABLE IF NOT EXISTS dept (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS employee (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  emp_no VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(100) NOT NULL,
  join_year INT NOT NULL,
  base_salary BIGINT NOT NULL,
  position VARCHAR(50) NOT NULL,
  work_status VARCHAR(20) NOT NULL,
  work_region VARCHAR(20) NOT NULL,
  email VARCHAR(120),
  phone VARCHAR(40),
  notes TEXT,
  dept_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_employee_dept FOREIGN KEY (dept_id) REFERENCES dept(id)
);

-- Evaluation item category
CREATE TABLE IF NOT EXISTS eval_category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL UNIQUE
);

-- Evaluation item master (EAV ready)
CREATE TABLE IF NOT EXISTS eval_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(200) NOT NULL,
  category_id BIGINT NOT NULL,
  max_score INT NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  description VARCHAR(500),
  CONSTRAINT fk_eval_item_category FOREIGN KEY (category_id) REFERENCES eval_category(id)
);

-- Evaluation memo per employee
CREATE TABLE IF NOT EXISTS employee_memo (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  employee_id BIGINT NOT NULL,
  project_name VARCHAR(200),
  period VARCHAR(100),
  detail TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_memo_employee FOREIGN KEY (employee_id) REFERENCES employee(id)
);

-- Evaluation record (EAV: value table)
CREATE TABLE IF NOT EXISTS evaluation (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  employee_id BIGINT NOT NULL,
  eval_item_id BIGINT NOT NULL,
  score INT NOT NULL,
  CONSTRAINT fk_eval_employee FOREIGN KEY (employee_id) REFERENCES employee(id),
  CONSTRAINT fk_eval_item FOREIGN KEY (eval_item_id) REFERENCES eval_item(id)
);

-- Salary raise table per cycle
CREATE TABLE IF NOT EXISTS salary_raise (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  employee_id BIGINT NOT NULL,
  raise_percent DECIMAL(5,2) NOT NULL,
  raised_salary BIGINT NOT NULL,
  CONSTRAINT fk_raise_employee FOREIGN KEY (employee_id) REFERENCES employee(id)
);

-- Seed data
INSERT INTO dept(name) VALUES ('개발팀'), ('인사팀') ON DUPLICATE KEY UPDATE name = VALUES(name);
INSERT INTO eval_category(name) VALUES ('근태'), ('기술개발') ON DUPLICATE KEY UPDATE name = VALUES(name);
INSERT INTO eval_item(name, category_id, max_score, enabled, description)
VALUES ('근태', (SELECT id FROM eval_category WHERE name='근태'), 10, 1, '출결 성실도'),
       ('적극성', (SELECT id FROM eval_category WHERE name='기술개발'), 20, 1, '업무 태도')
ON DUPLICATE KEY UPDATE name = VALUES(name);



