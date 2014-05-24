Façade design pattern implementation on Android
===========================================

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
