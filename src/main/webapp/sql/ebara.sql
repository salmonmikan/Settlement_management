CREATE TABLE department_master (
    department_id VARCHAR(5) NOT NULL PRIMARY KEY,
    department_name VARCHAR(20)
);

INSERT INTO department_master (department_id, department_name) VALUES
('D001', '営業部'),
('D002', '管理部');