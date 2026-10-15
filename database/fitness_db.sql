
CREATE DATABASE IF NOT EXISTS fitness_db;
USE fitness_db;

CREATE TABLE IF NOT EXISTS WorkoutPlans (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    duration VARCHAR(50)
);

INSERT INTO WorkoutPlans (title, description, duration)
SELECT 'Weight Loss', 'Cardio and fat-burning exercises', '4 weeks'
WHERE NOT EXISTS (
    SELECT 1 FROM WorkoutPlans WHERE title = 'Weight Loss'
);

INSERT INTO WorkoutPlans (title, description, duration)
SELECT 'Muscle Building', 'Strength training and muscle growth', '6 weeks'
WHERE NOT EXISTS (
    SELECT 1 FROM WorkoutPlans WHERE title = 'Muscle Building'
);

INSERT INTO WorkoutPlans (title, description, duration)
SELECT 'General Fitness', 'Full-body fitness routine', '4 weeks'
WHERE NOT EXISTS (
    SELECT 1 FROM WorkoutPlans WHERE title = 'General Fitness'
);
