struct variables {
    float a;
    float b;
    char op;
};

program CALCULATE_PROG {
    version CALCULATE_VER {
        float ADD(variables) = 1;
        float SUB(variables) = 2;
        float MUL(variables) = 3;
        float DIV(variables) = 4;
    } = 1;
} = 0x2fffffff;