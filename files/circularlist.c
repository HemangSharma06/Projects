#include <stdio.h>
#include <stdlib.h>

struct node
{
    int data;
    struct node *next;
} *head = NULL;
void insert()
{
    int data;
    struct node *ptr = (struct node *)malloc(sizeof(struct node)),*tmp;
    if (!ptr){
        printf("\nOverflow....\n");
        return;
    }
    printf("\nEnter the data: ");
    scanf("%d",&data);
    ptr->data = data;
    if (head == NULL){
        head = ptr;
    }
    else{
        tmp = head;
        while (tmp->next != head)tmp = tmp->next;
        tmp->next = ptr;
    }
    ptr->next = head;
    printf("\nData inserted successfully...\n");
}
void deleted(){

}
void reverse(){
    struct node *prev = NULL,*cur = head, *next = NULL;
    while (cur->next != head) {
        next = cur->next;
        cur->next = prev;
        prev = cur;
        cur = next;
    }
    printf("\nreversed Successfully....\n");
    next = cur->next;
    cur->next = prev;
    prev = cur;
    cur = next;
    head = prev;
}
void display(){
    if (head == NULL) return;
    struct node *tmp = head;
    printf("\nList: ");
    while (tmp->next != head){
        printf(" %d -> ", tmp->data);
        tmp = tmp->next;
    }
    printf(" %d -> %d(1st node data)\n", tmp->data);
}
int main()
{
    int x;
    printf("To insert student details press 1\nTo delete student details Press 2\nTo display the student details list Press 3\nPress 4 to reverse the linked list\nPress 5 to exit");
    while (x != 5)
    {
        printf("\nEnter the choice: ");
        scanf("%d", &x);
        switch (x)
        {
        case 1:
            insert();
            break;

        case 2:
            printf("Enter the roll number to delete: ");
            break;

        case 3:
            display();
            break;
        case 4:
            reverse();
            display();
            break;
        case 5:
            printf("\nSuccessfully Exit...\n");
            break;
        default:
            printf("Invalid Choice....\n\n");
        }
    }
}
