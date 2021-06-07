# Trendyol Mobil Android Bootcamp Final Project - Succulent Shop

Before going into technical details, i wanted to show my gratitude to those who made this amazing bootcamp possible.
I really enjoyed every second of it, learned so much from it and wanted to say thank you for all of your efforts!

## Final Assisgnment Todos:

- [x] Move signup and related product features to coroutines (and flow if relevant):

I made a decision about where should we use flow. Of course we want to use it if we are gonna make multiple emits.
So we should move related products to flow but we don't need to move signup to flow. That's what i did in my project.

- [x] In the screens you show a progress indicator use flow to emit loading, success and error states.

This is a bit tricky. As i searched about loading emit there are tons of different approaches. So i decided to approach it like this:

```
suspend fun fetchProducts(): Flow<GenericResult<Any>> = flow {
        emit(Loading)

        val cachedProducts = db.productDao().getAll()
```
I emit loading first. After that if our db/api emits an alternative response, it'll be shown.

# Personal Features and Improvements

As you know, everybody in this bootcamp working on the same project. So i'll try not to bother you with talking too much about common features.

## UI/UX & Functional Improvements

### Problem: When we don't have any image in our Room db, we are seeing this on product list screen

![Imageless cards](https://i.imgur.com/mhU4MZ1.png)

I noticed this problem, after Room adds images to db we don't see it but perfection lies in details. So i searched what can i do about this
and after a lot of complicated ways i found out Glide has a solution for this kind of problems, which is placeholder.

```
Glide.with(binding.root.context)
                .load(product.imageUrl)
                .placeholder(R.drawable.succulentlogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerInside()
                .into(binding.imageView)
```

After adding this we're gonna see this beautiful screen when our items are loading!

![placeholder](https://i.imgur.com/eVvC9Ic.png)

I added diskCacheStrategy too because we are using same resource a lot of times and with this DiskCacheStrategy.ALL we are reducing our bandwith usage.

---

### Problem: We can't refresh our product list screen until we logout and login or restart our app.

I solved this problem with swiperefreshlayout.

![swipe refresh](https://i.imgur.com/IREoOoR.png)

---

### Problem: We can't zoom in product images.

I solved this problem with Chris Bane's photoView plugin. I looked at Trendyol's solution for this need and i saw they're using a new screen to show zoomable image.
I decided to go with this and added ImageViewerFragment to my project. In this fragment we are showing full resolution image to user and of course it's zoomable!

![imageviewer](https://i.imgur.com/VtntOjk.png) ![imageviewerzoomin](https://i.imgur.com/GoQq9FP.png)

---

### Problem: If we get snackbar error when keyboard is open it stays behind of the keyboard. 

I solved this problem with adding this property to manifest.
```
android:windowSoftInputMode="adjustResize"
```
After this we can see our error message easily:

![errormessage](https://i.imgur.com/KuVWmlC.png)
---

### Problem: Keyboard Passing Between Screens

As i stated, keyboard passes between fragments which is very frustrating for the user. I solved this problem by adding this method to BaseFragment:
```
private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
``` 
## Code-Quality

### Deleting StartScreen Fragment and Moving It's Logic To Loginfragment

At first assingments i used StartScreen fragment as an entry point. But i realised it's an overkill for this project.
I decided to use a better approach (not generally, for this succulent shop project) and move my Jwt control logic to loginfragment's create method.

```
override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.isUserAuthenticated()
        super.onCreate(savedInstanceState)
    }
``` 
And isUserAuthenticated:
```
fun isUserAuthenticated() {
        if (repository.loadJwt() != null) {
            navigation.navigate(LoginFragmentDirections.loginToProductList())
        }
    }
``` 
---

### Creating Generic Result

We shouldn't write a new sealed class for every single repository. So for this problem i created a generic result sealed class.

```
sealed class GenericResult<out T : Any> {
    class Success<out T : Any>(val obj: T) : GenericResult<T>()
    class ClientError(val errorMessage: String? = null) : GenericResult<Nothing>()
    object Loading : GenericResult<Nothing>()
    object Failure : GenericResult<Nothing>()
    object UnexpectedError : GenericResult<Nothing>()
}
```
Voila! We don't need to write it in everywhere anymore.
---

### Making JwtStore Class Singleton Object

This is not the best option i've to admit it. But we don't use dependency injection and we have to pass JwtStore multiple times.
And other problem is everytime we pass JwtStore we have to send context with it. This is an unwanted situation. So i decided to 
create it as a singleton object, build it in application class. This isn't a perminent solution but it really helped.
```
object JwtStore {
    private lateinit var sharedPrefs: SharedPreferences

    private const val PREFS_FILE_NAME = "jwt_store"
    private const val PREFS_KEY_JWT = "jwt"

    fun buildPrefs(context: Context) {
        sharedPrefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun saveJwt(jwt: String) = sharedPrefs.edit(commit = true) {
        putString(PREFS_KEY_JWT, jwt)
    }

    fun loadJwt(): String? = sharedPrefs.getString(PREFS_KEY_JWT, null)

    fun deleteJwt() = sharedPrefs.edit(commit = true) {
        remove(PREFS_KEY_JWT)
    }
}
```

### Creating JwtStoreInterface and Implementing It to Repository

We have another problem. We are passing our JwtStore nearly everywhere in our fragment-viewModel-repository trio.
I created this interface to solve this problem (with the help of our singleton JwtStore object):
```
interface JwtStoreInterface {
    fun saveJwt(jwt: String) = JwtStore.saveJwt(jwt)

    fun loadJwt() = JwtStore.loadJwt()

    fun deleteJwt() = JwtStore.deleteJwt()
}
```
And implemented it to our repositories:
```
class LoginRepository : JwtStoreInterface
```
Now, we send repository to our viewModel and all the JwtStore control functionality goes with it. We can
reach JwtStore functions simply writing:
```
repository.loadJwt()
```

### Creating Visibility Enum

When we are trying to control visibilities in our viewmodels we have to implement View class. But this isn't necessary.
After all visibilies are just integers. Of course writing them as hardcoded integers is not a good solution. They can change in the future.
So i created an enum class for this. It's not too necessary but it helps.

```
enum class Visibility(val value: Int) {
    VISIBLE(View.VISIBLE),
    INVISIBLE(View.INVISIBLE),
    GONE(View.GONE)
}
```
### Unit Tests

This is a very new topic for me. I managed to write unit tests for validate classes. Share with you one example:
```

```
I started learning about this topic recently and TDD is very mind opening for me. I always wondered how can i know my classes are on point? Or my functions are good enough?
Thinking about testability helps us for this kind of questions. Murat Can Bur adviced me to learn unit testing and for starting told me to write unite tests for validation classes first.
I promised him that i will write unit tests for my validation classes before bootcamp finishes. So i did it and gonna learn more about this topic very soon.

## I just want to thank to every person who contributed to this learning experience. I hope we will make great things together in the future.

# Special Thanks to Trendyol, Kodluyoruz and Safa Orhan 
