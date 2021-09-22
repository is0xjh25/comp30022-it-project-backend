USE testdb;

-- -----------------------------------------------------
-- User
-- -----------------------------------------------------
INSERT INTO `user` (id,email,password,first_name,last_name,phone) VALUES
(1, 'lingxiao1@student.unimelb.edu.au', '$2a$10$xJ1uFvSo4HoDKpmjGfGzD.pO5p0fRr/ApAegBkaXazKv.YK8TGqw2', 'lingxiao', 'li', '188188'),
(2, 'yiyahuang@student.unimelb.edu.au', '$2a$10$Ofq/EO5.r/il/nDLW8ajDuA9WRdR2mGGNQPl.h47h5XnHJdXplz4y', 'Yiyang',  'Huang', '1234567890'),
(3, 'test3@student.unimelb.edu.au', '$2a$10$xJ1uFvSo4HoDKpmjGfGzD.pO5p0fRr/ApAegBkaXazKv.YK8TGqw2', 'lingxiao', 'li', '188188'),
(4, 'test4@student.unimelb.edu.au', '$2a$10$xJ1uFvSo4HoDKpmjGfGzD.pO5p0fRr/ApAegBkaXazKv.YK8TGqw2', 'Yiyang',  'Huang', '1234567890'),
(5, 'test5@student.unimelb.edu.au', '$2a$10$xJ1uFvSo4HoDKpmjGfGzD.pO5p0fRr/ApAegBkaXazKv.YK8TGqw2', 'Yiyang',  'Huang', '1234567890'),
(6, 'test6@student.unimelb.edu.au', '$2a$10$xJ1uFvSo4HoDKpmjGfGzD.pO5p0fRr/ApAegBkaXazKv.YK8TGqw2', 'Yiyang',  'Huang', '1234567890');


-- -----------------------------------------------------
-- Organization
-- -----------------------------------------------------
INSERT INTO `organization` (id,name, owner) VALUES
( 1, 'University of Melbourne', 1),
( 2, 'MIT', 2);

-- -----------------------------------------------------
-- belong_to belong to organization
-- -----------------------------------------------------
INSERT INTO `belong_to` (id,user_id, organization_id) VALUES
( 1, 1, 1),
( 2, 2, 2),
( 3, 3, 1),
( 4, 4, 1),
( 5, 5, 1),
( 6, 6, 1);

-- -----------------------------------------------------
-- department
-- -----------------------------------------------------
INSERT INTO `department` (id,name, organization_id) VALUES
( 1, 'department of computer science', 1),
( 2, 'department of math', 1),
( 3, 'department of computer science', 2),
( 4, 'department of math', 2);

-- -----------------------------------------------------
-- permission
-- -----------------------------------------------------
INSERT INTO `permission` (id, user_id, department_id, authority_level) VALUES
( 1, 1, 1, 5),
( 2, 1, 2, 5),
( 3, 2, 3, 5),
( 4, 2, 4, 5),
( 5, 3, 1, 4),
( 6, 4, 1, 2),
( 7, 5, 1, 0),
( 8, 6, 1, 4),
( 9, 5, 2, 0);
