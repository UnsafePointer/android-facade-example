Façade design pattern implementation on Android
===============================================

__Description:__

On this example, I'm using this pattern to abstract object persistence operations through a single interface (thanks to Gson) using the following strategies: memory cache (with Guava), database (with ActiveAndroid) and network cache (with OkHttp). This is great for performance and user experience on mobile applications.

__Building:__

In order to build the application, clone this repo recursively:

```sh
$ git clone git@github.com:Ruenzuo/android-facade-example.git --recursive
```

Then set up the dependencies and you're ready to go:

```sh
$ cd android-facade-example && gradle clean build
```  

![android-facade-screenshot-1.png](https://dl.dropboxusercontent.com/u/12352209/GitHub/android-facade-screenshot-1.png)&nbsp;
![android-facade-screenshot-2.png](https://dl.dropboxusercontent.com/u/12352209/GitHub/android-facade-screenshot-2.png)
![android-facade-screenshot-3.png](https://dl.dropboxusercontent.com/u/12352209/GitHub/android-facade-screenshot-3.png)&nbsp;
![android-facade-screenshot-4.png](https://dl.dropboxusercontent.com/u/12352209/GitHub/android-facade-screenshot-4.png)

__Design notes:__

* Here's an UML diagram describing the pattern:

![android-facade-screenshot-4.png](https://dl.dropboxusercontent.com/u/12352209/GitHub/android-facade-screenshot-5.png)

* The SyncService is not part of the façade. Therefore the code there it's a little messy.
* There are a lot of performance and test hooks and methods that you don't need to have on production.
* This has been tested on very large data sources it has 250 countries, 100 cities for each country and 10 stations for each city.
* Currently I'm not using any synchronization pattern, just checking if the data exists on database to make new requests.

__To-Do:__

* `[✓]` <del>Make it public.</del>
* `[✓]` <del>Write the blog post</del>. It's [here](http://ruenzuo.github.io/facade-software-design-pattern-on-ios-and-android/index.html).
* `[ ]` Add tests.
* `[ ]` Decide which strategy use depending on system resources like available memory, network state and others. 
* `[ ]` Add synchronization pattern.

License
=======

    The MIT License (MIT)

    Copyright (c) 2014 Renzo Crisóstomo

    Permission is hereby granted, free of charge, to any person obtaining a copy of
    this software and associated documentation files (the "Software"), to deal in
    the Software without restriction, including without limitation the rights to
    use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
    the Software, and to permit persons to whom the Software is furnished to do so,
    subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
    FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
    COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
    IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
    CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
