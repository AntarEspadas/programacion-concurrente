#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>

#define N 7
#define DELAY 5

int main()
{
    int i;
    int j;
    int total = 1;
    int x;

    for (i = 0; i < N / 2; i++)
    {
        if (!fork())
            continue;
        if (fork())
            break;
        for (j = i * 2; j < N - 2; j++)
        {
            if (fork())
            {
                wait(&x);
                exit(WEXITSTATUS(x) + 1);
            }
        }
        sleep(DELAY);
        exit(1);
    }

    if (i < N / 2)
    {
        wait(&x);
        total += WEXITSTATUS(x);
        wait(&x);
        total += WEXITSTATUS(x);
    }
    else
    {
        sleep(DELAY);
    }

    if (i > 0)
    {
        exit(total);
    }
    else
    {
        printf("total = %d\n", total);
    }
}