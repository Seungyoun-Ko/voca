package com.koksy.appinvest.data.repository

import com.koksy.appinvest.domain.model.AssetType
import com.koksy.appinvest.domain.model.FinancialMetric
import com.koksy.appinvest.domain.model.PricePoint
import com.koksy.appinvest.domain.model.StockDetail
import com.koksy.appinvest.domain.model.StockSearchResult
import com.koksy.appinvest.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DummySearchRepository : SearchRepository {
    override fun searchStocks(query: String): Flow<List<StockSearchResult>> {
        val normalizedQuery = query.trim().lowercase()
        val results = if (normalizedQuery.isBlank()) {
            searchResults
        } else {
            searchResults.filter { item ->
                item.symbol.lowercase().contains(normalizedQuery) ||
                    item.name.lowercase().contains(normalizedQuery) ||
                    item.market.lowercase().contains(normalizedQuery) ||
                    item.themeTags.any { it.lowercase().contains(normalizedQuery) }
            }
        }
        return flowOf(results)
    }

    override fun observeStockDetail(symbol: String): Flow<StockDetail?> {
        return flowOf(details[symbol])
    }

    private val searchResults = listOf(
        StockSearchResult(
            symbol = "NVDA",
            name = "NVIDIA",
            market = "NASDAQ",
            assetType = AssetType.STOCK,
            themeTags = listOf("AI 반도체", "데이터센터"),
        ),
        StockSearchResult(
            symbol = "005930",
            name = "삼성전자",
            market = "KOSPI",
            assetType = AssetType.STOCK,
            themeTags = listOf("메모리", "온디바이스 AI"),
        ),
        StockSearchResult(
            symbol = "OKLO",
            name = "Oklo",
            market = "NYSE",
            assetType = AssetType.STOCK,
            themeTags = listOf("SMR", "원전"),
        ),
        StockSearchResult(
            symbol = "IONQ",
            name = "IonQ",
            market = "NYSE",
            assetType = AssetType.STOCK,
            themeTags = listOf("양자컴퓨터"),
        ),
        StockSearchResult(
            symbol = "SCHD",
            name = "Schwab US Dividend Equity ETF",
            market = "NYSE Arca",
            assetType = AssetType.ETF,
            themeTags = listOf("배당", "ETF"),
        ),
        StockSearchResult(
            symbol = "TIGER 미국S&P500",
            name = "TIGER 미국S&P500",
            market = "ETF",
            assetType = AssetType.ETF,
            themeTags = listOf("미국지수", "ETF"),
        ),
    )

    private val details = mapOf(
        "NVDA" to StockDetail(
            symbol = "NVDA",
            name = "NVIDIA",
            market = "NASDAQ",
            assetType = AssetType.STOCK,
            price = "$1,142.32",
            changeRate = 2.84,
            marketCap = "$2.8T",
            tradingValue = "$42.1B",
            themeTags = listOf("AI 반도체", "데이터센터", "GPU"),
            chartPoints = pricePoints(910.0, 946.0, 972.0, 1_010.0, 1_052.0, 1_095.0, 1_142.0),
            financials = listOf(
                FinancialMetric("PER", "64.2x", "고성장 프리미엄"),
                FinancialMetric("매출 성장", "+126%", "최근 분기 YoY"),
                FinancialMetric("영업이익률", "64%", "데이터센터 중심"),
                FinancialMetric("배당", "0.02%", "성장주 성격"),
            ),
            summary = "AI 가속기 수요와 데이터센터 투자 사이클의 중심 종목입니다.",
        ),
        "005930" to StockDetail(
            symbol = "005930",
            name = "삼성전자",
            market = "KOSPI",
            assetType = AssetType.STOCK,
            price = "78,400원",
            changeRate = 1.17,
            marketCap = "468조원",
            tradingValue = "1.9조원",
            themeTags = listOf("메모리", "HBM", "온디바이스 AI"),
            chartPoints = pricePoints(71_200.0, 72_600.0, 74_100.0, 73_800.0, 75_900.0, 77_200.0, 78_400.0),
            financials = listOf(
                FinancialMetric("PER", "18.7x", "업황 회복 반영"),
                FinancialMetric("PBR", "1.4x", "대형주 평균권"),
                FinancialMetric("영업이익률", "15%", "반도체 회복 구간"),
                FinancialMetric("배당수익률", "1.8%", "분기 배당"),
            ),
            summary = "메모리 업황 회복과 HBM 공급 확대 기대가 핵심입니다.",
        ),
        "OKLO" to StockDetail(
            symbol = "OKLO",
            name = "Oklo",
            market = "NYSE",
            assetType = AssetType.STOCK,
            price = "$14.88",
            changeRate = 12.46,
            marketCap = "$1.7B",
            tradingValue = "$1.4B",
            themeTags = listOf("SMR", "원전", "전력"),
            chartPoints = pricePoints(9.4, 9.9, 10.8, 11.2, 12.6, 13.1, 14.9),
            financials = listOf(
                FinancialMetric("매출", "Pre-revenue", "개발 단계"),
                FinancialMetric("현금", "$290M", "프로젝트 자금"),
                FinancialMetric("변동성", "High", "테마 민감"),
                FinancialMetric("배당", "-", "성장 투자"),
            ),
            summary = "SMR 전력 공급 기대와 데이터센터 전력 수요가 맞물린 고변동성 테마주입니다.",
        ),
        "IONQ" to StockDetail(
            symbol = "IONQ",
            name = "IonQ",
            market = "NYSE",
            assetType = AssetType.STOCK,
            price = "$41.21",
            changeRate = 8.73,
            marketCap = "$8.9B",
            tradingValue = "$920M",
            themeTags = listOf("양자컴퓨터", "클라우드"),
            chartPoints = pricePoints(29.2, 31.1, 30.4, 34.8, 37.0, 39.6, 41.2),
            financials = listOf(
                FinancialMetric("매출 성장", "+77%", "초기 시장"),
                FinancialMetric("R&D 비중", "High", "기술 투자"),
                FinancialMetric("현금", "$410M", "운영 자금"),
                FinancialMetric("배당", "-", "성장 투자"),
            ),
            summary = "양자컴퓨팅 상용화 기대를 가장 직접적으로 반영하는 순수 테마 종목입니다.",
        ),
        "SCHD" to StockDetail(
            symbol = "SCHD",
            name = "Schwab US Dividend Equity ETF",
            market = "NYSE Arca",
            assetType = AssetType.ETF,
            price = "$79.12",
            changeRate = 0.42,
            marketCap = "$58B AUM",
            tradingValue = "$612M",
            themeTags = listOf("배당", "ETF", "현금흐름"),
            chartPoints = pricePoints(73.8, 74.1, 75.6, 76.4, 77.2, 78.6, 79.1),
            financials = listOf(
                FinancialMetric("분배수익률", "3.5%", "분기 분배"),
                FinancialMetric("보수", "0.06%", "저비용 ETF"),
                FinancialMetric("보유종목", "100+", "미국 배당주"),
                FinancialMetric("스타일", "Value", "현금흐름 중심"),
            ),
            summary = "배당 성장과 낮은 보수를 선호하는 장기 투자자에게 자주 비교되는 ETF입니다.",
        ),
        "TIGER 미국S&P500" to StockDetail(
            symbol = "TIGER 미국S&P500",
            name = "TIGER 미국S&P500",
            market = "ETF",
            assetType = AssetType.ETF,
            price = "18,965원",
            changeRate = 0.31,
            marketCap = "4.1조원 AUM",
            tradingValue = "842억원",
            themeTags = listOf("미국지수", "ETF", "장기투자"),
            chartPoints = pricePoints(17_420.0, 17_760.0, 18_020.0, 18_180.0, 18_430.0, 18_720.0, 18_965.0),
            financials = listOf(
                FinancialMetric("추종지수", "S&P 500", "미국 대형주"),
                FinancialMetric("보수", "0.07%", "국내 상장 ETF"),
                FinancialMetric("환노출", "Unhedged", "달러 영향"),
                FinancialMetric("분배", "분기", "상품 기준 확인"),
            ),
            summary = "국내 계좌에서 미국 대표지수에 투자할 수 있는 핵심 지수형 ETF입니다.",
        ),
    )

    private fun pricePoints(vararg closes: Double): List<PricePoint> {
        return closes.mapIndexed { index, close ->
            PricePoint(
                label = "D${index + 1}",
                close = close,
            )
        }
    }
}
