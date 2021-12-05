import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.time.*;

public class Client {
    public static void main(String args[]) throws RemoteException {
        try {
            Scanner sc = new Scanner(System.in);
            // Registry reg = LocateRegistry.getRegistry("localhost", 8000);
            // checkBal obj_bal = (checkBal) reg.lookup("bankServer");
            Registry reg = LocateRegistry.getRegistry("localhost", 8081);
            loadBalancerInterface lb = (loadBalancerInterface) reg.lookup("loadBalancer");
            System.out.println("Connected to server " + lb.getServerName());
            checkBal obj_bal = lb.getServer();
            Clock client_time;
            Registry reg_time;
            getTime obj;
            long start;
            long serverTime;
            long end;
            long rtt;
            long updatedTime;
            int want_to_add_user;
            // 2 for normal things
            // 1 for adding a user

            System.out.print("1. Add user\n2. Proceed with normal stuffs\n Choice: ");
            want_to_add_user = sc.nextInt();
            sc.nextLine();

            if (want_to_add_user == 1) {
                String acc_no, password;
                double amount;
                System.out.print("Enter the account number: ");
                acc_no = sc.nextLine();
                System.out.print("Enter the password: ");
                password = sc.nextLine();
                System.out.print("Enter the amount: ");
                amount = sc.nextDouble();
                obj_bal.AddUser(acc_no, password, amount);
                System.out.println("User Added Successfully");
                return;
            }
            System.out.print("\nEnter account number:");
            String acc_no = sc.nextLine();
            System.out.print("Enter password:");
            String password = sc.nextLine();
            System.out.println("Choices:\n1.Check Balance\n2.Transfer Money\n3.Add Money\n4.Remove Money\nEnter choice:");
            int ch = sc.nextInt();
            switch (ch) {
                case 1:
                    client_time = Clock.systemUTC();
                    reg_time = LocateRegistry.getRegistry("localhost", 8080);
                    obj = (getTime) reg_time.lookup("timeServer");
                    start = Instant.now().toEpochMilli();
                    serverTime = obj.getSystemTime();
                    System.out.println("Server time " + serverTime);
                    end = Instant.now().toEpochMilli();
                    rtt = (end - start) / 2;
                    System.out.println("Round Trip Time " + rtt);
                    updatedTime = serverTime + rtt;
                    client_time = Clock.offset(client_time,
                            Duration.ofMillis(updatedTime - client_time.instant().toEpochMilli()));
                    System.out.println("New Client time " + client_time.instant().toEpochMilli());
                    double bal = obj_bal.checkBalance(acc_no, password);
                    if (bal == -1) {
                        System.out.println("\nInvalid credentials");
                        return;
                    } else {
                        System.out.println("\nBalance: Rs." + bal + "\n");
                    }
                    break;

                case 2:
                    sc.nextLine();
                    System.out.print("Enter account number to credit:");
                    String cred_acc_no = sc.nextLine();

                    System.out.print("Enter amount to transfer:");
                    double amt = sc.nextDouble();

                    client_time = Clock.systemUTC();
                    reg_time = LocateRegistry.getRegistry("localhost", 8080);
                    obj = (getTime) reg_time.lookup("timeServer");
                    start = Instant.now().toEpochMilli();
                    serverTime = obj.getSystemTime();
                    System.out.println("Server time " + serverTime);
                    end = Instant.now().toEpochMilli();
                    rtt = (end - start) / 2;
                    System.out.println("Round Trip Time " + rtt);
                    updatedTime = serverTime + rtt;
                    client_time = Clock.offset(client_time,
                            Duration.ofMillis(updatedTime - client_time.instant().toEpochMilli()));
                    System.out.println("New Client time " + client_time.instant().toEpochMilli());
                    boolean status = obj_bal.transfer(acc_no, cred_acc_no, password, amt);
                    if (status) {
                        System.out.println("\nTransfer Successful");
                        System.out.println("New Balance:" + obj_bal.checkBalance(acc_no, password));
                    } else {
                        System.out.println("\nError occured");
                    }
                    break;

                case 3:
                    sc.nextLine();

                    System.out.print("Enter amount to Add:");
                    double amt_add = sc.nextDouble();

                    client_time = Clock.systemUTC();
                    reg_time = LocateRegistry.getRegistry("localhost", 8080);
                    obj = (getTime) reg_time.lookup("timeServer");
                    start = Instant.now().toEpochMilli();
                    serverTime = obj.getSystemTime();
                    System.out.println("Server time " + serverTime);
                    end = Instant.now().toEpochMilli();
                    rtt = (end - start) / 2;
                    System.out.println("Round Trip Time " + rtt);
                    updatedTime = serverTime + rtt;
                    client_time = Clock.offset(client_time,
                            Duration.ofMillis(updatedTime - client_time.instant().toEpochMilli()));
                    System.out.println("New Client time " + client_time.instant().toEpochMilli());
                    boolean status_add = obj_bal.AddBalance(acc_no, password, amt_add);
                    if (status_add) {
                        System.out.println("\nAdded Successfully");
                        System.out.println("New Balance:" + obj_bal.checkBalance(acc_no, password));
                    } else {
                        System.out.println("\nError occured");
                    }
                    break;
                case 4:
                    sc.nextLine();

                    System.out.print("Enter amount to Deduct:");
                    double amt_deduce = sc.nextDouble();

                    client_time = Clock.systemUTC();
                    reg_time = LocateRegistry.getRegistry("localhost", 8080);
                    obj = (getTime) reg_time.lookup("timeServer");
                    start = Instant.now().toEpochMilli();
                    serverTime = obj.getSystemTime();
                    System.out.println("Server time " + serverTime);
                    end = Instant.now().toEpochMilli();
                    rtt = (end - start) / 2;
                    System.out.println("Round Trip Time " + rtt);
                    updatedTime = serverTime + rtt;
                    client_time = Clock.offset(client_time,
                            Duration.ofMillis(updatedTime - client_time.instant().toEpochMilli()));
                    System.out.println("New Client time " + client_time.instant().toEpochMilli());
                    boolean status_remove = obj_bal.RemoveBalance(acc_no, password, amt_deduce);
                    if (status_remove) {
                        System.out.println("\nRemoved Successfully");
                        System.out.println("New Balance:" + obj_bal.checkBalance(acc_no, password));
                    } else {
                        System.out.println("\nError occured");
                    }
                    break;
                default:
                    System.out.println("Wrong choice entered");
            }
            return;

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}