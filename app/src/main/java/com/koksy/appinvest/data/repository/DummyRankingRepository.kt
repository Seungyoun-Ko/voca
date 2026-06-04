package com.koksy.appinvest.data.repository

import com.koksy.appinvest.domain.model.RankingCategory
import com.koksy.appinvest.domain.model.StockRankingItem
import com.koksy.appinvest.domain.model.ThemeRankingItem
import com.koksy.appinvest.domain.repository.RankingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DummyRankingRepository : RankingRepository {
    override fun observeStockRankings(category: RankingCategory): Flow<List<StockRankingItem>> {
        return flowOf(
            when (category) {
                RankingCategory.TRADING_VALUE -> tradingValueRankings
                RankingCategory.GAINERS -> gainerRankings
                RankingCategory.LOSERS -> loserRankings
                RankingCategory.THEMES -> emptyList()
            },
        )
    }

    override fun observeThemeRankings(): Flow<List<ThemeRankingItem>> {
        return flowOf(themeRankings)
    }

    private val tradingValueRankings = listOf(
        StockRankingItem(
            rank = 1,
            symbol = "NVDA",
            name = "NVIDIA",
            market = "NASDAQ",
            price = "$1,142.32",
            changeRate = 2.84,
            tradingValue = "$42.1B",
            themeTags = listOf("AI 반도체", "데이터센터"),
        ),
        StockRankingItem(
            rank = 2,
            symbol = "005930",
            name = "삼성전자",
            market = "KOSPI",
            price = "78,400원",
            changeRate = 1.17,
            tradingValue = "1.9조원",
            themeTags = listOf("메모리", "온디바이스 AI"),
        ),
        StockRankingItem(
            rank = 3,
            symbol = "TSLA",
            name = "Tesla",
            market = "NASDAQ",
            price = "$183.92",
            changeRate = -0.72,
            tradingValue = "$18.6B",
            themeTags = listOf("전기차", "로보택시"),
        ),
        StockRankingItem(
            rank = 4,
            symbol = "TIGER 미국S&P500",
            name = "TIGER 미국S&P500",
            market = "ETF",
            price = "18,965원",
            changeRate = 0.31,
            tradingValue = "842억원",
            themeTags = listOf("미국지수", "ETF"),
        ),
    )

    private val gainerRankings = listOf(
        StockRankingItem(
            rank = 1,
            symbol = "OKLO",
            name = "Oklo",
            market = "NYSE",
            price = "$14.88",
            changeRate = 12.46,
            tradingValue = "$1.4B",
            themeTags = listOf("SMR", "원전"),
        ),
        StockRankingItem(
            rank = 2,
            symbol = "IONQ",
            name = "IonQ",
            market = "NYSE",
            price = "$41.21",
            changeRate = 8.73,
            tradingValue = "$920M",
            themeTags = listOf("양자컴퓨터"),
        ),
        StockRankingItem(
            rank = 3,
            symbol = "034020",
            name = "두산에너빌리티",
            market = "KOSPI",
            price = "21,350원",
            changeRate = 6.21,
            tradingValue = "7,420억원",
            themeTags = listOf("SMR", "전력인프라"),
        ),
    )

    private val loserRankings = listOf(
        StockRankingItem(
            rank = 1,
            symbol = "RIVN",
            name = "Rivian",
            market = "NASDAQ",
            price = "$10.14",
            changeRate = -7.91,
            tradingValue = "$1.1B",
            themeTags = listOf("전기차"),
        ),
        StockRankingItem(
            rank = 2,
            symbol = "096770",
            name = "SK이노베이션",
            market = "KOSPI",
            price = "104,200원",
            changeRate = -4.38,
            tradingValue = "2,180억원",
            themeTags = listOf("2차전지", "정유"),
        ),
        StockRankingItem(
            rank = 3,
            symbol = "ARKK",
            name = "ARK Innovation ETF",
            market = "ETF",
            price = "$44.36",
            changeRate = -3.02,
            tradingValue = "$486M",
            themeTags = listOf("혁신성장", "ETF"),
        ),
    )

    private val themeRankings = listOf(
        ThemeRankingItem(
            rank = 1,
            name = "SMR",
            momentumScore = 92,
            changeRate = 5.84,
            leadingStocks = listOf("OKLO", "두산에너빌리티", "BWXT"),
            summary = "전력 수요와 원전 정책 모멘텀이 함께 반영되는 중",
        ),
        ThemeRankingItem(
            rank = 2,
            name = "데이터센터",
            momentumScore = 87,
            changeRate = 3.76,
            leadingStocks = listOf("NVDA", "VRT", "EQIX"),
            summary = "AI 서버 증설과 전력/냉각 인프라 수혜가 지속",
        ),
        ThemeRankingItem(
            rank = 3,
            name = "양자컴퓨터",
            momentumScore = 81,
            changeRate = 2.94,
            leadingStocks = listOf("IONQ", "RGTI", "QBTS"),
            summary = "상용화 기대와 정부 연구 예산 확대 이슈가 부각",
        ),
        ThemeRankingItem(
            rank = 4,
            name = "배당성장 ETF",
            momentumScore = 74,
            changeRate = 1.22,
            leadingStocks = listOf("SCHD", "VIG", "TIGER 미국배당다우존스"),
            summary = "금리 인하 기대 속 현금흐름형 자산 선호 회복",
        ),
    )
}
