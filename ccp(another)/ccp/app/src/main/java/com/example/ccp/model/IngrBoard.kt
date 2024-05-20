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

    // name 속성 설정 메서드
    fun setIngredientName(name: String) {
        this.name = name
    }

    // unit 속성 설정 메서드
    fun setIngredientUnit(unit: Int) {
        this.unit = unit
    }
}
