# Lab 6.2: Interfaces

## Step 1: Run the code for sleep

fetch, build, install

    $ go get github.com/elephantscale/gopl.io/ch7/sleep

* Analyse the code in `github.com/elephantscale/gopl.io/ch7/sleep`

```
$ go build gopl.io/ch7/sleep
$ ./sleep
Sleeping for 1s...
$ ./sleep -period 50ms
Sleeping for 50ms...
$ ./sleep -period 2m30s
Sleeping for 2m30s...
```

## Step 2: Run the code for tempflag

fetch, build, install

    $ go get github.com/elephantscale/gopl.io/ch7/tempflag

* Analyse the code in `github.com/elephantscale/gopl.io/ch7/tempflag`

```
$ go build gopl.io/ch7/tempflag
$ ./tempflag
20°C
$ ./tempflag -temp -18C
-18°C
$ ./tempflag -temp 212F
100°C
```

## Step 3:

Add support for Kelvin temperatures to tempflag.