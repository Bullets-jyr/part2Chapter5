package kr.co.bullets.part2chapter5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import kr.co.bullets.part2chapter5.databinding.ActivityMainBinding
import org.jsoup.Jsoup
import retrofit2.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var newsAdapter: NewsAdapter

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://news.google.com/")
        .addConverterFactory(
            TikXmlConverterFactory.create(
                TikXml.Builder()
                    .exceptionOnUnreadXml(false)
                    .build()
            )
        ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newsAdapter = NewsAdapter()

        binding.newRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsAdapter
        }

        val newsService = retrofit.create(NewsService::class.java)
        newsService.mainFeed().enqueue(object : Callback<NewsRss> {
            override fun onResponse(call: Call<NewsRss>, response: Response<NewsRss>) {
                Log.e("MainActivity", "${response.body()?.channel?.item}")

                val list = response.body()?.channel?.item.orEmpty().transform()
                newsAdapter.submitList(list)
                list.forEachIndexed { index, newsModel ->
                    Thread {
//                        val item = list.first()
                        try {
                            val jsoup = Jsoup.connect(newsModel.link).get()
                            val elements = jsoup.select("meta[property^=og:]")
                            val ogImageNode = elements.find { node ->
                                node.attr("property") == "og:image"
                            }

        //                    val imageUrl = ogImageNode?.attr("content")

                            newsModel.imageUrl = ogImageNode?.attr("content")

                            Log.e("MainActivity", "imageUrl: ${newsModel.imageUrl}")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        runOnUiThread {
                            newsAdapter.notifyItemChanged(index)
                        }
                    }.start()
                }

//                newsAdapter.submitList(response.body()?.channel?.item.orEmpty())
            }

            override fun onFailure(call: Call<NewsRss>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}