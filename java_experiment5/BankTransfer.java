package java_experiment5;
import java.sql.*;
import java.util.Scanner;

public class BankTransfer {
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);

    String url = "jdbc:mysql://localhost:3306/bankdb"; 
    String user = "root";
    String pass = "root";

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, pass); 
        con.setAutoCommit(false);

        Statement st = con.createStatement();

        System.out.print("Enter From Account Number: ");
        int fromAcc = sc.nextInt();

        System.out.print("Enter To Account Number: ");
        int toAcc = sc.nextInt();

        System.out.print("Enter Amount to Transfer: ");
        int amount = sc.nextInt();

        ResultSet rs = st.executeQuery("select balance from account where acc_no=" + fromAcc);

        if(!rs.next()) {
            System.out.println("Account not found.");
            return;
        }

        double balance = rs.getDouble("balance");

        if(balance < amount) {
            System.out.println("Insufficient Balance. Transaction Cancelled.");
            con.rollback();
            con.close();
            return;
        }

        st.executeUpdate("update account set balance = balance - " + amount + " where acc_no = " + fromAcc);
        System.out.println("\nAmount " + amount + " debited from Account No: " + fromAcc);

        // Credit
        st.executeUpdate("update account set balance = balance + " + amount + " where acc_no = " + toAcc);
        System.out.println("Amount " + amount + " credited to Account No: " + toAcc);

        con.commit();
        System.out.println("\nTransaction Successful\n");

        // Balance after transaction
        System.out.println("Balance After Transaction\n");

        ResultSet rs2 = st.executeQuery("select acc_no,name,balance from account");

        while(rs2.next()) {
            System.out.println("Account No: " + rs2.getInt("acc_no") +
                               " Name: " + rs2.getString("name") +
                               " Balance: " + rs2.getDouble("balance"));
        }

        con.close(); 
        System.out.println("\nConnection Closed");

    } catch(Exception e) { 
        System.out.println("Transaction Failed");
    }
}


}




