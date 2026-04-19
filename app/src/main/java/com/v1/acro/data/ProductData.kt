package com.v1.acro.data

import com.v1.acro.R

object ProductData {
    val categories = listOf(
        ProductCategory(1, "Clothing", R.drawable.clothcategory),
        ProductCategory(2, "Electronic", R.drawable.elecategory),
        ProductCategory(3, "Food", R.drawable.foodcategory),
        ProductCategory(4, "Home", R.drawable.homecategory),
        ProductCategory(5, "Beauty", R.drawable.beautycategory),
        ProductCategory(6, "Jewellery", R.drawable.jewelcategory)
    )

    val products = listOf(
        Product(1, "Sweatshirt", 19, R.drawable.cloth1, "Clothing"),
        Product(2, "Jeans", 29, R.drawable.cloth2, "Clothing"),
        Product(3, "Dress", 59, R.drawable.cloth3, "Clothing"),
        Product(4, "Jacket", 99, R.drawable.cloth4, "Clothing"),
        Product(5, "T-Shirt", 49, R.drawable.cloth5, "Clothing"),
        Product(6, "Gown", 89, R.drawable.cloth6, "Clothing"),

        Product(7, "AC", 1078, R.drawable.elec1, "Electronic"),
        Product(8, "TV", 237, R.drawable.elec2, "Electronic"),
        Product(9, "Laptop", 1848, R.drawable.elec3, "Electronic"),
        Product(10, "Speakers", 38, R.drawable.elec4, "Electronic"),
        Product(11, "Headphones", 48, R.drawable.elec5, "Electronic"),
        Product(12, "Juicer", 129, R.drawable.elec6, "Electronic"),

        Product(13, "Burger", 25, R.drawable.food1, "Food"),
        Product(14, "Pizza", 32, R.drawable.food2, "Food"),
        Product(15, "Fries", 13, R.drawable.food3, "Food"),
        Product(16, "Noodles", 28, R.drawable.food4, "Food"),
        Product(17, "Cake", 15, R.drawable.food5, "Food"),

        Product(18, "Bedsheet", 22, R.drawable.home1, "Home"),
        Product(19, "Clock", 26, R.drawable.home2, "Home"),
        Product(20, "Curtains", 11, R.drawable.home3, "Home"),
        Product(21, "Table", 18, R.drawable.home4, "Home"),

        Product(22, "Lipstick", 16, R.drawable.beau1, "Beauty"),
        Product(23, "Sunscreen", 12, R.drawable.beau2, "Beauty"),
        Product(24, "Comb", 10, R.drawable.beau3, "Beauty"),
        Product(25, "NailPolish", 14, R.drawable.beau4, "Beauty"),
        Product(26, "Powder", 13, R.drawable.beau5, "Beauty"),

        Product(27, "Ring", 100, R.drawable.jewl1, "Jewellery"),
        Product(28, "Necklace", 107, R.drawable.jewl2, "Jewellery"),
        Product(29, "Bracelet", 114, R.drawable.jewl3, "Jewellery"),
        Product(30, "Earrings", 118, R.drawable.jewl4, "Jewellery"),
        Product(31, "Watch", 165, R.drawable.jewl5, "Jewellery"),
        Product(32, "Bangles", 137, R.drawable.jewl6, "Jewellery"),
    )
}