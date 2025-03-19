#include <stdio.h>
#include <stdlib.h>

int count = 0,del = 0;
struct student
{
    int roll;
    char name[40];
    struct student *next;

} *head;

void insert()
{
    printf("\n\n");
    struct student *std, *tmp;
    int loc, i;
    std = (struct student *)malloc(sizeof(struct student));
    if (std == NULL)
    {
        printf("Overflow....\n");
        return;
    }
    else
    {
        if (head == NULL)
            head = std;
        printf("Enter the Roll No. : ");
        scanf("%d", &std->roll);
        printf("Enter the Name : ");
        scanf("%s", &std->name);

        std->next = head;
        head = std;
        printf("Details Inserted Successfully....\n\n");
        count++;
    }
    return;
}

void delete(int rollno)
{
    struct student *tmp1, *tmp2;
    tmp1 = head;
    tmp2 = head;
    while (tmp1 != NULL)
    {
        if (tmp1->roll == rollno)
        {
            if (tmp1 == NULL)
            {
                printf("Underflow....\n");
                return;
            }
            else if (tmp1 == tmp2)
            {
                head = head->next;
                free(tmp1);
            }
            else
            {
                tmp2->next = tmp1->next;
                free(tmp1);
            }
            printf("Record deleted successfully.....\n\n");
            count--;
            del++;
            return;
        }
        tmp2 = tmp1;
        tmp1 = tmp1->next;
    }
    printf("Record with roll number not found.....\n\n");
}

void display()
{

    int var = count, i = 1;
    struct student *tmp;
    tmp = head;
    if (tmp == NULL)
    {
        printf("No such details....\n\n");
        return;
    }
    else
    {
        int count1 = 0;
        while (count1 != count)
        {
            printf("\nStudent %d", i);
            printf("\nRoll number: %d", tmp->roll);
            printf("\nName : %s\n", tmp->name);
            i++;
            count1++;
            tmp = tmp->next;
        }
    }
}

void reverse()
{

    struct student *prevNode, *curNode;

    if (head != NULL)
    {
        prevNode = head;
        curNode = head->next;
        head = head->next;

        prevNode->next = NULL;

        while (head != NULL)
        {
            head = head->next;
            curNode->next = prevNode;

            prevNode = curNode;
            curNode = head;
        }

        head = prevNode;

        printf("successfully reversed list\n");
    }
}
int main()
{
    int x, rl, ch;
    printf("To insert student details press 1\n");
    printf("To delete student details Press 2\n");
    printf("To display the student details list Press 3\n");
    printf("To reverse the linked list Press 4\n");
    printf("To exit operations Press 5\nEnter the choice:");
    scanf("%d", &ch);
    do
    {
        switch (ch)
        {
        case 1:
            insert();
            break;

        case 2:
            printf("Enter the roll number to delete: ");
            scanf("%d", &rl);
            delete (rl);
            printf("\nStudents records after deletion\n\n");
            display();
            break;

        case 3:
            printf("\nDetails of all the students.....\n\n");
            display();
            break;
        case 4:
            reverse();
            printf("\nStudent records after reversal of list...\n");
            display();
            break;
        case 5:
            break;
            break;
        default:
            printf("\nInvalid choice....\n\n");
        }
        printf("\nEnter the choice: ");
        scanf("%d",&ch);
    }while (ch != 5);
    return 0;
}
