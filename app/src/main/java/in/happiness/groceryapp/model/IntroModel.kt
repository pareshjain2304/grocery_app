package `in`.happiness.groceryapp.model

class ImageModel {

    var image_drawable: String = ""
    var titleText: String?=null
    var productDescription: String?=null

    constructor(image_drawable: String, titleText: String?, productDescription: String?) {
        this.image_drawable = image_drawable
        this.titleText = titleText
        this.productDescription = productDescription
    }


}

class IntroModel {

    var image_drawable: Int

    constructor(image_drawable: Int) {
        this.image_drawable = image_drawable
    }


}