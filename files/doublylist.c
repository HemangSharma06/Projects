#include <stdio.h>
#include <stdlib.h>

int count = 0;
struct student
{
    int roll;
    char name[40];
    float perct;
    char gender[10];
    char phone[20];
    struct student *next,*prev;

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
        if (head == NULL){
            std->prev = NULL;
            head = std;
        }    
        printf("Enter the Roll No. : ");
        scanf("%d", &std->roll);
        printf("Enter the Name : ");
        scanf("%s", &std->name);
        printf("Enter the Gender : ");
        scanf("%s", &std->gender);
        printf("Enter Mobile Number : ");
        scanf("%s", &std->phone);
        printf("Enter percentage : ");
        scanf("%f", &std->perct);

        std->next = head;
        head = std;
        std->prev = std;
        printf("Details Inserted Successfully....\n\n\n");
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
            printf("Record deleted successfully.....\n\n\n");
            count--;
            return;
        }
        tmp2 = tmp1;
        tmp1 = tmp1->next;
    }
    printf("Record with roll number not found.....\n\n\n");
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
            printf("\nName : %s", tmp->name);
            printf("\nGender : %s", tmp->gender);
            printf("\nMobile : %s", tmp->phone);
            printf("\nPercentage : %f\n\n\n", tmp->perct);
            i++;
            count1++;
            tmp = tmp->next;
        }
    }
}
void reverse(){
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
    int x, rl, ch = 1;
    while (ch == 1)
    {
        printf("To insert student details press 1\nTo delete student details Press 2\nTo display the student details list Press 3\nPress 4 to reverse the linked list\nEnter the choice: ");
        scanf("%d", &x);
        switch (x)
        {
        case 1:
            insert();
            break;

        case 2:
            printf("Enter the roll number to delete: ");
            scanf("%d", &rl);
            delete (rl);
            break;

        case 3:
            display();
            break;
        case 4:
            reverse();
            display();
            break;
        default:
            printf("Invalid Choice....\n\n");
        }
        printf("Do you want to perform any operations?If yes, Press 1\nEnter the choice: ");
        scanf("%d", &ch);
    }
    return 0;
}