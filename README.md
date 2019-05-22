Go Programming - Advanced
===============

# Closure Functions

## What is a Closure function?
* Special type of anonymous function
* Returns an anonymous function when called
* References variables outside the function

## Example of Closure function

```
// squares returns a function that returns
// the next square number each time it is called.
func squares() func() int {
    var x int
    return func() int {
        x++
        return x * x
    }
}
```

* Calling `squares()` will return a anonymous `func() int`
* The func() will access `var x` present outside the body

```
    f := squares()
    fmt.Println(f()) // "1"
    fmt.Println(f()) // "4"
    fmt.Println(f()) // "9"
```

* The variable `f` will contain the func()
* Increments x everytime while calling
* Calling again `g := squares()` will pass a new func() with x=0
