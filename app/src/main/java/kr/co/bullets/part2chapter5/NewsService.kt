package kr.co.bullets.part2chapter5

import retrofit2.Call
import retrofit2.http.GET

interface NewsService {

    @GET("rss?hl=ko&gl=KR&ceid=KR%3Ako")
    fun mainFeed(): Call<NewsRss>
}