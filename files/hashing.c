#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define TABLE_SIZE 20

typedef struct Node
{
    int key;
    struct Node *next;
} Node;
Node *hashTable[TABLE_SIZE];

int hashFunction(int key)
{
    return key % TABLE_SIZE;
}

void insert(int key)
{
    int hashIndex = hashFunction(key);
    Node *newNode = (Node *)malloc(sizeof(Node));
    newNode->key = key;
    newNode->next = NULL;

    if (hashTable[hashIndex] == NULL)
    {
        hashTable[hashIndex] = newNode;
    }
    else
    {
        Node *temp = hashTable[hashIndex];
        while (temp->next != NULL)
        {
            temp = temp->next;
        }
        temp->next = newNode;
    }
}
int search(int key)
{
    int hashIndex = hashFunction(key);
    Node *temp = hashTable[hashIndex];

    while (temp != NULL)
    {
        if (temp->key == key)
        {
            return 1;
        }
        temp = temp->next;
    }
    return 0;
}
void display()
{
    for (int i = 0; i < TABLE_SIZE; i++)
    {
        printf("hashTable[%d]: ", i);
        Node *temp = hashTable[i];
        while (temp != NULL)
        {
            printf("%d -> ", temp->key);
            temp = temp->next;
        }
        printf("NULL\n");
    }
}

int main()
{
    printf("Size of Hash Table is %d.\n", TABLE_SIZE);
    for (int i = 0; i < TABLE_SIZE; i++)
    {
        hashTable[i] = NULL;
    }
    int ch, num;
    printf("1 -> To Insert\n2 -> To Search\n3 -> To display\n4 -> To exit");
    do
    {
        printf("\nEnter choice: ");
        scanf("%d", &ch);
        switch (ch)
        {
        case 1:
            printf("\nEnter number to add into hash table: ");
            scanf("%d", &num);
            insert(num);
            break;
        case 2:
            printf("\nEnter number to search: ");
            scanf("%d", &num);
            int find = search(num);
            if (find == 0)
                printf("\nNumber not found....\n");
            else
                printf("\nSuccessfully found....\n");
            break;
        case 3:
            display();
            break;
        case 4:
            printf("\nSuccessfully Exit....\n");
            break;
        default:
            printf("\nInvalid Choice....\n");
        }
    } while (ch != 4);

    return 0;
}
