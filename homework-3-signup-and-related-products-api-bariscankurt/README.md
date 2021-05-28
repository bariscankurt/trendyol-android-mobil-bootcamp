## Succulent Shop

### Homework 3 - Signup and Related Products API

For this assignment, I started using Retrofit for api calls. Before this i was using a mock repository to show product list and product detail screens. 
I also added Logout option to dropdown menu in product list fragment. I saved the token to sharedpreferences for bypassing Login screen if user is already logged in. 
I managed this routing logic in StartScreen fragment which doesn't have any layout. Added progress indicator to product list and product detail screens.
Added different Snackbar errors for different situations. Like connection lost, missing token, invalid response from api, unexpected server errors etc.
And finally I added a zoom future for product detail screen. When we are in product detail screen if we click on the product image, application will take us to a new
screen where we can freely zoom in to our product. To examine the product properly image viewer screen (aka zoom screen) loads the original (highest) resolution image.

Includes:

* Retrofit for api calls
* Dropdown menu for Log out (only in product list screen)
* Saves JWT token to sharedpreferences for bypassing login screen if user is already logged in
* StartScreen fragment which acts like a router. For example if there is a JWT token in sharedpreferences folder, user directly goes to the product list screen
* Shows progress indicator until api calls return success code (200)
* Snackbar errors for different situations. Like connection lost, missing token, invalid response from api, unexpected server errors etc.
* Zoom future for product detail screen. When we open a product, if we click on it's image new screen opens and in this screen we can zoom and examine our product better because in this screen we are getting full resolution images.
* I used PhotoView plugin by Chris Bane for zoomable images.

### Dropdown Menu Log out Option:
![Logout Option](https://i.imgur.com/sgeYUFf.png)

### Progress Indicator For Product List And Product Detail Screens:
![Progress Indicator](https://i.imgur.com/qMsDpo3.png)

### Snackbar Error Connection Lost:
![Snackbar Error](https://i.imgur.com/qMsDpo3.png)

### Snackbar Error Identifier or Password Invalid:
![Snackbar Error](https://i.imgur.com/6ZdSor9.png)

### Zoom Future (ImageViewer Screen):
![Zoom Future](https://i.imgur.com/1KqgJ3S.png)