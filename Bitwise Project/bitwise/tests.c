/* Testing Code */

#include <limits.h>
#include <math.h>

/* Routines used by floating point test code */

/* Convert from bit level representation to floating point number */
float u2f(unsigned u) {
    union {
        unsigned u;
        float f;
    } a;
    a.u = u;
    return a.f;
}

/* Convert from floating point number to unsigned representation */
unsigned f2u(float f) {
    union {
        unsigned u;
        float f;
    } a;
    a.f = f;
    return a.u;
}

/* Convert from floating point number to int representation */
int f2i(float f) {
    union {
        int i;
        float f;
    } a;
    a.f = f;
    return a.i;
}

int test_bitOr(int x, int y) {
    return x|y;
}

int test_tmax(void) {
    return 2147483647;
}

int test_sign(int x) {
    if (x > 0) {
        return 1;
    } else if (x == 0) {
        return 0;
    } else {
        return -1;
    }
}

int test_copyLSB(int x) {
    if(x & 1 == 1) {
        return -1;
    } else {
        return 0;
    }
}
int test_replaceByte(int x, int n, int c) {
  if(n == 0) { return (x & 0xffffff00) + c; }
  else if(n == 1) { return (x & 0xffff00ff) + (c<<8); }
  else if(n == 2) { return (x & 0xff00ffff) + (c<<16); }
  else if(n == 3) { return (x & 0x00ffffff) + (c<<24); }
  return 0;
}
int test_rotateLeft(int x, int n) {
    if(n == 0) { return x; }
    else {
        int m1 = -1 << n;
        int m2 = (x >> (32 - n)) & (~m1);
        return (x<<n) + m2;
    }
}
int test_bang(int x) {
  return !x;
}
int test_bitParity(int x) {
  /* Work things down.  At any time, upper part of words will
     contain junk.  Mask this off at the very end
  */
 for(int i = 1; i < 32; i++) {
    x = x ^ (x>>1);
 }
 return x&1;
}
unsigned test_floatScale4(unsigned uf) {
    float f = u2f(uf);
    float fourf = 4*f;
    if (isnan(f)) {
        return uf;
    } else {
        return f2u(fourf);
    }
}

int test_floatFloat2Int(unsigned uf) {
    float f = u2f(uf);
    int tmin = 0x80000000;
    int tmax = 0x7fffffff;
    if ((float)tmin <= f && f <= (float)tmax) {
        return (int)f;
    }
    return 0x80000000u;
}
