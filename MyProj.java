import java.sql.*;  //Import the java SQL library

class program     //Create a new class to encapsulate the program
{
 
 public static void SQLError (Exception e)   //Our function for handling SQL errors
 {
	System.out.println("ORACLE error detected:");
	e.printStackTrace();	
 }

public static void main (String args[])  //The main function

{

 try {                                        //Keep an eye open for errors
       
       String driverName = "oracle.jdbc.driver.OracleDriver";
       Class.forName(driverName);

       System.out.println("Connecting   to Oracle...");  

       String url = "jdbc:oracle:thin:@apollo.ite.gmu.edu:1521:ite10g";
       Connection conn = DriverManager.getConnection(url,"username","password");

       System.out.println("Connected!");
       
       Statement stmt = conn.createStatement();   //Create a new statement
       stmt.executeQuery("alter table FACULTY rename column UNO to FUNO");       
       
       //Now we execute our query and store the results in the myresults object:
       ResultSet myresults = stmt.executeQuery("SELECT * FROM STUDENT");
 
       //System.out.println("Employee_Name\tSalary");
       //System.out.println("-------------\t------"); //Print a header

       while (myresults.next()) //pass to the next row and loop until the last 
        {
    	  System.out.println("Date mailed: 5/1/2018");
    	  System.out.println("Semester: Spring 2018");
    	  System.out.println("Student name:" + myresults.getString("SNAME")); //Print the current row
          System.out.println("Student identification number:" + myresults.getString("UNO"));
          System.out.println("Student major:" + myresults.getString("MAJOR"));
          System.out.println("Student status:" + myresults.getString("STATUS"));
          
          String temp = myresults.getString("UNO");
          Statement substmt = conn.createStatement();
          ResultSet submyresults = substmt.executeQuery("SELECT * FROM ENROLL natural join SECTION natural join COURSE natural join MEETING join FACULTY on FUNO = INSTRUCTOR where UNO = " + temp);
          String course = "";
          int total = 0;
          int amount = 0;
          while (submyresults.next()) {
        	  if (!course.equals(submyresults.getString("DCODE") + " " + submyresults.getString("CNO"))){
        		  System.out.println("\n\n" + "	" + submyresults.getString("DCODE") + " " + submyresults.getString("CNO"));
        		  System.out.println("	" + submyresults.getString("TITLE"));
        		  System.out.println("	" + "Credits: " + submyresults.getString("CREDITS"));
        		  System.out.println("	" + "Section No: " + submyresults.getString("SNO"));
        		  System.out.println("	" + submyresults.getString("FNAME"));
        		  System.out.print("	" + submyresults.getString("BLDG") + " " + submyresults.getString("ROOM") + ", " + submyresults.getString("DAY") + " " + submyresults.getString("BEGIN") + "-" + submyresults.getString("END"));
        		  total += Integer.parseInt(submyresults.getString("CREDITS"));
        	  }
        	  else if (!course.equals("")){
        		  System.out.println(", " + submyresults.getString("DAY") + " " + submyresults.getString("BEGIN") + "-" + submyresults.getString("END") + "\n");
        	  }
        	  course = submyresults.getString("DCODE") + " " + submyresults.getString("CNO");
          }
          if (myresults.getString("STATUS").equals("undergraduate")) {
        	 if (total <= 11) {
        		 amount = total*448;
        	 }
        	 else {
        		 amount = 5376;
        	 }
          }
          else if (myresults.getString("STATUS").equals("graduate")) {
         	 if (total <= 8) {
        		 amount = total*646;
        	 }
        	 else {
        		 amount = 5814;
        	 }       	  
          }
          System.out.println("\n\nTotal number of credits: " + total);
          if (total == 0) {
        	  System.out.println("Not enrolled this semester.");
          }
          System.out.println("Amount due: $" + amount + ".00\n");
          
	}
  
        conn.close();  // Close our connection.

      }
       catch (Exception e) {SQLError(e);} //if any error occurred in the try..catch block, call the SQLError function

}
}