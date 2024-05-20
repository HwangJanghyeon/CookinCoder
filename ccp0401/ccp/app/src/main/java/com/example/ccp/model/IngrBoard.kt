package com.example.ccp.model



data class IngrBoard(

    var id: Int = 0,
    var title: String? = null,
    var category: String? = null,
    var name: String? = null,
    var unit: Int = 0,
    var imageUrl: String? = null
) {
    // 생성자
    constructor(title: String, category: String, name: String, unit: Int) : this() {
        this.title = title
        this.category = category
        this.name = name
        this.unit = unit
    }
}
