package com.example.illergi_opencv_java_test.entities

data class UserAllergyFood(
    val fishandShellfish: List<String>? = null,
    val egg: List<String>? = null,
    val dairyProduct: List<String>? = null,
    val peanut: List<String>? = null,
    val soyBean: List<String>? = null,
    val treeNuts: List<String>? = null,
    val cerealwithGluten: List<String>? = null,
    val sulfites: List<String>? = null,
    val celery: List<String>? = null,
    val lupine: List<String>? = null,
    val sesameSeed: List<String>? = null,
    val spicesandHerbs: List<String>? = null,
    val fruitandVegetable: List<String>? = null,
    val meat: List<String>? = null
) {

    operator fun get(fieldName: String): List<String>? {

        return when (fieldName) {
            "fishandShellfish" -> this.fishandShellfish
            "egg" -> this.egg
            "dairyProduct" -> this.dairyProduct
            "peanut" -> this.peanut
            "soyBean" -> this.soyBean
            "treeNuts" -> this.treeNuts
            "cerealwithGluten" -> this.cerealwithGluten
            "sulfites" -> this.sulfites
            "celery" -> this.celery
            "lupine" -> this.lupine
            "sesameSeed" -> this.sesameSeed
            "spicesandHerbs" -> this.spicesandHerbs
            "fruitandVegetable" -> this.fruitandVegetable
            "meat" -> this.meat
            else -> emptyList()
        }
    }

    fun mergeList(): List<String> {
        var list = ArrayList<String>();
        this.fishandShellfish?.let { list.addAll(it) }
        this.egg?.let { list.addAll(it) }
        this.dairyProduct?.let { list.addAll(it) }
        this.peanut?.let { list.addAll(it) }
        this.soyBean?.let { list.addAll(it) }
        this.treeNuts?.let { list.addAll(it) }
        this.cerealwithGluten?.let { list.addAll(it) }
        this.sulfites?.let { list.addAll(it) }
        this.celery?.let { list.addAll(it) }
        this.lupine?.let { list.addAll(it) }
        this.sesameSeed?.let { list.addAll(it) }
        this.spicesandHerbs?.let { list.addAll(it) }
        this.fruitandVegetable?.let { list.addAll(it) }
        this.meat?.let { list.addAll(it) }
        return list
    }


}
