package fr.epita.hellogames

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.format.Time
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val data = arrayListOf<Game>()
    var arrayNumber = ArrayList<Int>(10)
    var arrayRandom = ArrayList<Int>(10)
    val random = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        for (i in 0..9) {
            arrayNumber.add(i)
        }
        arrayRandom  = shuffle(arrayNumber)
        // A List to store or objects

        // The base URL where the WebService is located
        val baseURL = "https://androidlessonsapi.herokuapp.com/api/"
        // Use GSON library to create our JSON parser
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        // Create a Retrofit client object targeting the provided URL
        // and add a JSON converter (because we are expecting json responses)
        val retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(jsonConverter)
                .build()
        // Use the client to create a service:
        // an object implementing the interface to the WebService
        val service: WebServiceInterface = retrofit.create(WebServiceInterface::class.java)

        val callback = object : Callback<List<Game>> {
            override fun onFailure(call: Call<List<Game>>?, t: Throwable?) {
                // Code here what happens if calling the WebService fails
                Log.d("TAG", "WebService call failed")
            }
            override fun onResponse(call: Call<List<Game>>?,
                                    response: Response<List<Game>>?) {
                // Code here what happens when WebService responds
                if (response != null) {
                    if (response.code() == 200) {
                        // We got our data !
                        val responseData = response.body()

                        if (responseData != null) {
                            Log.d("TAG", "WebService success : " + data.size)
                            data.addAll(responseData)
                            setBackground(gameTR, data[arrayRandom[0]].name)
                            setBackground(gameTL, data[arrayRandom[1]].name)
                            setBackground(gameBR, data[arrayRandom[2]].name)
                            setBackground(gameBL, data[arrayRandom[3]].name)
                        }
                    }
                }
            }
        }

        // Finally, use the service to enqueue the callback
        // This will asynchronously call the method

         service.listToDos().enqueue(callback)

        gameTR.setOnClickListener(this@MainActivity)
        gameTL.setOnClickListener(this@MainActivity)
        gameBR.setOnClickListener(this@MainActivity)
        gameBL.setOnClickListener(this@MainActivity)
    }

    fun shuffle(array: ArrayList<Int>) : ArrayList<Int> {
        var currIndex: Int = array.size
        var tmp: Int = 0
        while (currIndex != 0) {
            var rnd: Double = Math.floor(Math.random() * currIndex)
            currIndex -= 1
            tmp = array[currIndex]
            array[currIndex] = arrayNumber[rnd.toInt()]
            array[rnd.toInt()] = tmp
        }
        return array
    }

    override fun onClick(clickedView: View?) {
        if (clickedView != null) {
            when (clickedView.id) {
                R.id.gameTR -> {
                    Toast.makeText(this@MainActivity, "clicked",Toast.LENGTH_SHORT).show()
                    goGame(data[arrayRandom[0]].id)
                }
                R.id.gameTL -> {
                    Toast.makeText(this@MainActivity, "clicked",Toast.LENGTH_SHORT).show()
                    goGame(data[arrayRandom[1]].id)
                }
                R.id.gameBR -> {
                    Toast.makeText(this@MainActivity, "clicked",Toast.LENGTH_SHORT).show()
                    goGame(data[arrayRandom[2]].id)
                }
                R.id.gameBL -> {
                    Toast.makeText(this@MainActivity, "clicked",Toast.LENGTH_SHORT).show()
                    goGame(data[arrayRandom[3]].id)
                }

            }
        }
    }

    fun setBackground(game: View, name: String) {
        when (name.toLowerCase()) {
            "battleship" -> game.setBackgroundResource(R.drawable.battleship)
            "gameoflife" -> game.setBackgroundResource(R.drawable.gameoflife)
            "hangman" -> game.setBackgroundResource(R.drawable.hangman)
            "mastermind" -> game.setBackgroundResource(R.drawable.mastermind)
            "memory" -> game.setBackgroundResource(R.drawable.memory)
            "minesweeper" -> game.setBackgroundResource(R.drawable.minesweeper)
            "simon" -> game.setBackgroundResource(R.drawable.simon)
            "slidingpuzzle" -> game.setBackgroundResource(R.drawable.slidingpuzzle)
            "sudoku" -> game.setBackgroundResource(R.drawable.sudoku)
            "tictactoe" -> game.setBackgroundResource(R.drawable.tictactoe)
            else -> {
                game.setBackgroundResource(R.drawable.sudoku)
            }
        }

    }

    fun goGame(game_id: Int) {
        val intent = Intent(this@MainActivity, MainSecondActivity::class.java)
        // Insert extra data in the intent
        val message = game_id
        intent.putExtra("MESSAGE", message)
        // Start the other activity by sending the intent
        startActivity(intent)
    }
}
