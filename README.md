# perlin2d

A simple implementation of Perlin noise in Clojurescript.

Based on the excellent article: https://rtouti.github.io/graphics/perlin-noise-algorithm


The script comes with two main functions:

`perind2d` will generate a noise value for a given x and Y value.

`do-octave` will repeatedly call the Perlin function for N octaves as well as add amplitude and frequency to create a
more realistic look for things like terrain.

```clojure

(perlin2D 1 2)

(do-octave 1 2 8 0.005 1)

```

If you look in the ```demo``` folder you'll find a simple example that uses the provide functions to generate a randomized terrain.

## Installation (`deps.edn`)

Add this to your `deps.edn` `:deps` map:
```
peterjewicz/perlin2d {:git/url "https://github.com/peterjewicz/perlin2d.git"
                     :git/sha "bd6e8d60b83bdc2a8730dbd328c2899a90604773"}
```

Then you can use this library's functions via the `perlin2d.core` namespace.
