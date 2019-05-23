# Lab 4: Defer

## Step 1: Run the code for title1

fetch, build, install

    $ go get github.com/elephantscale/gopl.io/ch5/title1

* Analyse the code in `github.com/elephantscale/gopl.io/ch5/title1`
* Without `defer` statement

```
	$ go build gopl.io/ch5/title1
	$ ./title1 http://gopl.io
	The Go Programming Language
	$ ./title1 https://golang.org/doc/effective_go.html
	Effective Go - The Go Programming Language
	$ ./title1 https://golang.org/doc/gopher/frontpage.png
	title: https://golang.org/doc/gopher/frontpage.png
		has type image/png, not text/html
```	


## Step 2: Run the code for title2

fetch, build, install

    $ go get github.com/elephantscale/gopl.io/ch5/title2

* Analyse the code 
* With `defer` statement
* Run using the same procedure of `title1`

## Step 3: Run the code for trace

fetch, build, install

    $ go get github.com/elephantscale/gopl.io/ch5/trace

```
$ go build gopl.io/ch5/trace
$ ./trace
2015/11/18 09:53:26 enter bigSlowOperation
2015/11/18 09:53:36 exit bigSlowOperation (10.000589217s)
```

## Step 4:

Without changing its behavior, rewrite the `gopl.io/ch5/fetch` function to use defer to close the writable file.



