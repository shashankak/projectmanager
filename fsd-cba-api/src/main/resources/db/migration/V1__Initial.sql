CREATE TABLE ParentTask (
  Parent_ID INT AUTO_INCREMENT PRIMARY KEY,
  Parent_Task VARCHAR(50) NOT NULL
);

CREATE TABLE Project (
  Project_ID INT AUTO_INCREMENT PRIMARY KEY,
  Project VARCHAR(50) NOT NULL,
  Start_Date DATETIME NOT NULL,
  End_Date DATETIME NULL,
  Priority INT NOT NULL
);

CREATE TABLE Task (
  Task_ID INT AUTO_INCREMENT PRIMARY KEY,
  Parent_ID INT NULL,
  Project_ID INT NOT NULL,
  Task VARCHAR(50) NOT NULL,
  Start_Date DATETIME NOT NULL,
  End_Date DATETIME NULL,
  Priority INT NOT NULL,
  Status VARCHAR(20),
  FOREIGN KEY (Parent_ID) REFERENCES Task (Task_ID),
  FOREIGN KEY (Project_ID) REFERENCES Project (Project_ID)
);


CREATE TABLE User (
  User_ID INT AUTO_INCREMENT PRIMARY KEY,
  First_Name VARCHAR(50) NOT NULL ,
  Last_Name VARCHAR(50),
  Employee_ID VARCHAR(50),
  Project_ID INT NULL,
  Task_ID INT NULL,
  FOREIGN KEY (Project_ID) REFERENCES Project (Project_ID),
  FOREIGN KEY (Task_ID) REFERENCES Task (Task_ID)
);

