import java.util.Scanner;
public class stack {
    Node top,head;
    static class Node{
        int data;
        Node next;
        Node(int d){
            data = d;
            next = null;
        }
    }
    public void push(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the data: ");
        int item = sc.nextInt();
        Node node = new Node(item);
        node.next = head;
        head = node;
        System.out.println("\nItem pushed successfully....");
        top = head;
    }
    public void pop(){
        if (top == null){
            System.out.println("Stack Underflow....");
            return;
        }
        Node tmp = head;
        int n = tmp.data;
        head = head.next;
        System.out.print("\nItem popped successfully\nPopped element: " + n);
    }
    public void traverse(){
        if (head == null){
            System.out.println("Stack Underflow....");
            return;
        }
        Node tmp = head;
        int i = 1;
        while (tmp != null){
            System.out.println("\nData " + i + "\nNumber: " + tmp.data);
            i++;
            tmp = tmp.next;
        }
    }
    public void search(int data){
        if (head == null){
            System.out.println("\nStack Underflow....");
            return;
        }
        Node tmp = head;
        while(tmp != null){
            if (tmp.data == data){
                System.out.println("Element founded successfully in the stack");
                return;
            }
            tmp = tmp.next;
        }
        System.out.println("Element not found in the list");
    }
    public void peek(){
        Node tmp = top;
        if (top == null){
            System.out.println("\nNo elements in the Stack");
            return;
        }
        System.out.println("\nSuccessfully Peeked\nPeeked Element: " + tmp.data);
    }
    public static void main(String[] args){
        stack st = new stack();
        int ch = 0,number;
        System.out.print("Press 1 -> To Push\nPress 2 -> to Pop\nPress 3 -> to Peek\nPress 4 -> to Traverse\nPress 5 -> to search\nPress 6 -> to Exit\n");
        try (Scanner sc = new Scanner(System.in)) {
            while (ch != 6){
                System.out.print("\nEnter the choice: ");
                ch = sc.nextInt();
                switch (ch){
                    case 1:
                        st.push();
                        break;
                    case 2:
                        st.pop();
                        break;
                    case 3:
                        st.peek();
                        break;
                    case 4:
                        st.traverse();
                        break;
                    case 5:
                        System.out.print("Enter the number to search: ");
                        number = sc.nextInt();
                        st.search(number);
                        break;
                    case 6:
                        System.out.print("\nExit Successfully\n");
                        break;
                    default:
                        System.out.println("Invalid Choice");
                }
            }
        }
    }
}