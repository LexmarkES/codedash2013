#include <ctype.h>
#include <malloc.h>
#include <stdio.h>

#define	MAXUL	100000000	/* = 10^8	*/
#define	FORMAT	"%8.8lu"

FILE *fpin;
int   curline = 1;
int   errors  = 0;
int   serie   = 0;
int   totarth = 0;
int   totgeo  = 0;

typedef enum {
	NONE,
	ARITHMETIC,
	GEOMETRIC
} arithgeo;

typedef	struct {
	unsigned long	*digits;
		 int	 length;
} bignum;

typedef struct  number {
struct  number *next;
	int     line;
	int	error;
	bignum *value;
	bignum *correct;
} number;

static int
findfirstnumber(void)
{
	int c = '\0';

	while (!isdigit(c) && !feof(fpin)) {
		if ((c = fgetc(fpin)) == EOF) {
			return '\0';
		}
		if (c == '\n') {
			curline++;
		}
	}

	return c;
}

static bignum *
bignuminit(void)
{
	bignum *num = malloc(sizeof(bignum));

	num->length = 0;
	num->digits = (unsigned long *)0;

	return num;
}

static bignum *
bignumconst(int value)
{
	bignum *num = bignuminit();

	num->length = 1;
	num->digits = malloc(sizeof(num->digits[0]));
	num->digits[0] = value;

	return num;
}

static void
bignumprint(bignum *num)
{
  const	char *fmt = "%lu";
	int   i;

	for (i = num->length - 1; i >= 0; i--) {
		printf(fmt, num->digits[i]);
		fmt = FORMAT;
	}
}

static void
bignumsane(bignum *num)
{
	int i;

	for (i = 0; i <= num->length - 2; i++) {
		if (num->digits[i] >= MAXUL) {
			num->digits[i+1] += (num->digits[i] / MAXUL);
			num->digits[i  ] %=  MAXUL;
		}
	}
	if (num->digits[num->length - 1] >= MAXUL) {
		num->length++;
		num->digits = realloc(num->digits,
				      num->length * sizeof(num->digits[0]));
		num->digits[num->length - 1] = num->digits[num->length - 2] / MAXUL;
		num->digits[num->length - 2] %= MAXUL;
	}
}

static void
bignumadd(bignum *num, unsigned long term)
{
	num->digits[0] += term;
	bignumsane(num);
}

static void
bignummul(bignum *num, int factor)
{
	int   i;

	for (i = num->length - 1; i >= 0; i--) {
		num->digits[i] *= factor;
	}

	bignumsane(num);
}

static void
bignumadddigit(bignum *num, int digit)
{

	bignummul(num, 10);
	bignumadd(num, digit);
}

static int
bignumequal(bignum *left, bignum *right)
{
	int i;

	if (left->length != right->length) {
		return 0;
	}

	for (i = left->length - 1; i >= 0; i--) {
		if (left->digits[i] != right->digits[i]) {
			return 0;
		}
	}

	return 1;
}

static bignum *
bignumcopy(bignum *src)
{
	int     i;
	bignum *dest = bignuminit();

	dest->length = src->length;
	dest->digits = malloc(dest->length * sizeof(dest->digits[0]));

	for (i = dest->length - 1; i >= 0; i--) {
		dest->digits[i] = src->digits[i];
	}

	return dest;
}

static number *
readnumber(int first, int *c)
{
	int	cur;
	bignum *value = bignumconst(first);
	number *next = malloc(sizeof(number));

	if (next != (number *)0) {
		next->line    = curline;
		next->next    = (number *)0;
		next->error   = 0;
		next->correct = (bignum *)0;

		while (((cur = fgetc(fpin)) != EOF)
		   && (isdigit(cur) || (cur == '\r') || (cur == '\n'))) {
			if (isdigit(cur)) {
				bignumadddigit(value, cur - '0');
			} else if (cur == '\n') {
				curline++;
			}
		}

		next->value = value;

		*c = cur;
	}

	return next;
}


static int
readserie(int first, number **head)
{
	int      cur    = first;
	int      length = 0;
	number  *next;
	number **tail = head;

	while ((next = readnumber(cur - '0', &cur)) != (number *)0) {
		*tail =  next;
		tail  = &next->next;
		length++;

		while ((cur != EOF) && !isdigit(cur)) {
			if (cur == '\n') {
				curline++;
			} else if (cur == '.') {
				return length;
			}
			cur = fgetc(fpin);
		}
	}
	return length;
}

static arithgeo
seriekind(number *head)
{
	/* This only works fine as the first three numbers are not too big... */
	unsigned long value1 = head->value->digits[0];
	unsigned long value2 = head->next->value->digits[0];
	unsigned long value3 = head->next->next->value->digits[0];
	unsigned long factor;

	if ((value2 - value1) == (value3 - value2)) {
		return ARITHMETIC;
	}

	factor = (value1 == 0 ? 0 : value2 / value1);
	if ((value1 * factor == value2) && (value2 * factor == value3)) {
		return GEOMETRIC;
	}

	return NONE;
}

static int
handlearithmetic(number *head)
{
	/* This only works fine as the difference is within this range... */
	  int   err  = 0;
 unsigned long  diff = head->next->value->digits[0] - head->value->digits[0];
	bignum *term = bignumconst(head->value->digits[0]);

	while (head != (number *)0) {
		if (!bignumequal(term, head->value)) {
			head->correct = bignumcopy(term);
			head->error   = 1;
			err++;
		}
		bignumadd(term, diff);
		head  = head->next;
	}

	return err;
}

static int
handlegeometric(number *head)
{
	/* This only works fine as the difference is within this range... */
	 int	 err    = 0;
unsigned long    factor = head->next->value->digits[0] / head->value->digits[0];
	 bignum *term   = bignumconst(head->value->digits[0]);

	while (head != (number *)0) {
		if (!bignumequal(term, head->value)) {
			head->correct = bignumcopy(term);
			head->error   = 1;
			err++;
		}
		bignummul(term, factor);
		head  = head->next;
	}

	return err;
}

static void
handleserie(number *head, int length)
{
	arithgeo which = seriekind(head);
	int	 err   = 0;
	int	 first = 1;

	serie++;

	printf("%d. Line %d: %s: %d terms, ",
		serie, head->line,
		(which == ARITHMETIC ? "arithmetic" : "geometric"),
		length);

	switch (which) {
	   case	ARITHMETIC: err = handlearithmetic(head); totarth++;	break;
	   case	GEOMETRIC:  err = handlegeometric (head); totgeo++;	break;
	   case	NONE:
	   default:							break;
	}

	errors += err;
	printf("%d error%s", err, (err == 1 ? "" : "s"));

	while (head != (number *)0 && err > 0) {
		if (head->error) {
			printf("%c ", (first ? ':' : ','));
			bignumprint(head->value);
			printf(": ");
			bignumprint(head->correct);
			err--;
			first = 0;
		}
		head = head->next;
	}
	printf("\n");
}

static void
cleanup(number *head)
{
	number *next;

	while ((next = head) != (number *)0) {
		head = next->next;
		if (next->correct != (bignum *)0) {
			free(next->correct);
		}
		free(next->value);
		free(next);
	}
}

int
main(int argc, char *argv[])
{
	char    cur;
	number *head;
	int	length;

	fpin = stdin;

	if ((argc > 1) && ((fpin = fopen(argv[1], "r")) == (FILE *)0)) {
		fprintf(stderr,
			"%s: could not open: %s\nUsage: %s [inputfile]\n",
			argv[0], argv[1], argv[0]);
		return 1;
	}

	while (!feof(fpin)) {
		if ((cur = findfirstnumber()) != '\0') {
			head = (number *)0;
			if ((length = readserie(cur, &head)) >= 3) {
				handleserie(head, length);
			}
			cleanup(head);
		}
	}

	printf("Total of %d arithmetic and %d geometric progressions, total of %d errors.\n",
		totarth, totgeo, errors);

	return 0;
}
