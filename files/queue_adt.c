#include <stdio.h>
#include <stdlib.h>
int count = 0;

struct node
{
    int data;
    struct node *next;
} *rear = NULL, *front = NULL;

void enqueue()
{
    struct node *ptr;
    ptr = (struct node *)malloc(sizeof(struct node));
    printf("Enter the data or number: ");
    scanf("%d", &ptr->data);
    if (!ptr)
    {
        printf("\nQueue Overflow....\n");
        return;
    }
    else if (front == NULL)
    {
        front = ptr;
        rear = ptr;
    }
    else
    {
        rear->next = ptr;
        rear = rear->next;
    }
    printf("\nEnqueue data successfully....\n");
    count++;
    rear->next = NULL;
}

void dequeue()
{
    if (front == NULL)
    {
        printf("\nQueue Underflow...\n");
        return;
    }
    struct node *tmp = front;
    front = front->next;
    printf("\nNumber %d dequeued successfully...\n", tmp->data);
    free(tmp);
    count--;
}

void traverse()
{
    struct node *tmp = front;
    int i = 1;
    if (front == NULL)
    {
        printf("\nNo elements in the queue...\n");
        return;
    }
    else if (count == 1)
    {
        printf("HI\n");
        printf("Data %d\nNumber %d = %d    <-    FRONT & REAR END.\n\n", i, i, tmp->data);
        return;
    }
    printf("Data %d\nNumber %d = %d    <-    FRONT END.\n\n", i, i, tmp->data);
    tmp = tmp->next;
    i++;
    while (tmp->next != rear->next)
    {
        printf("Data %d\nNumber %d = %d\n\n", i, i, tmp->data);
        i++;
        tmp = tmp->next;
    }
    printf("Data %d\nNumber %d = %d    <-    REAR END.\n\n", i, i, tmp->data);
}

void search()
{
    struct node *tmp = front;
    int item, i = 0;
    printf("\nEnter the element or data to search: ");
    scanf("%d", &item);
    if (front == NULL)
    {
        printf("\nNo elements in the Queue....\n");
        return;
    }
    while (tmp->next != NULL)
    {
        if (tmp->data == item)
        {
            printf("\nSuccessfully found data\n");
            i++;
        }
        tmp = tmp->next;
    }
    if (i != 0)
        return;
    else if (tmp->data == item)
        printf("\nSuccessfully found data\n");
    else
        printf("\nNo searched data found\n");
}

void main()
{
    int ch;
    printf("\nPress 1 => To Enqueue\nPress 2 => To Dequeue\nPress 3 => To traverse\nPress 4 => To search an Element or Data\nPress 5 => To exit\n");
    do
    {
        printf("\nEnter choice: ");
        scanf("%d", &ch);
        switch (ch)
        {
        case 1:
            enqueue();
            break;
        case 2:
            dequeue();
            break;
        case 3:
            traverse();
            break;
        case 4:
            search();
            break;
        case 5:
            printf("\nSuccessfully exit....\n");
            break;
        default:
            printf("\nInvalid choice....\n");
        }
    } while (ch != 5);
}