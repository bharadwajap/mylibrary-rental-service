DROP TABLE IF EXISTS rentals;
 
CREATE TABLE rentals (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT(50) NOT NULL,
  book_id VARCHAR(20) NOT NULL,
  issue_time TIMESTAMP NOT NULL,
  return_time TIMESTAMP,
  return_duration INT NOT NULL,
  late_fee DOUBLE
);
 
INSERT INTO rentals (user_id, book_id, issue_time, return_time, return_duration, ) VALUES
  ('1','27534857', 'Driving License', 9876543210),
  ('Irfan Khan','AKIJD847UJ', 'PAN', 9876543211),
  ('Vishal Kumar','8774983984985895', 'Aadhar', 9876543212);