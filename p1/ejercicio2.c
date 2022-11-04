#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>

#define N 3
#define DELAY 1

int main()
{
    int i;
    int n = N;

    int x;
    int total = 0;

    for (i = 0; i < n; i++)
    {
        if (!fork())
        {
            i = -1;
            n = n - 1;
        }
    }

    for (i = 0; i < n; i++)
    {
        wait(&x);
        total += WEXITSTATUS(x);
    }

    if (n == 0)
        sleep(DELAY);

    if (n < N)
        exit(total + 1);
    else
        printf("total = %d\n", total);
}