# Lab 5: Methods

## Step 1: Run the code for coloredpoint

fetch, build, install

    $ go get github.com/elephantscale/gopl.io/ch6/coloredpoint

* Analyse the code in `github.com/elephantscale/gopl.io/ch6/coloredpoint`
* This demonstrates struct embedding

```
	$ go build gopl.io/ch6/coloredpoint
```	

## Step 2: Run the code for urlvalues

fetch, build, install

    $ go get github.com/elephantscale/gopl.io/ch6/urlvalues

* Analyse the code 
* Demonstrates the method creation for `map[string][]string` type

## Step 3: Analyse the code in intset

Analyse the code in `github.com/elephantscale/gopl.io/ch6/intset`


## Step 4:

Implement these additional methods in intset:

```
func (*IntSet) Len() int      // return the number of elements
func (*IntSet) Remove(x int)  // remove x from the set
func (*IntSet) Clear()        // remove all elements from the set
func (*IntSet) Copy() *IntSet // return a copy of the set
```

# Step 5:

Define a variadic (*IntSet).AddAll(...int) method that allows a list of values to be added, such as s.AddAll(1, 2, 3).

# Step 6:

Add a method Elems that returns a slice containing the elements of the set, suitable for iterating over with a range loop.

# Step 7:

The type of each word used by IntSet is uint64, but 64-bit arithmetic may be inefficient on a 32-bit platform. Modify the program to use the uint type, which is the most efficient unsigned integer type for the platform. Instead of dividing by 64, define a constant holding the effective size of uint in bits, 32 or 64. You can use the perhaps too-clever expression 32 << (^uint(0) >> 63) for this purpose.