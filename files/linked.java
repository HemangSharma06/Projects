import java.util.Scanner;
class linked {
    Node head;
    class Node{
        int data;
        Node next;
        Node(int d){
            data = d;
            next = null;
        }
    }
    public void insert(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the data: ");
        int item = sc.nextInt();
        Node node = new Node(item);
        node.next = head;
        head = node;
        System.out.println("\nNode inserted at front of linked list....");
    }
    public void delete(){
        if (head == null){
            System.out.println("Empty list");
            return;
        }
        Node tmp = head;
        head = head.next;
        tmp = null;
    }
    public void display(){
        if (head == null){
            System.out.println("Empty list");
            return;
        }
        Node tmp = head;
        int i = 1;
        while (tmp != null){
            System.out.print("Data " + i + "\nNumber: " + tmp.data + "\n");
            i++;
            tmp = tmp.next;
        }
    }
    public void search(int data){
        if (head == null){
            System.out.println("Empty list");
            return;
        }
        Node tmp = head;
        while(tmp != null){
            if (tmp.data == data){
                System.out.println("Element founded successfully in the list");
                return;
            }
            tmp = tmp.next;
        }
        System.out.println("Element not found in the list");
    }
    public static void main(String[] args){
        linked list = new linked();
        int ch = 0,number;
        System.out.print("Press 1 -> To insert\nPress 2 -> to delete\nPress 3 -> to display\nPress 4 -> to search\nPress 5 -> to exit\n");
        Scanner sc = new Scanner(System.in);
        while (ch != 5){
            System.out.print("Enter the choice: ");
            ch = sc.nextInt();
            switch (ch){
                case 1:
                    list.insert();
                    break;
                case 2:
                    list.delete();
                    break;
                case 3:
                    list.display();
                    break;
                case 4:
                    System.out.print("Enter the number to search: ");
                    number = sc.nextInt();
                    list.search(number);
                    break;
                case 5:
                    System.out.print("\nExit Successfully\n");
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
}