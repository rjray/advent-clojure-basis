# advent-20XX-clojure

This is my code for the 20XX [Advent of Code](https://adventofcode.com/20XX), all solutions in [Clojure](https://clojure.org/).

All code is under the `src` directory. Each solution-file is named `dayNN.clj` and contains both puzzle solutions for that day. These are the publically-facing functions `part-1` and `part-2`. These files are the code *exactly as I used it to solve and submit the answers*. If I revisit any of the days and try to clean up or optimize the solutions, that work will be in a separate file that will be named `dayNNbis.clj`. (Except that I will likely go back and comment the code after the fact, when I'm not racing the clock.)

The `resources` directory contains the input data for each day. These files are named for the day (`dayNN.txt`), and files with the example input are named `dayNN-example.txt`.

## Stats

Number of answers correct on first submission: -/- (--%)

Highest finish for first half: -

Highest finish for second half: -

## Usage

This project is managed with [Leiningen](https://leiningen.org/). Running the following will download any dependencies and start a REPL:

```
lein repl
```

# Advent of Code Clojure Basis

Starter pack for doing [Advent of Code](https://www.adventofcode.com) in Clojure. This is based almost entirely on Mitchell Hanburg's [Advent of Code Clojure Starter](https://github.com/mhanberg/advent-of-code-clojure-starter). I've renamed a few files and deleted a few, but that's all.

Uses [lein](https://github.com/technomancy/leiningen).

## Usage

There are 25 namespaces, 25 input files, 25 example input files, 25 tests, and 50 `lein` tasks.

1. Fill in the tests with the example solutions.
1. Write your implementation.
1. Fill in the final problem inputs into the `lein` task and run `lein run d01.p1`.

## Installation

I'll put more here as this matures.
