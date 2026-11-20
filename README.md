My attempt at the CA448 Compiler Construction DCU module to create a parser and semantic analyzer for the SimpL language.

This was graded 97%.

## Building

The toolchain (Temurin JDK 25 and JavaCC 6.1.2) is managed with [mise](https://mise.jdx.dev):

```sh
mise install                          # install the JDK
mise run build                        # fetch JavaCC, regenerate build/ from src/, compile into out/
mise run run build/example.spl        # run the parser + semantic checker on a program
mise run test                         # run the regression tests
mise run check:generated              # check build/ is up to date with src/
```

Edit sources in `src/`. `mise run generate` copies them into `build/` and regenerates the parser. Commit the regenerated `build/` along with your changes.

## Tests

`mise run test` runs every program in `build/*.spl` and `tests/*.spl` through the parser and semantic checker, then diffs the output against `tests/<name>.expected`. After an intentional change in output, run `mise run test:update` and review the snapshot diff before committing.

GitHub Actions (`.github/workflows/test.yml`) runs `mise run check:generated` and `mise run test` on every push to `master` and on every pull request.
