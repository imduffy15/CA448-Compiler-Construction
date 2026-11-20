My attempt at the CA448 Compiler Construction DCU module to create a parser and semantic analyzer for the SimpL language.

This was graded 97%.

## Building

The toolchain (Temurin JDK 25 and JavaCC 6.1.2) is managed with [mise](https://mise.jdx.dev):

```sh
mise install                          # install the JDK
mise run build                        # fetch JavaCC, regenerate build/ from src/, compile into out/
mise run run build/example.spl        # run the parser + semantic checker on a program
mise run test                         # run the checker on the bundled examples
```

Edit sources in `src/`. `mise run generate` copies them into `build/` and regenerates the parser.
