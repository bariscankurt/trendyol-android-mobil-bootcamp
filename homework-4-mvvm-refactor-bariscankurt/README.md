## Succulent Shop

### Homework 4 - MVVM Refactor

### Completed Todos

* Move Signup screen into MVVM pattern
* Move Related products feature into MVVM pattern
* Move all the logic that's not covered by my migration (showing circular progress indicators, checking jwtStore and opening product list if user logged in, your bonus features etc.)to MVVM pattern
* Move ProductAdapter into Data Binding
* Remove viewBinding true from gradle and disable view binding.
* Remove tools:viewBindingIgnore="true" from layout files since it will be unnecessary after disabling view binding.

### Additional Futures

* Snackbar passing between screens prevented
* Snackbar's "blocked by soft keyboard" (we can't see the snackbar if soft keyboard is open) problem solved with adding windowsoftinputmode attribute to manifest
* GenericRequestCallback created to prevent similar interface duplication.
* ResponseBody.errorMessage extension function moved to ResponseBodyExt file to prevent code duplication.
* Start fragment (where we look jwt token and decide where to go) stayed same because it doesn't have any view and i didn't want to add unnecessary splash screen 
* Image Viewer (this is my zoomable screen future) moved to MVVM. 

Zoom Future:

![Zoom Future](https://i.imgur.com/nt0AftU.png)

