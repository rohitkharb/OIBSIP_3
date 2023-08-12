package ATM_Interface;

import java.util.*;
import java.sql.*;
import java.util.Date;

public class ATMInterface 
{
    float balance = 0;

    public void startATM()
    {
        System.out.println("");
        System.out.println("----------WELCOME TO ATM-----------");
        System.out.println("1. SIGN IN");
        System.out.println("2. SIGN UP");
        System.out.println("3. QUIT");
        System.out.println();
        int opt = 0;
        try {
            System.out.print("Enter Your Choice : ");
            opt = Integer.parseInt(System.console().readLine());
        } catch (Exception e) {
            System.out.println("----- Enter valid Choice -----");
            System.out.print("Enter Your Choice : ");
            opt = Integer.parseInt(System.console().readLine());
        }
        switch (opt) 
        {
            case 1: signIn();
                    break;
            case 2: signUp();
                    break;
            case 3: System.exit(0);
            default: System.out.print("Enter a valid choice");
        }
    }

    public void signIn() 
    {
        System.out.print("User ID: ");
        String userId = System.console().readLine();
        System.out.print("Password: ");
        int password = Integer.parseInt(System.console().readLine());

        conn c = new conn();
        try 
        {
            String query = "Select * from login where Password = '"+password+"'and User_Id = '"+userId+"'";
            ResultSet rs = c.s.executeQuery(query);

            if(rs.next())
            {
                System.out.println("Successfully Verified");
                showMenu(userId,password); 
            }
            else 
            {
                System.out.println("Enter a valid Pin!");
                signIn();
            }
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }
    }

    public void signUp()
    {
        conn c = new conn();
        Random random = new Random();
        long ran = Math.abs((random.nextLong() % 9000L) +1000L);

        System.out.println("----- ACCOUNT DETAILS -----");
        System.out.println("-----------"+ran+"-----------\n");
        System.out.print("Name: ");
        String name = System.console().readLine();
        System.out.print("Father's Name: ");
        String fname = System.console().readLine();
        System.out.println("Account Type: (Ex.Current,Saving)");
        String at = System.console().readLine();

        long userId = Math.abs(random.nextLong() % 90000000L) + 9350609001000000L;
        long password = Math.abs(random.nextLong() % 9000L) + 1000L;

        System.out.println("Account Successfully Created\nUserId: "+userId+"\nPassword: "+password);

        try 
        {
            String query = "insert into login values('"+ran+"','"+userId+"','"+password+"','"+name+"','"+fname+"','"+at+"')";
            c.s.executeUpdate(query);

            

        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("You have to deposit some amount");
        depositCash(""+userId, (int)password);
        showMenu(""+userId,(int)password);
    }

    public void showMenu(String userId,int password) 
    {
        while (true) 
        {
            System.out.println("");
            System.out.println("---------- ATM -----------");
            System.out.println("1. Balance Inquiry");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Transaction History");
            System.out.println("6. Quit");
            System.out.println();

            int opt = 0;
            try 
            {
                System.out.print("Enter Your Choice : ");
                opt = Integer.parseInt(System.console().readLine());
            } 
            catch (Exception e) 
            {
                System.out.println("----- Enter a valid choice -----");
                System.out.print("Enter Your Choice : ");
                opt = Integer.parseInt(System.console().readLine());
            }
            switch (opt) 
            {
                case 1: checkBalance(userId,password);
                        break;
                case 2: depositCash(userId,password);
                        break;
                case 3: withdrawCash(userId,password);
                        break;
                case 4: transfer(userId,password);
                        break;
                case 5: transactionHistory(userId,password);
                        break;
                case 6: System.exit(0);
                default: System.out.print("Enter a valid choice");
            }
            
        }
    }

    public void checkBalance(String userId,int password)
    {

        conn c= new conn();
        try 
        {
            String checkBalance1 = "Select * from balance where Password = '"+password+"'and User_Id = '"+userId+"'";
            ResultSet rs = c.s.executeQuery(checkBalance1);
            if(rs.next())
            {
                balance  = Float.parseFloat(rs.getString("Balance"));
            }
            else
            {
                balance = 0;
                String checkBalance2 = "insert into balance values('"+userId+"','"+password+"','"+balance+"')";
                c.s.executeUpdate(checkBalance2);
            }
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        
        }

        System.out.printf("Your Balance is Rs.%.2f", balance);
        System.out.println();
    }

    public void depositCash(String userId,int password) 
    {
        balance = 0;
        System.out.print("Enter deposit amount \nRs:");
        float amount = Float.parseFloat(System.console().readLine());
        balance = balance + amount;
        Date date = new Date();

        try 
        {
            conn c = new conn();
            String depositBank1 = "insert into bank values('"+password+"','"+date+"',' Deposit','"+balance+"')";
            c.s.executeUpdate(depositBank1);

            String depositBalance2 = "Select * from balance where Password = '"+password+"'and User_Id = '"+userId+"'";
            ResultSet rs = c.s.executeQuery(depositBalance2);
            if(rs.next())
            {
                balance  += Float.parseFloat(rs.getString("Balance"));
            }
            else
            {
                String withdrawQuery3 = "insert into balance values('"+userId+"','"+password+"','"+balance+"')";
                c.s.executeUpdate(withdrawQuery3);
            }
            
            String withdrawQuery2 = "update balance set Balance ='"+ balance +"'where Password ='"+password+"'";
            c.s.executeUpdate(withdrawQuery2);
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }

        System.out.printf("Amount Deposit Successfully! \nCurrent balance is Rs.%.2f", balance);
        System.out.println();
    }

    public void withdrawCash(String userId,int password) 
    {
        conn c = new conn();
        Date date = new Date();
        System.out.print("Enter the withdraw amount:\nRs.");
        float amount = Float.parseFloat(System.console().readLine());

        try 
        {
            String withdrawQuery1 = "Select * from balance where Password = '"+password+"'and User_Id = '"+userId+"'";
            ResultSet rs = c.s.executeQuery(withdrawQuery1);
            if(rs.next())
            {
                balance  = Float.parseFloat(rs.getString("Balance"));
            }
            else
            {
                System.out.println("----- NO BALANCE -----\nDeposit First");
                System.out.println();
                depositCash(userId, password);
            }

            if (amount > balance) 
            {
                System.out.println("Can't Withdraw! \nYour balance is insufficient...\nPlease Deposit First");
                showMenu(userId, password);
            } 
            else 
            {   
                balance = balance - amount;
            }

            String withdrawQuery2 = "update balance set Balance ='"+balance+"'where Password = '"+password+"'";
            c.s.executeUpdate(withdrawQuery2);

            String withdrawQuery4 = "insert into bank values('"+password+"','"+date+"','Withdraw','"+amount+"')";
            c.s.executeUpdate(withdrawQuery4);
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }

        System.out.printf("\nWithdraw was Success! your current balance is Rs.%.2f", balance);
        System.out.println();
    }

    public void transfer(String userId,int password)
    {
        
        System.out.print("Enter Receiver Id: ");
        String receiverId = System.console().readLine();
        Date date = new Date();

        if(receiverId == userId)
        {
            System.out.println("---- This is your Id ---- ");
            System.out.print("Please Enter Receiver Id: ");
            receiverId = System.console().readLine();
        }

        try 
        {
            conn c = new conn();
            String transferQuery1 = "select * from login where User_Id ='"+receiverId+"'";
            ResultSet rs = c.s.executeQuery(transferQuery1);
            if(rs.next())
            {
                System.out.println("Enter Amount you want to transfer");
                float transferBalance = Float.parseFloat(System.console().readLine());
                String transferQuery2 = "Select * from balance where Password = '"+password+"'and User_Id = '"+userId+"'";
                ResultSet rs2 = c.s.executeQuery(transferQuery2);
                if(rs2.next())
                {
                    balance  = Float.parseFloat(rs2.getString("Balance"));
                }
                
                if(balance<=1000)
                {
                    System.out.println("----- INSUFFICIENT BALANCE -----\nDeposit First");
                    System.out.println();
                    depositCash(userId, password);
                }

                if (transferBalance > balance) 
                {
                    System.out.println("Can't Transfer! \nYour balance is insufficient...\nPlease Deposit First");
                    showMenu(userId, password);
                } 
            
                balance = balance - transferBalance;

                String transferQuery3 = "update balance set Balance ='"+balance+"'where Password = '"+password+"'";
                c.s.executeUpdate(transferQuery3);

                String transferQuery4 = "Select * from balance where User_Id = '"+receiverId+"'";
                ResultSet rs3 = c.s.executeQuery(transferQuery4);
                if(rs3.next())
                {
                    float receiverBalance = Float.parseFloat(rs3.getString("Balance"));
                    receiverBalance += transferBalance;
                    String transferQuery5 = "update balance set Balance ='"+receiverBalance+"'where User_Id = '"+receiverId+"'";
                    c.s.executeUpdate(transferQuery5);
                }

                System.out.println("\nReceipt:");
                System.out.println("User Id\t\t  Date\t\t\t\tReceiverId\t  Amount");
                System.out.println(""+userId+"  "+date+"  "+receiverId+"  "+transferBalance);
            }
            else
            {
                System.out.println("----- Invalid UserId -----");
            }
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }
    }

    public void transactionHistory(String userId,int password)
    {
        conn c = new conn();
        System.out.println("----- Transaction History -----\n");

        try 
        {
            String transactionQuery1 = "Select * from balance where Password = '"+password+"'and User_Id = '"+userId+"'";
            ResultSet rs1 = c.s.executeQuery(transactionQuery1);
            if(rs1.next())
            {
                balance  = Float.parseFloat(rs1.getString("Balance"));
            }

            System.out.println("Current Balance: " + balance + "\n");

            String transactionQuery2 = "Select * from bank where Password = '"+password+"'";
            ResultSet rs2 = c.s.executeQuery(transactionQuery2);
            System.out.println("   Type\t    Date\t\t\t      Amount");
            while(rs2.next())
            {
                System.out.println(rs2.getString("Type") + "    " + rs2.getString("Date") + "      " + rs2.getString("Amount"));
            }
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }
    }


    public static void main(String[] args) 
    {
        new ATMInterface().startATM();
    }
}
