CREATE TABLE user
(
    id INT(20) PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age INT(11) NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱'
);

INSERT INTO user (id, name, age, email) VALUES
        (1, '张三', 18, '84669250@qq.com'),
        (2, '李四', 20, '84669250@qq.com'),
        (3, '王舞', 28, '84669250@qq.com'),
        (4, '田七', 21, '84669250@qq.com'),
        (5, '赵九', 24, '84669250@qq.com');

ALTER TABLE user ADD update_time DATETIME;
ALTER TABLE user ADD create_time DATETIME;
ALTER TABLE user ADD deleted INT COMMENT "0表示未删除 1表示逻辑删除";