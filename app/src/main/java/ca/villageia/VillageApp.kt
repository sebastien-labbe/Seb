package ca.villageia

import android.app.Application
import ca.villageia.data.AppDatabase
import ca.villageia.data.VillageRepository

class VillageApp : Application() {
    val repository by lazy { VillageRepository(AppDatabase.build(this)) }
}

val Application.repository get() = (this as VillageApp).repository
