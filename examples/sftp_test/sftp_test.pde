import sftp.*;

Sftp sftp;

void setup() {
  size(200,200);
  background(0);

  // Create the SFTP object
  // if 3rd argument is false, you must set the password in your code
  // if 3rd argument is true, you will be prompted to enter your password
  sftp = new Sftp("www.hostname.com","login", true);
  // sftp.setPassword("XXXXXX");
  sftp.start(); // start the thread

  noLoop();
}

void draw() {

}

void mousePressed() {
  // At any point you can execute an SFTP command 
  // Not all commands are currently implemented, but you do have "ls" and "get"
  // Gosh, I should implement "put", sorry!
  sftp.executeCommand("ls");
  sftp.executeCommand("get file.txt");
}
