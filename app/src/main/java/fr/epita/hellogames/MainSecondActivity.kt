package fr.epita.hellogames

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_selected_game.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Najjaj on 07/03/2018.
 */
class MainSecondActivity : AppCompatActivity(), View.OnClickListener {

    var data = GameDetails(0, "", "", 0, 0, "","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_game)


        val baseURL = "https://androidlessonsapi.herokuapp.com/api/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(jsonConverter)
                .build()
        val service: WebServiceInterface = retrofit.create(WebServiceInterface::class.java)

        val callback = object : Callback<GameDetails> {
            override fun onFailure(call: Call<GameDetails>?, t: Throwable?) {
                Log.d("TAG", "WebService call failed")
            }
            override fun onResponse(call: Call<GameDetails>?,
                                    response: Response<GameDetails>?) {
                if (response != null) {
                    if (response.code() == 200) {
                        val responseData = response.body()

                        if (responseData != null) {
                            Log.d("TAG", "WebService success : " + data.type)
                            data = responseData
                            set_name_text.text =  data.name
                            set_type_text.text = data.type
                            set_nbplayers_text.text = data.players.toString()
                            set_year_text.text = data.year.toString()
                            description_text.text = data.description_en
                            setBackground(data.name)
                        }
                    }
                }
            }
        }
        val intent = intent
        // extract data from the intent
        val message = intent.getIntExtra("MESSAGE", 0)
        service.listGameDetails(message).enqueue(callback)

        button_want_more.setOnClickListener(this@MainSecondActivity)
    }

    fun setBackground(name: String) {
        when (name.toLowerCase()) {
            "battleship" -> game_view.setBackgroundResource(R.drawable.battleship)
            "gameoflife" -> game_view.setBackgroundResource(R.drawable.gameoflife)
            "hangman" -> game_view.setBackgroundResource(R.drawable.hangman)
            "mastermind" -> game_view.setBackgroundResource(R.drawable.mastermind)
            "memory" -> game_view.setBackgroundResource(R.drawable.memory)
            "minesweeper" -> game_view.setBackgroundResource(R.drawable.minesweeper)
            "simon" -> game_view.setBackgroundResource(R.drawable.simon)
            "slidingpuzzle" -> game_view.setBackgroundResource(R.drawable.slidingpuzzle)
            "sudoku" -> game_view.setBackgroundResource(R.drawable.sudoku)
            "tictactoe" -> game_view.setBackgroundResource(R.drawable.tictactoe)
            else -> {
                game_view.setBackgroundResource(R.drawable.sudoku)
            }
        }

    }

    override fun onClick(p0: View?) {
        val url = data.url
        // Define an implicit intent
        val intent = Intent(Intent.ACTION_VIEW)
        // Add the required data in the intent (here the URL we want to open)
        intent.data = Uri.parse(url)
        // Launch the intent
        startActivity(intent)
    }

}