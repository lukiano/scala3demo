# Scala 3 demo

A command-line tic-tac-toe game based on [Writing a simple CLI game in Scala 3](https://softwaremill.com/writing-a-simple-cli-game-in-scala-3).

## Scala Native

Uses Scala 3.9.0, sbt 2.0.8, and Scala Native 0.5.12. Building requires a JDK for sbt and a C/C++ toolchain (on macOS, install the Xcode Command Line Tools with `xcode-select --install`). See the [Scala Native setup guide](https://scala-native.org/en/stable/user/setup.html) for other platforms.

```sh
sbt nativeLink
./target/out/native0.5/scala-3.9.0/scala3demo/scala3demo
```

Or compile and run directly with `sbt run`. The executable runs without a JVM. Enter moves such as `A1` or `B2`; a new game starts after each win or draw. Press Ctrl-C to exit.

With sbt 2, `%%` automatically selects Native artifacts when `ScalaNativePlugin` is enabled.

Run the board, coordinate, game, and Cats Effect runtime tests on Scala Native:

```sh
sbt 'testOnly BoardSpec CoordinateSpec GameSpec GameRuntimeSpec'
```

The existing `GameRuntimeInterpretedSpec` is excluded from this command because its placeholder interpreter always returns `"test"`, causing the input-validation loop to run indefinitely. An unrestricted `sbt test` includes that unfinished test.
