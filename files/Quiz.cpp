#include <iostream>
#include <fstream>
#include <ctime>
#include <algorithm>

using namespace std;

int Ccount = 0, Wcount = 0;

class puzzles
{
    int min = 11, max = 310, count1 = 0, random1 = std::rand() % (max - min + 1) + min - 1, random2;

public:
    void readfile();
};

void puzzles::readfile()
{
    std::ifstream infile("Quiz.txt");
    std::string line, ch, ans = "The Answer is ";

    random2 = random1;

    int r = random1 % 10;
    random1 -= r;
    r = 10 - random2 % 10;
    random2 += r;

    if (infile.is_open())
    {

        while (getline(infile, line) && (count1 < random1 - 1))
        {
            count1++;
        }
        while (getline(infile, line) && count1 < random2 - 3)
        {
            std::cout << line << endl;
            count1++;

            if ((count1 + 4) == (random2))
            {
                std::cout << "Type A/B/C/D to choose the option: ";
                std::cin >> ch;
                transform(ch.begin(), ch.end(), ch.begin(), ::toupper);
                ch += ".";
                ans += ch;
                while (count1 < random2 - 1 && getline(infile, line))
                {
                    if (line == ans)
                    {
                        std::cout << "\nCorrect!...\n"
                                  << endl;
                        Ccount++;
                        count1++;
                    }
                    else if (line == "")
                    {
                        std::cout << line << endl;
                        count1++;
                    }
                    else
                    {
                        std::cout << line << endl;
                        Wcount++;
                        count1++;
                    }
                }
            }
        }
        std::cout << "\n";
        infile.close();
    }
    else
    {
        std::cerr << "Error to open...." << endl;
    }
}

int main()
{
    std::string s1, s2;
    std::srand(std::time(0));

    std::cout << "\nThis Quiz Generator contains 30 Questions though which one of the Question randomly Generates and displayed." << endl
              << "Type 'Start' To start the quiz " << endl;
    std::cin >> s1;
    transform(s1.begin(), s1.end(), s1.begin(), ::toupper);
    if (s1 != "START")
    {
        std::cout << "\nNot started.\n";
        return 1;
    }
    std::cout << "\n";
    do
    {
        puzzles p;
        p.readfile();
        std::cout << "\n\nEnter yes for next question: ";
        std::cin >> s2;
        transform(s2.begin(), s2.end(), s2.begin(), ::toupper);
        std::cout << "\n\n";
    } while (s2 == "YES");

    int Tcount = Ccount + Wcount;
    std::cout << "\nYou did " << Tcount << " Questions. " << endl
              << Ccount << " -> correct." << endl
              << Wcount << " -> wrong." << endl;
    return 0;
}
