#include <iostream>
#include <string>
#include <stdlib.h>
#include <vector>

using namespace std;

int CalculatePooling(const vector<int>& vIsland, int nStart, int nEnd);
bool IsNumber(char c);
bool ParseNumber(string& sLineRemainder, int& nOut);
bool ProcessLine(const string& sLine, vector<int>& vIsland);
void TrimLine(string& sLine);

int main(int argc, char* argv[])
{
	string sInput;
	bool bProcessInput = true;
	bool bFirstLine = true;
	while (bProcessInput)
	{
		getline(cin, sInput);
		TrimLine(sInput);

		// Check our break out case
		if (sInput == "0")
		{
			bProcessInput = false;
		}
		else
		{
			if (bFirstLine)
				bFirstLine = false;
			else
				cout << "\n";

			vector<int> vIsland;

			if (!ProcessLine(sInput, vIsland))
			{
				cout << "Invalid input";
			}
			else
			{
				cout << CalculatePooling(vIsland, 0, vIsland.size() - 1);
			}
		}

	}

	return 0;
}

int CalculatePooling(const vector<int>& vIsland, int nStart, int nEnd)
{
	int nWater = 0;

	// High point is the lowest of the peaks (high point of water)
	int nHighPoint = vIsland[nStart];
	int nHighIndex = nStart;

	if (vIsland[nStart] > vIsland[nEnd])
	{
		nHighPoint = vIsland[nEnd];
		nHighIndex = nEnd;
	}

	int nMidPointHeight = nHighPoint;
	int nMidPointIndex = nHighIndex;

	for (int i = nStart + 1; i < nEnd; i++)
	{
		if (vIsland[i] > nMidPointHeight)
		{
			nMidPointHeight = vIsland[i];
			nMidPointIndex = i;
		}
		else if (vIsland[i] < nHighPoint)
		{
			nWater += (nHighPoint - vIsland[i]);
		}
	}

	if (nMidPointIndex != nHighIndex)
	{
		nWater = 0;
		nWater += CalculatePooling(vIsland, nStart, nMidPointIndex);
		nWater += CalculatePooling(vIsland, nMidPointIndex, nEnd);
	}

	return nWater;
}

bool IsNumber(char c)
{
	if (c >= '0' && c <= '9')
		return true;
	return false;
}

bool ParseNumber(string& sLineRemainder, int& nOut)
{
	size_t nToken = sLineRemainder.find_first_of(" ");
	bool bIsNumber = true;
	size_t i = 0;

	if (nToken == string::npos)
		nToken = sLineRemainder.length();

	// Handle negative numbers
	if (sLineRemainder[i] == '-')
		i++;

	while (bIsNumber && i < nToken)
	{
		bIsNumber = IsNumber(sLineRemainder[i]);
		i++;
	}

	string sToken = sLineRemainder.substr(0, nToken);
	sLineRemainder.erase(0, nToken);
	TrimLine(sLineRemainder);

	if (bIsNumber)
	{
		nOut = atoi(sToken.c_str());
	}

	return bIsNumber;
}

bool ProcessLine(const string& sLine, vector<int>& vIsland)
{
	string sWorkingLine(sLine);
	bool bIslandSizeParsed = false;
	int nIslandSize = 0;
	int nIslandElementCount = 0;

	while (!sWorkingLine.empty())
	{
		int nNumber;
		if (!ParseNumber(sWorkingLine, nNumber))
		{
			return false;
		}

		if (!bIslandSizeParsed)
		{
			bIslandSizeParsed = true;
			nIslandSize = nNumber;
			vIsland.reserve(nIslandSize);
		}
		else
		{
			nIslandElementCount++;
			vIsland.push_back(nNumber);

			// Once we've parsed the island, ignore the rest of the line
			if (nIslandElementCount == nIslandSize)
			{
				break;
			}
		}
	}

	if (nIslandElementCount != nIslandSize)
	{
		return false;
	}

	return true;
}

void TrimLine(string& sLine)
{
	// Trim end of the string
	size_t nEnd = sLine.find_last_not_of(" \t\n\r\f\v");
	if (nEnd != string::npos)
		sLine.erase(nEnd + 1);
	else
	{
		sLine.clear();
		return;
	}

	size_t nStart = sLine.find_first_not_of(" \t\n\r\f\v");

	if (nStart != string::npos && nStart > 0)
		sLine.erase(0, nStart);
}
