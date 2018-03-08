package fr.epita.hellogames

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Najjaj on 07/03/2018.
 */
interface WebServiceInterface {
    @GET("game/list")
    fun listToDos(): Call<List<Game>>

    @GET("game/details")
    fun listGameDetails(@Query("game_id") game_id: Int): Call<GameDetails>
}