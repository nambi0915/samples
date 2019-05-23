# Lab 3: Recursion

## Step 1: Run the code for findlinks1

fetch, build, install

    $ go get github.com/elephantscale/gopl.io/ch5/findlinks1

Run along with fetch
```
	$ go build gopl.io/ch1/fetch
	$ go build gopl.io/ch5/findlinks1	
	$ ./fetch https://golang.org | ./findlinks1
```	
`findlinks1` will take the input from the output of `fetch`

## Step 2: Run the code for outline

fetch, build, install

    $ go get github.com/elephantscale/gopl.io/ch5/outline

Run along with fetch
```
	$ go build gopl.io/ch1/fetch
	$ go build gopl.io/ch5/outline	
	$ ./fetch https://golang.org | ./outline
```	
`outline` will take the input from the output of `fetch`

## Step 3:

Change the `findlinks` program to traverse the n.FirstChild linked list using recursive calls to `visit` instead of a loop.

## Step 4:

Write a function to populate a mapping from element names—p, div, span, and so on—to the number of elements with that name in an HTML document tree.

## Step 5:

Write a function to print the contents of all text nodes in an HTML document tree. Do not descend into <script> or <style> elements, since their contents are not visible in a web browser.

## Step 6:

Extend the visit function so that it extracts other kinds of links from the document, such as images, scripts, and style sheets.
