# Lab 6.1: Interfaces

## Step 1: Run the code for bytecounter

fetch, build, install

    $ go get github.com/elephantscale/gopl.io/ch7/bytecounter

* Analyse the code in `github.com/elephantscale/gopl.io/ch7/bytecounter`

## Step 2:

Using the ideas from ByteCounter, implement counters for words and for lines. You will find `bufio.ScanWords` useful.

## Step 3:

Write a function `CountingWriter` with the signature below that, given an io.Writer, returns a new Writer that wraps the original, and a pointer to an int64 variable that at any moment contains the number of bytes written to the new Writer.

```
func CountingWriter(w io.Writer) (io.Writer, *int64)
```