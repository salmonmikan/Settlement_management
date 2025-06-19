CREATE TABLE department_master (
    department_id VARCHAR(5) NOT NULL PRIMARY KEY,
    department_name VARCHAR(20)
);

INSERT INTO department_master (department_id, department_name) VALUES
('D001', '管理部'),
('D002', '営業部'),
('D003', '総務部'),
('D004', '経理部'),
('D005', '生育部');