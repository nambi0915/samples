# Lab 2: Variadic Functions

## Step 1: Run the sample code

fetch, build, install

    $ go get github.com/elephantscale/gopl.io/ch5/sum

## Step 2: 

Find the code for `sum` in your enviroment, analyze that code

## Step 3:

Write variadic functions `max` and `min` similar to sum. Write variants that require atleast one argument 

## Step 4:

Write a variadic version of `strings.Join`

## Step 5:

Write a variadic function `ElementsByTagName` that, given an HTML node tree and zero or more names, returns all the elements that match one of those names. 
Here are two example calls:

```
func ElementsByTagName(doc *html.Node, name ...string) []*html.Node

images := ElementsByTagName(doc, "img")
headings := ElementsByTagName(doc, "h1", "h2", "h3", "h4")
```